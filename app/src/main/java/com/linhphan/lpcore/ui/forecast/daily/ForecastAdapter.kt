package com.linhphan.lpcore.ui.forecast.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.lpcore.R
import com.linhphan.lpcore.databinding.ItemForecastBinding
import com.linhphan.lpcore.ui.forecast.model.HourlyForecastUiItem

class ForecastAdapter : ListAdapter<HourlyForecastUiItem, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {

    fun updateData(newItems: List<HourlyForecastUiItem>) {
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
        fun bind(item: HourlyForecastUiItem) {
            binding.tvHour.text = item.hour
            binding.tvTempMax.text = item.tempMax
            binding.tvPrecipitation.text = item.precipitationProbability
            binding.ivWeatherIcon.setImageResource(item.iconRes ?: R.drawable.ic_launcher_foreground)
        }
    }

    class ForecastDiffCallback : DiffUtil.ItemCallback<HourlyForecastUiItem>() {
        override fun areItemsTheSame(oldItem: HourlyForecastUiItem, newItem: HourlyForecastUiItem): Boolean {
            return oldItem.hour == newItem.hour
        }

        override fun areContentsTheSame(oldItem: HourlyForecastUiItem, newItem: HourlyForecastUiItem): Boolean {
            return oldItem == newItem
        }
    }
}