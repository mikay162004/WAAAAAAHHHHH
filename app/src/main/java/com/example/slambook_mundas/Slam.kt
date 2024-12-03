package com.example.slambook_mundas

data class Slam(
    val name: String,
    val nickname: String,
    val age: Int? = null,
    val birthday: String? = null,
    val zodiacSign: String? = null,
    val hobbies: String? = null,
    val favorites: String? = null,
    val message: String? = null,
    val isFriend: Boolean // Add a flag to distinguish friends' slams
)

