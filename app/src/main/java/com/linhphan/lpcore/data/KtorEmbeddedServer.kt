package com.linhphan.lpcore.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorEmbeddedServer @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private var server: EmbeddedServer<*,*>? = null
    val baseUrl: String
        get() = "http://localhost:8080/"

    fun start() {
        if (server == null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    server = embeddedServer(CIO, port = 8080, module = {
                        module()
                    }).start(wait = false)
                    Timber.tag("KtorEmbeddedServer").d("Ktor server started at $baseUrl")
                } catch (e: Exception) {
                    Timber.tag("KtorEmbeddedServer").e(e, "Failed to start Ktor server")
                }
            }
        }
    }

    fun stop() {
        server?.stop(1000, 10000)
        server = null
        Timber.tag("KtorEmbeddedServer").d("Ktor server stopped")
    }

    private fun Application.module() {
        // No specific configuration needed for ContentNegotiation for simple string responses
        // But if you want to return JSON objects automatically, you would configure it here.
        install(ContentNegotiation)

        routing {
            addStub(routing = this, path = "/data/2.5/forecast", fileName = "forecast_response.json")
        }
    }

    private fun addStub(routing: Routing, path: String, fileName: String, status: HttpStatusCode? = HttpStatusCode.OK) {
        routing.get(path) {
            val jsonResponse = loadJsonFromAsset(fileName)
            if (jsonResponse != null) {
                call.respondText(jsonResponse, ContentType.Application.Json, status)
            } else {
                call.respondText("Mock data not found", status = HttpStatusCode.InternalServerError)
            }
        }
    }

    private fun loadJsonFromAsset(fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            Timber.tag("KtorEmbeddedServer").e(ex, "Error reading asset file: $fileName")
            null
        }
    }
}