package com.myapplication.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.myapplication.R
import com.myapplication.databinding.FragmentImageDetailsBinding
import com.myapplication.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailsFragment :
    BaseFragment<FragmentImageDetailsBinding>(R.layout.fragment_image_details) {

    private val argument: ImageDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            imageDetails = argument.image
        }
    }
}