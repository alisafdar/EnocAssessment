package com.alisafdar.enocassessment.data.network

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.alisafdar.enocassessment.data.persistance.UserPreferences
import com.alisafdar.enocassessment.data.network.apis.TokenRefreshApi
import com.alisafdar.enocassessment.data.repository.BaseRepository
import com.alisafdar.enocassessment.data.responses.TokenResponse
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    context: Context, private val tokenApi: TokenRefreshApi
) : Authenticator, BaseRepository(tokenApi) {

    private val appContext = context.applicationContext

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val tokenResponse = getUpdatedToken()) {
                is Resource.Success -> {
                    userPreferences.saveAccessTokens(
                        tokenResponse.value.access_token!!, tokenResponse.value.refresh_token!!
                    )
                    response.request.newBuilder().header("Authorization", "Bearer ${tokenResponse.value.access_token}").build()
                }
                else -> null
            }
        }
    }

    private suspend fun getUpdatedToken(): Resource<TokenResponse> {
        val refreshToken = userPreferences.refreshToken.first()
        return safeApiCall { tokenApi.refreshAccessToken(refreshToken) }
    }

}