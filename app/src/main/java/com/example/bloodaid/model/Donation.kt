package com.example.bloodaid.model

import java.io.Serializable
import java.util.UUID

data class Donation(
    var fullName: String = "",
    var gender: String = "",
    var type: String = "",
    var bloodType: String = "",
    var description: String = "",
    val uuid: String = ""
) : Serializable