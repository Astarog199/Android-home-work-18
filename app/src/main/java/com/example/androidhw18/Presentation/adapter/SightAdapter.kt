package com.example.androidhw18.Presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidhw18.Data.Sight
import com.example.androidhw18.databinding.SightItemBinding

class SightAdapter(private var data: List<Sight>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SightItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.getOrNull(position)

        with(holder.binding){
            name.text = item?.name ?: ""
            item?.let {
                Glide
                    .with(photoSight.context)
                    .load(it.path)
                    .into(photoSight)
            }
        }
    }
}

