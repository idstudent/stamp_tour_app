package com.ljystamp.stamp_tour_app.api.model

data class SavedLocation(
    val contentId: Int = 0,
    val contentTypeId: Int = 0,
    val title: String = "",
    val address: String = "",
    val image: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isVisited: Boolean = false,
    val savedAt: com.google.firebase.Timestamp? = null
)