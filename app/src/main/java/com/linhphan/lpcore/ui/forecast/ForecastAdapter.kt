package com.linhphan.lpcore.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.lpcore.databinding.ItemForecastBinding

class ForecastAdapter : ListAdapter<ForecastUiItem, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {

    fun updateData(newItems: List<ForecastUiItem>) {
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
        fun bind(item: ForecastUiItem) {
            binding.tvDate.text = item.date
            binding.tvDescription.text = item.description
            binding.tvTempMax.text = item.tempMax
            binding.tvTempMin.text = item.tempMin
        }
    }

    class ForecastDiffCallback : DiffUtil.ItemCallback<ForecastUiItem>() {
        override fun areItemsTheSame(oldItem: ForecastUiItem, newItem: ForecastUiItem): Boolean {
            // Assuming date string is unique enough for list diffing, or we could add an ID if available.
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: ForecastUiItem, newItem: ForecastUiItem): Boolean {
            return oldItem == newItem
        }
    }
}