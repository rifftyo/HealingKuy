package com.rifftyo.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rifftyo.core.databinding.ItemCityBinding
import com.rifftyo.core.domain.model.City

class CityAdapter : ListAdapter<City, CityAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((City) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ListViewHolder(private val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: City) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.imgCity)
            binding.tvCity.text = data.name
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<City> =
            object : DiffUtil.ItemCallback<City>() {
                override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                    return oldItem.name == newItem.name
                }
                override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                    return oldItem == newItem
                }
            }
    }
}