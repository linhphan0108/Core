package com.linhphan.lpcore.debug

import android.content.Context
import com.linhphan.lpcore.data.AppConfiguration
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@ExperimentalCoroutinesApi
class AppConfigViewModelTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @MockK
    lateinit var context: Context

    @MockK(relaxed = true)
    lateinit var appConfiguration: AppConfiguration

    private lateinit var viewModel: AppConfigViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        
        every { appConfiguration.isEmbeddedServerEnabled } returns true
        
        viewModel = AppConfigViewModel(context, testDispatcher, appConfiguration)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setEmbeddedServerEnabled updates config and emits event`() = runTest(testDispatcher) {
        // Given
        every { appConfiguration.isEmbeddedServerEnabled } returns false
        val events = mutableListOf<AppConfigViewModel.AppConfigEvent>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { events.add(it) }
        }

        // When
        viewModel.setEmbeddedServerEnabled(true)
        advanceUntilIdle()

        // Then
        verify { appConfiguration.isEmbeddedServerEnabled = true }
        assertEquals(true, viewModel.isEmbeddedServerEnabled.value)
        assertTrue(events.any { it is AppConfigViewModel.AppConfigEvent.ConfigurationChanged })
        
        job.cancel()
    }

    @Test
    fun `clearCache clears cache directory`() = runTest(testDispatcher) {
        // Given
        val cacheDir = tempFolder.newFolder("cache")
        File(cacheDir, "test_cache_file").createNewFile()
        
        every { context.cacheDir } returns cacheDir
        
        val events = mutableListOf<AppConfigViewModel.AppConfigEvent>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { events.add(it) }
        }

        // When
        viewModel.clearCache()
        advanceUntilIdle()
        
        // Then
        assertEquals(0, cacheDir.listFiles()?.size ?: 0)
        assertTrue(events.any { it is AppConfigViewModel.AppConfigEvent.DataCleared && it.message == "Cache Cleared" })
        
        job.cancel()
    }

    @Test
    fun `clearSharedPrefs clears shared prefs directory`() = runTest(testDispatcher) {
        // Given
        val rootDir = tempFolder.newFolder("data")
        val sharedPrefsDir = File(rootDir, "shared_prefs")
        sharedPrefsDir.mkdirs()
        File(sharedPrefsDir, "prefs.xml").createNewFile()
        
        every { context.filesDir } returns File(rootDir, "files")
        
        val events = mutableListOf<AppConfigViewModel.AppConfigEvent>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { events.add(it) }
        }

        // When
        viewModel.clearSharedPrefs()
        advanceUntilIdle()
        
        // Then
        assertFalse(sharedPrefsDir.exists())
        assertTrue(events.any { it is AppConfigViewModel.AppConfigEvent.DataCleared && it.message == "Shared Prefs Cleared" })
        
        job.cancel()
    }

    @Test
    fun `clearDatabases clears databases directory`() = runTest(testDispatcher) {
        // Given
        val rootDir = tempFolder.newFolder("data_db")
        val dbDir = File(rootDir, "databases")
        dbDir.mkdirs()
        File(dbDir, "app.db").createNewFile()
        
        every { context.filesDir } returns File(rootDir, "files")
        
        val events = mutableListOf<AppConfigViewModel.AppConfigEvent>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { events.add(it) }
        }

        // When
        viewModel.clearDatabases()
        advanceUntilIdle()
        
        // Then
        assertFalse(dbDir.exists())
        assertTrue(events.any { it is AppConfigViewModel.AppConfigEvent.DataCleared && it.message == "Databases Cleared" })
        
        job.cancel()
    }
}