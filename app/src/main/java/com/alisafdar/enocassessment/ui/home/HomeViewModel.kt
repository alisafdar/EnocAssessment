package com.alisafdar.enocassessment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.data.repository.UserRepository
import com.alisafdar.enocassessment.data.responses.User
import com.alisafdar.enocassessment.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel(repository) {
    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>>
        get() = _user

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        _user.value = repository.getUser()
    }

    fun uploadImage(file : MultipartBody.Part?, id : RequestBody) = viewModelScope.launch {
        repository.uploadImage(file, id)
    }
}