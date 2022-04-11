package com.landable.app.data.network

import com.landable.app.common.APIException
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun apiRequest(call: suspend () -> Response<ResponseBody>): Response<ResponseBody> {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error code:${response.code()}")
            throw APIException(message.toString())
        }
    }
}