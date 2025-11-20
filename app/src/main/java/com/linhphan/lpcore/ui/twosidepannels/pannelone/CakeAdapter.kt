package com.linhphan.lpcore.ui.twosidepannels.pannelone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.linhphan.lpcore.data.Cake
import com.linhphan.lpcore.databinding.ItemCakeBinding

class CakeAdapter(private val onItemClicked: (Cake) -> Unit) :
    ListAdapter<Cake, CakeAdapter.CakeViewHolder>(DiffCallback) {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = ItemCakeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, position == selectedPosition)
        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onItemClicked(current)
        }
    }

    class CakeViewHolder(private val binding: ItemCakeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cake: Cake, isSelected: Boolean) {
            binding.tvCakeName.text = cake.name
            binding.root.isActivated = isSelected
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Cake>() {
            override fun areItemsTheSame(oldItem: Cake, newItem: Cake): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cake, newItem: Cake): Boolean {
                return oldItem == newItem
            }
        }
    }
}