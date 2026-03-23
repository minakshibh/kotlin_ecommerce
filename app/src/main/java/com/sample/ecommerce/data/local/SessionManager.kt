package com.sample.ecommerce.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val keyCurrentUserId = "current_user_id"
    private val keyAuthToken = "auth_token"

    fun getCurrentUserId(): Long? {
        val id = prefs.getLong(keyCurrentUserId, -1L)
        return if (id >= 0) id else null
    }

    fun setCurrentUserId(userId: Long?) {
        prefs.edit().putLong(keyCurrentUserId, userId ?: -1L).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(keyAuthToken, null)?.takeIf { it.isNotBlank() }
    }

    fun setAuthToken(token: String?) {
        prefs.edit().putString(keyAuthToken, token ?: "").apply()
    }

    fun logout() {
        setCurrentUserId(null)
        setAuthToken(null)
    }

    companion object {
        private const val PREFS_NAME = "ecommerce_session"
    }
}
