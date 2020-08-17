package com.xanjitxarkar.voteu.utils



import android.content.Context
import android.view.View
import android.widget.Toast


object  Utils {
    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
    fun View.show() {
        this.visibility = View.VISIBLE
    }
    fun View.hide() {
        this.visibility = View.INVISIBLE
    }
    fun View.gone() {
        this.visibility = View.GONE
    }
    fun Context.toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}