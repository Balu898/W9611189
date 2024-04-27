package uk.ac.tees.mad.w9611189.ui.util

import android.content.Context
import android.widget.Toast

object Helper {
    fun showToast(context: Context,msg:String) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}