package com.sample.ecommerce.data.remote.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Parses sample JSON strings into [ApiResponse] and [AuthResponseData].
 * Use when API is unavailable and you want to return mock data from sample JSON.
 */
object SampleResponseParser {

    private val gson = Gson()

    private val apiResponseType = object : TypeToken<ApiResponse<AuthResponseData>>() {}.type

    /**
     * Parses a success JSON string (e.g. [SampleAuthResponses.SUCCESS_LOGIN]) into [AuthResponseData].
     * @return [AuthResponseData] if parsing succeeds and success=true and data is non-null
     * @throws Exception if JSON is invalid or success=false
     */
    fun parseSuccessResponse(json: String): AuthResponseData {
        val response = gson.fromJson<ApiResponse<AuthResponseData>>(json, apiResponseType)
        if (!response.success || response.data == null) {
            throw IllegalArgumentException("Not a success response: ${response.message}")
        }
        return response.data
    }

    /**
     * Parses any API response JSON into [ApiResponse]<[AuthResponseData]>.
     */
    fun parseResponse(json: String): ApiResponse<AuthResponseData> {
        return gson.fromJson(json, apiResponseType)
    }

    /**
     * Parses an error JSON string and returns the message (e.g. "Invalid email or password").
     */
    fun parseErrorMessage(json: String): String {
        val response = parseResponse(json)
        return response.message
    }
}
