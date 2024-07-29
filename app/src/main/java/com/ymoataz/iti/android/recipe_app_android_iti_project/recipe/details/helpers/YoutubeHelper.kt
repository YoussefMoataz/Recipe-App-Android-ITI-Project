package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.details.helpers

object YoutubeHelper {
    fun getVideoId(url: String): String {
        val regex = "(?:https?://)?(?:www\\.)?(?:youtube\\.com(?:/[^/\\s]+/[^/\\s]+/|/[^/\\s]+/|/v/|/e(?:mbed)?/|.*[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})"
        val pattern = Regex(regex)
        val matchResult = pattern.find(url)
        return matchResult?.groups?.get(1)?.value ?: ""
    }
}