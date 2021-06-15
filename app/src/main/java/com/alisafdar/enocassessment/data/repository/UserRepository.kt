package com.alisafdar.enocassessment.data.repository

import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.data.network.apis.UserApi
import com.alisafdar.enocassessment.data.persistance.UserDao
import com.alisafdar.enocassessment.data.responses.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: UserApi, private val userDao: UserDao) : BaseRepository(api) {

    suspend fun getUser(): Resource<User> {
        val user : User = userDao.getUser()
        if (user.id > 0) {
            return safeCall { user }
        }

        return safeApiCall { api.getUser().user }
    }

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun uploadImage(file : MultipartBody.Part?, id : RequestBody) = safeApiCall { api.uploadImage(file, id) }
}