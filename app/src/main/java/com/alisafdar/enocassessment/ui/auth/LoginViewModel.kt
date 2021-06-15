package com.alisafdar.enocassessment.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.data.repository.AuthRepository
import com.alisafdar.enocassessment.data.repository.UserRepository
import com.alisafdar.enocassessment.data.responses.LoginResponse
import com.alisafdar.enocassessment.data.responses.User
import com.alisafdar.enocassessment.ui.base.BaseViewModel
import com.alisafdar.enocassessment.utils.LoginValidator
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepository :UserRepository
) : BaseViewModel(repository) {

    private val uiState = MutableLiveData<LoginDataState>()

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(email: String, password: String) {
        if (areUserCredentialsValid(email, password)) {
            viewModelScope.launch {
                runCatching {
                    repository.login(email, password)
                }.onSuccess {
                    uiState.postValue(LoginDataState.Success(it))
                }.onFailure {
                    uiState.postValue(LoginDataState.Error(""))
                }
            }
        }
    }

    private fun areUserCredentialsValid(userEmail: String, password: String): Boolean {
        return if (!LoginValidator.isEmailValid(userEmail)) {
            uiState.postValue(LoginDataState.InValidEmailState)
            false
        } else if (!LoginValidator.isPasswordValid(password)) {
            uiState.postValue(LoginDataState.InValidPasswordState)
            false
        } else {
            uiState.postValue(LoginDataState.ValidCredentialsState)
            true
        }
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        repository.saveAccessTokens(accessToken, refreshToken)
    }

    suspend fun insertUser(user: User) {
        userRepository.insertUser(user)
    }

    fun getObserverState() = uiState
}