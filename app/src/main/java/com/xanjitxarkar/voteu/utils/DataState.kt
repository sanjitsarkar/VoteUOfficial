package com.xanjitxarkar.voteu.utils

import java.lang.Exception

sealed class DataState<out R> {
    data class Success<out T>(val data:T):DataState<T>()
    data class Error(val exception: Exception):DataState<Nothing>()
    data class Info(val info: String):DataState<Nothing>()
    object Loading: DataState<Nothing>()
    data class NoData(val title:String): DataState<Nothing>()

}