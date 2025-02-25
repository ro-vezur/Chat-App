package com.example.chatapp.helpers.strings

fun String.containsDigit(): Boolean {
    return this.any { it.isDigit() }
}