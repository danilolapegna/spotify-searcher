package com.spotifysearch.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.spotifysearch.R

fun Fragment.runOnUiThread(action: () -> Unit) {
    activity?.runOnUiThread { action.invoke() }
}

fun AppCompatActivity.hideInputMethod(view: View? = null) {
    val focusView: View? = view ?: currentFocus
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
    focusView?.let { inputManager?.hideSoftInputFromWindow(it.windowToken, 0) }
}

fun AppCompatActivity.getFragmentInContainer(): Fragment? {
    return supportFragmentManager.findFragmentById(R.id.fragmentContainer)
}

fun AppCompatActivity.displayToast(stringRes: Int, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, stringRes, length).show()
}

fun AppCompatActivity.switchFragment(
        newFragment: Fragment?,
        containerId: Int,
        addToBackStack: Boolean = false
) {
    if (newFragment == null || newFragment.isAdded) return

    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(containerId, newFragment, newFragment.javaClass.name)

    if (addToBackStack) transaction.addToBackStack(null)
    transaction.commit()
}