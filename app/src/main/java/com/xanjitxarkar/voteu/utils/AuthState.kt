package com.xanjitxarkar.voteu.utils

import java.lang.Exception

sealed class AuthState<out R> {
    data class Success<out T>(val user:T):AuthState<T>()
    data class Error(val exception: Exception):AuthState<Nothing>()
    object Loading: AuthState<Nothing>()

}