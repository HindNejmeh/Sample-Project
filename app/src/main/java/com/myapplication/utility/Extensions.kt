package com.myapplication.utility

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.myapplication.domain.base.State
import com.myapplication.domain.base.fallbackMessage

val Activity.view: View get() = findViewById(android.R.id.content)

fun Activity.showSnackbar(
    message: String?, duration: Int = Snackbar.LENGTH_SHORT, anchor: View? = null
) {
    view.showSnackbar(message, duration, anchor)
}

fun Activity.showSnackbar(
    @StringRes stringId: Int, duration: Int = Snackbar.LENGTH_SHORT, anchor: View? = null
) {
    view.showSnackbar(stringId, duration, anchor)
}

fun View.hideKeyboard() {
    context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSnackbar(
    @StringRes stringId: Int, duration: Int = Snackbar.LENGTH_SHORT, anchor: View? = null
) {
    Snackbar.make(this, stringId, duration).apply { if (anchor != null) anchorView = anchor }.show()
}

fun View.showSnackbar(
    message: String?, duration: Int = Snackbar.LENGTH_SHORT, anchor: View? = null
) {
    Snackbar.make(this, message ?: "null", duration)
        .apply { if (anchor != null) anchorView = anchor }.show()
}

fun ViewGroup.clearInputErrors() {
    forEach {
        if (it is TextInputLayout) {
            it.error = null
        } else if (it is ViewGroup) {
            it.clearInputErrors()
        }
    }
}

fun ViewGroup.setInputEnabled(isEnabled: Boolean) {
    forEach {
        if (it is Button || it is EditText) {
            it.isEnabled = isEnabled
        } else if (it is ViewGroup) {
            it.setInputEnabled(isEnabled)
        }
    }
}

fun Fragment.showSnackbar(
    @StringRes stringId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    fromActivity: Boolean = false,
    anchor: View? = null,
) {
    if (fromActivity) {
        activity?.showSnackbar(stringId, duration, anchor)
    } else {
        view?.showSnackbar(stringId, duration, anchor)
    }
}

fun Fragment.showSnackbar(
    message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    fromActivity: Boolean = false,
    anchor: View? = null,
) {
    if (fromActivity) {
        activity?.showSnackbar(message, duration, anchor)
    } else {
        view?.showSnackbar(message, duration, anchor)
    }
}

fun Fragment.showStateMessage(
    state: State<*>,
    duration: Int = Snackbar.LENGTH_SHORT,
    fromActivity: Boolean = false,
    asToast: Boolean = false,
    snackbarAnchor: View? = null,
) {
    val message = state.message
    if (message != null) {
        if (asToast) {
            showToast(requireContext(), message, duration)
        } else {
            showSnackbar(message, duration, fromActivity, snackbarAnchor)
        }

        return
    }

    val defaultMessage = state.fallbackMessage
    if (defaultMessage != null) {
        if (asToast) {
            showToast(requireContext(), defaultMessage, duration)
        } else {
            showSnackbar(defaultMessage, duration, fromActivity, snackbarAnchor)
        }
    }
}

fun Fragment.clearInputErrors() {
    (view as? ViewGroup)?.clearInputErrors()
}

fun Fragment.setInputEnabled(isEnabled: Boolean) {
    (view as? ViewGroup)?.setInputEnabled(isEnabled)
}

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

fun NavController.navigateSafely(@IdRes actionId: Int, @IdRes currentDestinationId: Int) {
    if (currentDestination?.id == currentDestinationId) navigate(actionId)
}

fun NavController.navigateSafely(directions: NavDirections, @IdRes currentDestinationId: Int) {
    if (currentDestination?.id == currentDestinationId) navigate(directions)
}

@MainThread
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, onChanged: (T) -> Unit) {
    val lifecycleOwner = if (this is Fragment) viewLifecycleOwner else this

    liveData.observe(lifecycleOwner, onChanged)
}

fun View.setPercentWidth(percentage: Double) {
    updateLayoutParams { width = (screenWidth * percentage).toInt() }
}
