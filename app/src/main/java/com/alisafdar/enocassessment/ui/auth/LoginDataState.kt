package com.alisafdar.enocassessment.ui.auth

import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.data.responses.LoginResponse

sealed class LoginDataState {
    data class Error(val message: String?) : LoginDataState()
    object ValidCredentialsState : LoginDataState()
    object InValidEmailState : LoginDataState()
    object InValidPasswordState : LoginDataState()
    class Success(val body: Resource<LoginResponse>? = null) : LoginDataState()
}