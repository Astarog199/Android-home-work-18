package com.example.androidhw18

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidhw18.Data.Sight
import com.example.androidhw18.databinding.SightItemBinding

class SightAdapter(private var data: List<Sight>): RecyclerView.Adapter<MysimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MysimpleViewHolder {
        return MysimpleViewHolder(
            SightItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MysimpleViewHolder, position: Int) {
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

class  MysimpleViewHolder(val binding: SightItemBinding) : RecyclerView.ViewHolder(binding.root)