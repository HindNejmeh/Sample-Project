package com.myapplication.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.myapplication.domain.base.Unique

abstract class BasePagingDataAdapter<T : Unique, V : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    PagingDataAdapter<T, BaseViewHolder<V>>(getUniqueDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder(
        DataBindingUtil.inflate<V>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false,
        )
    )

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        val item = getItem(position)
        bind(item, holder, position)

        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(item: T?, holder: BaseViewHolder<V>, position: Int)
}

fun <T : Unique> getUniqueDiffItemCallback() = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.uniqueId == newItem.uniqueId

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}