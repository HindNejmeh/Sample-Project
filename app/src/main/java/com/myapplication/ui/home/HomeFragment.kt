package com.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.myapplication.R
import com.myapplication.databinding.FragmentHomeBinding
import com.myapplication.ui.base.BaseFragment
import com.myapplication.ui.base.withBaseLoadStateFooter
import com.myapplication.ui.main.MainViewModel
import com.myapplication.utility.ConnectionManager
import com.myapplication.utility.mapToDataBindingState
import com.myapplication.utility.showSnackbar
import com.myapplication.utility.viewLifecycleValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    View.OnClickListener {

    @Inject
    lateinit var connectionManager: ConnectionManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private var adapter: ImageAdapter by viewLifecycleValues()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = if (mainViewModel.isUserLoggedIn) {
        adapter = ImageAdapter()
        super.onCreateView(inflater, container, savedInstanceState)
    } else {
        findNavController().navigate(R.id.to_auth)
        null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            onClickListener = this@HomeFragment
            adapter = this@HomeFragment.adapter.withBaseLoadStateFooter(connectionManager)
            state = viewModel.initialLoadState.mapToDataBindingState()
        }

        lifecycleScope.launch {
            viewModel.images.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                binding.failure.retry -> if (connectionManager.isConnected) {
                    this@HomeFragment.adapter.retry()
                } else {
                    showSnackbar(R.string.message_internet_unavailable)
                }
            }
        }
    }
}