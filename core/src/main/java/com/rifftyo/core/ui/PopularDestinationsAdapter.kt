package com.rifftyo.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rifftyo.core.databinding.ItemPopularDestinationsBinding
import com.rifftyo.core.domain.model.Destinations

class PopularDestinationsAdapter : ListAdapter<Destinations, PopularDestinationsAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Destinations) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemPopularDestinationsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ListViewHolder(private val binding: ItemPopularDestinationsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Destinations) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.imgDestination)
            binding.tvName.text = data.name
            binding.tvCity.text = data.city
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Destinations> =
            object : DiffUtil.ItemCallback<Destinations>() {
                override fun areItemsTheSame(oldItem: Destinations, newItem: Destinations): Boolean {
                    return oldItem.name == newItem.name
                }
                override fun areContentsTheSame(oldItem: Destinations, newItem: Destinations): Boolean {
                    return oldItem == newItem
                }
            }
    }
}