package com.example.chatapp.others

sealed class ResourceResult <T> (val data: T? = null, val message: String? = null) {
    class Loading<T>: ResourceResult<T>()
    class Success<T>(data: T): ResourceResult<T>(data = data)
    class Error<T>(message: String): ResourceResult<T>(message = message)
}