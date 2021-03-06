package com.johnzar.navigationtransition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johnzar.navigationtransition.databinding.ItemEnvelopeBinding

object EnvelopeItemDiffUtilCallback : DiffUtil.ItemCallback<Envelope>() {
    override fun areItemsTheSame(
        oldItem: Envelope,
        newItem: Envelope
    ): Boolean = oldItem.message == newItem.message

    override fun areContentsTheSame(
        oldItem: Envelope,
        newItem: Envelope
    ): Boolean = oldItem.message == newItem.message

}

class EnvelopListAdapter :
    ListAdapter<Envelope, EnvelopListAdapter.EnvelopeViewHolder>(EnvelopeItemDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvelopeViewHolder {
        return EnvelopeViewHolder(
            ItemEnvelopeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EnvelopeViewHolder, position: Int) = holder.bind(getItem(position))

    inner class EnvelopeViewHolder(val binding: ItemEnvelopeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Envelope?) = binding.run {
            binding.tvMessage.text = item?.message
        }
    }

}