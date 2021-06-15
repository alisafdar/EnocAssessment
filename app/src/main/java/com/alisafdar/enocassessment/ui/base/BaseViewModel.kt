package com.alisafdar.enocassessment.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.alisafdar.enocassessment.data.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {
    suspend fun logout() = withContext(Dispatchers.IO) { repository.logout() }
}