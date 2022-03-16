package com.example.cartrack.others

import android.R
import android.view.View
import com.google.android.material.snackbar.Snackbar

class Function {

    companion object{
        fun showMessage(message: String?, view: View) {
            Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT)
                .setAction("close") { view1: View? -> }
                .setActionTextColor(view.resources.getColor(R.color.holo_red_light))
                .show()
        }
    }

}