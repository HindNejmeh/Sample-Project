package com.myapplication.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.myapplication.R
import com.myapplication.utility.viewLifecycleValues

abstract class BaseFragment<T : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    Fragment() {

    protected var binding: T by viewLifecycleValues()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTransitions(view)
    }

    @CallSuper
    protected open fun setUpTransitions(view: View) {
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        if (view is ViewGroup) view.isTransitionGroup = true

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    fun setUpSharedElementEnterTransition(
        @IdRes drawingViewId: Int = R.id.fragment_nav_host,
        @ColorRes scrimColorId: Int = R.color.transparent,
        isMotionArced: Boolean = false,
    ) {
        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true).apply {
            setDrawingViewId(drawingViewId)
            scrimColor = ContextCompat.getColor(requireContext(), scrimColorId)
            if (isMotionArced) setPathMotion(MaterialArcMotion())
        }
    }
}