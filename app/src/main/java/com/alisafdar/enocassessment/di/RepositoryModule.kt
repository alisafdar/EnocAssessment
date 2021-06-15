package com.alisafdar.enocassessment.di

import com.alisafdar.enocassessment.data.network.apis.UserApi
import com.alisafdar.enocassessment.data.persistance.UserDao
import com.alisafdar.enocassessment.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideUserRepository(userApi: UserApi, userDao: UserDao): UserRepository {
        return UserRepository(userApi, userDao)
    }
}