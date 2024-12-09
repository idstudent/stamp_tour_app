package com.ljystamp.stamp_tour_app.util

sealed class SaveResult {
    data class Success(val message: String) : SaveResult()
    data class Failure(val message: String) : SaveResult()
    data class LoginRequired(val message: String): SaveResult()
    data class MaxLimitReached(val message: String) : SaveResult()
}