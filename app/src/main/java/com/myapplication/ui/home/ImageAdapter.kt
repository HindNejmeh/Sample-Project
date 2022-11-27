package com.myapplication.ui.home

import android.view.View
import androidx.navigation.findNavController
import com.myapplication.R
import com.myapplication.databinding.ModelImageBinding
import com.myapplication.models.Image
import com.myapplication.ui.base.BasePagingDataAdapter
import com.myapplication.ui.base.BaseViewHolder
import com.myapplication.utility.navigateSafely
import com.myapplication.utility.setPercentWidth

class ImageAdapter : BasePagingDataAdapter<Image, ModelImageBinding>(R.layout.model_image) {

    override fun bind(item: Image?, holder: BaseViewHolder<ModelImageBinding>, position: Int) {
        holder.binding.apply {
            image = item

            photo.setPercentWidth(0.222)

            onClickListener = View.OnClickListener {
                val direction = HomeFragmentDirections.actionFragmentImageDetails(item!!)
                root.findNavController().navigateSafely(direction, R.id.fragment_home)
            }
        }
    }
}