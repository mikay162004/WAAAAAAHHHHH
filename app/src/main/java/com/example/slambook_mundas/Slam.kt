package com.example.slambook_mundas

// Slam data class
data class Slam(
    var name: String,
    val nickname: String,
    val age: Int? = null,
    val birthday: String? = null,
    val zodiacSign: String? = null,
    val hobbies: String? = null,
    val interests: String? = null,
    val sports: String? = null,
    val favorites: String? = null,
    val message: String? = null,
    var gender: String? = "not specified",
    val isFriend: Boolean,
    val imageUri: String? = null
)

// Default value for text
var text: String = "Default Value"
