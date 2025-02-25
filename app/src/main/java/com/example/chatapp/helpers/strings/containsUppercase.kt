package com.example.chatapp.helpers.strings

fun String.containsUppercase(): Boolean {
    return this.any { it.isUpperCase() }
}