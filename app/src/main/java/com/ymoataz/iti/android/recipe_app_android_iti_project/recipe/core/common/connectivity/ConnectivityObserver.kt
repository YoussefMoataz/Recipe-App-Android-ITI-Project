package com.ymoataz.iti.android.recipe_app_android_iti_project.recipe.core.common.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}