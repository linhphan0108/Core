package com.linhphan.lpcore.ui.forecast.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.lpcore.databinding.ItemDailyForecastBinding
import com.linhphan.lpcore.ui.forecast.model.DailyForecastUiItem

class DailyForecastAdapter(
    private val onItemClicked: (DailyForecastUiItem) -> Unit = {}
) : ListAdapter<DailyForecastUiItem, DailyForecastAdapter.DailyForecastViewHolder>(DailyForecastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding = ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DailyForecastViewHolder(private val binding: ItemDailyForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyForecastUiItem) {
            binding.tvDay.text = item.day
            binding.tvDate.text = item.date
            binding.tvTempMax.text = item.tempMax
            binding.tvTempMin.text = item.tempMin
            binding.tvPrecipitation.text = item.precipitationProbability
            binding.ivWeatherIcon.setImageResource(item.iconRes)

            binding.root.isSelected = item.isSelected
            binding.root.setOnClickListener {
                updateSelection(adapterPosition)
                onItemClicked(item)
            }
        }
    }

    private fun updateSelection(position: Int) {
        val currentList = currentList.toMutableList()
        currentList.forEachIndexed { index, item ->
            val newItem = item.copy(isSelected = index == position)
            currentList[index] = newItem
        }
        submitList(currentList)
    }

    class DailyForecastDiffCallback : DiffUtil.ItemCallback<DailyForecastUiItem>() {
        override fun areItemsTheSame(oldItem: DailyForecastUiItem, newItem: DailyForecastUiItem): Boolean {
            return oldItem.day == newItem.day && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DailyForecastUiItem, newItem: DailyForecastUiItem): Boolean {
            return oldItem == newItem
        }
    }
}