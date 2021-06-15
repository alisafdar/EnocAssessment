package com.alisafdar.enocassessment.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.alisafdar.enocassessment.R
import com.alisafdar.enocassessment.databinding.DialogSelectPhotoBinding
import com.alisafdar.enocassessment.databinding.FragmentLoginBinding

class PhotoSelectDialog(
    context: Context?, val takePhoto: () -> Unit, val selectGallery: () -> Unit
) : AppCompatDialog(context) {

    private lateinit var binding: DialogSelectPhotoBinding

    init {
        setCancelable(true)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = layoutInflater.inflate(R.layout.dialog_select_photo, null);
        setContentView(view)
        binding = DialogSelectPhotoBinding.bind(view)

        setupUI()
    }


    private fun setupUI() {
        binding.takePhotoTextView.setOnClickListener {
            takePhoto()
            dismiss()
        }

        binding.selectFromGalleryTextView.setOnClickListener {
            selectGallery()
            dismiss()
        }

        //        binding.cancelDialogImageView.click { dismiss() }
    }
}