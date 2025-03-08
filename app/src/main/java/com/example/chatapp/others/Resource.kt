package com.example.chatapp.others

sealed class Resource <T> (var data: T? = null, val message: String? = null) {
    class Loading<T>: Resource<T>()
    class Success<T>(data: T): Resource<T>(data = data)
    class Error<T>(message: String): Resource<T>(message = message)
}