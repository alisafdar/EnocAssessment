package com.alisafdar.enocassessment.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import androidx.room.Room
import com.alisafdar.enocassessment.data.serializers.SecureDataSerializer
import com.alisafdar.enocassessment.data.persistance.AppDatabase
import com.alisafdar.enocassessment.data.persistance.UserDao
import com.alisafdar.enocassessment.data.persistance.UserPreferences
import com.alisafdar.enocassessment.data.security.Crypto
import com.alisafdar.enocassessment.datastore.data.PreferenceData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val builder =  Room
            .databaseBuilder(context, AppDatabase::class.java, "Enoc.db")
            .fallbackToDestructiveMigration()

        val factory = SupportFactory(SQLiteDatabase.getBytes("AnyStrongPassPhraseWillBeHere".toCharArray()))
        builder.openHelperFactory(factory)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context, crypto: Crypto): DataStore<PreferenceData> =
        context.createDataStore(fileName = "DataStoreTest.pb", serializer = SecureDataSerializer(crypto))

    @Singleton
    @Provides
    fun providesUserPreferences(dataStore: DataStore<PreferenceData>): UserPreferences = UserPreferences(dataStore)
}