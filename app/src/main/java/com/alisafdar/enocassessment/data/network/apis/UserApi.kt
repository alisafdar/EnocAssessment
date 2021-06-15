package com.alisafdar.enocassessment.data.network.apis

import com.alisafdar.enocassessment.data.responses.LoginResponse
import com.alisafdar.enocassessment.data.responses.UploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi : BaseApi {
    @GET("user")
    suspend fun getUser(): LoginResponse

    @Multipart
    @POST("upload_image")
    suspend fun uploadImage(@Part file : MultipartBody.Part?,
                     @Part("id") id : RequestBody) : UploadImageResponse
}