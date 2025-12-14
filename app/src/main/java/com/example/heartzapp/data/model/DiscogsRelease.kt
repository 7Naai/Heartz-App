package com.example.heartzapp.data.model

data class DiscogsRelease(
    val id: Int,
    val title: String,
    val year: Int?,
    val artists: List<Artist>,
    val images: List<Image>?
) {
    data class Artist(
        val name: String
    )

    data class Image(
        val uri: String
    )
}
