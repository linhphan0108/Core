package com.linhphan.lpcore.ui.forecast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ItemForecastBinding
import com.linhphan.lpcore.domain.model.DailyForecast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class ForecastAdapter : ListAdapter<DailyForecast, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {

    fun updateData(newItems: List<DailyForecast>) {
        submitList(newItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ForecastViewHolder(private val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyForecast) {
            val context = itemView.context
            // Display format: "Mon, 20 Nov 14:00"
            val dateFormat = SimpleDateFormat("EEE, dd MMM HH:mm", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault() // Use device timezone
            binding.tvDate.text = dateFormat.format(Date(item.date * 1000)) // API time is in seconds
            
            binding.tvDescription.text = item.weatherDescription.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            binding.tvTempMax.text = bindTemperature(context, item.tempMax)
            binding.tvTempMin.text = bindTemperature(context, item.tempMin)
        }

        fun bindTemperature(context: Context, temp: Double): String {
            return context.getString(R.string.temperature_celsius, temp.roundToInt())
        }
    }

    class ForecastDiffCallback : DiffUtil.ItemCallback<DailyForecast>() {
        override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem
        }
    }
}