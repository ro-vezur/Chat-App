package com.example.chatapp.helpers.media

fun extractPublicIdFromUrl(url: String): String? {
    val regex = Regex("""/upload/(?:v\d+/)?(.*)\.(jpg|jpeg|png|webp|gif|bmp)""")
    val match = regex.find(url)
    return match?.groups?.get(1)?.value
}