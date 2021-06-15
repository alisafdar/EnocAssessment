package com.alisafdar.enocassessment.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.alisafdar.enocassessment.R
import dagger.hilt.android.AndroidEntryPoint
import com.alisafdar.enocassessment.data.persistance.UserPreferences
import com.alisafdar.enocassessment.ui.auth.LoginActivity
import com.alisafdar.enocassessment.ui.home.HomeActivity
import com.alisafdar.enocassessment.utils.startNewActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferences : UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreferences.accessToken.asLiveData().observe(this, Observer {
            val activity = if (it?.isEmpty() == true) LoginActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })
    }

}