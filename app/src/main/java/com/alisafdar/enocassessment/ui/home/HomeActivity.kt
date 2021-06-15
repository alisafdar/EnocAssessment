package com.alisafdar.enocassessment.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alisafdar.enocassessment.R
import com.alisafdar.enocassessment.data.managers.PhotoManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.alisafdar.enocassessment.data.persistance.UserPreferences
import com.alisafdar.enocassessment.ui.auth.LoginActivity
import com.alisafdar.enocassessment.utils.startNewActivity
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun performLogout() = lifecycleScope.launch {
        viewModel.logout()
        startNewActivity(LoginActivity::class.java)
    }
}