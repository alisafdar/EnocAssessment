package com.alisafdar.enocassessment.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.alisafdar.enocassessment.R
import com.alisafdar.enocassessment.data.managers.PhotoManager
import com.alisafdar.enocassessment.data.network.Resource
import com.alisafdar.enocassessment.data.responses.User
import com.alisafdar.enocassessment.databinding.FragmentHomeBinding
import com.alisafdar.enocassessment.utils.UriUtils
import com.alisafdar.enocassessment.utils.handleApiError
import com.alisafdar.enocassessment.utils.visible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    @Inject
    lateinit var photoManager : PhotoManager

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.progressbar.visible(false)

        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visible(false)
                    updateUI(it.value)
                }
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })

        binding.editImageView.setOnClickListener {
            photoManager.showPhotoSelectDialog()
        }
    }

    private fun updateUI(user: User) {
        with(binding) {
            userNameTextView.text = user.name
            emailTextView.text = user.email
        }
    }

    private fun uploadImage(contentUri : Uri?){
        var fileUpload: MultipartBody.Part? = null

        val fullFilePath = UriUtils.getPathFromUri(context, contentUri)
        if (fullFilePath.isNotEmpty()) {
            val file = File(fullFilePath)
            val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())
            fileUpload = MultipartBody.Part.createFormData("photo", file.name, requestBody)
        }

        val id = "123334".toRequestBody("".toMediaTypeOrNull())
        viewModel.uploadImage(fileUpload, id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        photoManager.onActivityResult(requestCode, resultCode, data) { contentUri, _ ->
            context?.let { Glide.with(it).load(contentUri).into(binding.editImageView) }
            uploadImage(contentUri)
        }
    }
}