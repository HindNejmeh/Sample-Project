package com.myapplication.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import com.myapplication.R
import com.myapplication.databinding.ModelLoadStateBinding
import com.myapplication.utility.ConnectionManager
import com.myapplication.utility.showSnackbar

class BaseLoadStateAdapter(
    private val pagingDataAdapter: PagingDataAdapter<*, *>,
    private val connectionManager: ConnectionManager,
) : LoadStateAdapter<BaseViewHolder<ModelLoadStateBinding>>() {

    companion object {
        const val VIEW_TYPE_LOAD_STATE = -1
    }

    override fun getStateViewType(loadState: LoadState) = VIEW_TYPE_LOAD_STATE

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = BaseViewHolder(
        DataBindingUtil.inflate<ModelLoadStateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.model_load_state,
            parent,
            false,
        )
    )

    override fun onBindViewHolder(
        holder: BaseViewHolder<ModelLoadStateBinding>, loadState: LoadState
    ) {
        holder.binding.apply {
            this.loadState = loadState
            onClickListener = View.OnClickListener {
                if (connectionManager.isConnected) {
                    pagingDataAdapter.retry()
                } else {
                    root.showSnackbar(R.string.message_internet_unavailable)
                }
            }

            executePendingBindings()
        }
    }
}

fun PagingDataAdapter<*, *>.withBaseLoadStateFooter(connectionManager: ConnectionManager) =
    withLoadStateFooter(BaseLoadStateAdapter(this, connectionManager))