package com.alisafdar.enocassessment.data.repository

import com.alisafdar.enocassessment.data.network.apis.BaseApi
import com.alisafdar.enocassessment.data.network.apis.SafeCall

abstract class BaseRepository(private val api: BaseApi) : SafeCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}