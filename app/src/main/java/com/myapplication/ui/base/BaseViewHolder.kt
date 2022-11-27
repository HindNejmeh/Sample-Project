package com.myapplication.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class BaseViewHolder<out T : ViewDataBinding>(val binding: T) : ViewHolder(binding.root)