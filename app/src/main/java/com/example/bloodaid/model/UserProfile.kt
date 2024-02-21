package com.example.bloodaid.model

data class UserProfile(
    var name : String? = "",
    var bloodType : String? = "",
    var gender : String? = "",
    var age : String? = "",
    var phone : String? = "",
    var profileImageUrl: String? = "",
    var address : String? = ""
)
