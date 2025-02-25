package com.example.chatapp.helpers.strings

fun String.containsLowercase(): Boolean {
    return this.any { it.isLowerCase() }
}