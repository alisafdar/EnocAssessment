package com.alisafdar.enocassessment.di

import android.app.Activity
import com.alisafdar.enocassessment.data.managers.PhotoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object PhotoManagerModule {

    @Provides
    fun providePhotoManager(activity: Activity) = PhotoManager(activity)
}