package com.alisafdar.enocassessment.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alisafdar.enocassessment.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}