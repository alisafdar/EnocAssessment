package com.alisafdar.enocassessment.data.managers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Images.Media
import androidx.core.content.FileProvider
import com.alisafdar.enocassessment.data.constants.DIRECTORY_NAME
import com.alisafdar.enocassessment.data.constants.REQUEST_CODE_IMAGE_FROM_GALLERY
import com.alisafdar.enocassessment.data.constants.REQUEST_CODE_TAKE_PHOTO
import com.alisafdar.enocassessment.data.constants.REQUEST_CODE_VIDEO_FROM_GALLERY
import com.alisafdar.enocassessment.ui.dialogs.PhotoSelectDialog
import com.alisafdar.enocassessment.utils.getFileDataFromURI
import com.alisafdar.enocassessment.utils.isDeviceSupportCamera
import com.grumpyshoe.module.permissionmanager.impl.PermissionManagerImpl
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoManager(act: Activity) {
	var activity = act
	private var fileUri: Uri? = Uri.parse("")

	fun showPhotoSelectDialog() {
		PhotoSelectDialog(activity, takePhoto = { photoPressed() }, selectGallery = { openImageGallery() }).show()
	}

	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, onResult: (contentUri: Uri?, fileUri: Uri?) -> Unit) {
		if (resultCode == Activity.RESULT_OK) {
			when (requestCode) {
				REQUEST_CODE_IMAGE_FROM_GALLERY -> if (data != null) {
					val contentUri = data.data
					fileUri = Uri.parse(getFileDataFromURI(activity, contentUri, Media.DATA))
					activity.contentResolver.takePersistableUriPermission(contentUri !!, Intent.FLAG_GRANT_READ_URI_PERMISSION)

					onResult(contentUri, fileUri)
				}

				REQUEST_CODE_TAKE_PHOTO -> {
					val file = File(fileUri.toString())
					val contentUri = FileProvider.getUriForFile(activity, activity.packageName + ".provider", file)
					onResult(contentUri, fileUri)
				}
			}
		}
	}

	private fun getPhotoGalleryIntent(): Intent {
		val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
		intent.type = "image/*"
		intent.addCategory(Intent.CATEGORY_OPENABLE)
		intent.type = "image/*"
		intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1048576L)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		//	intent.addCategory(Intent.CATEGORY_OPENABLE)
		//	intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
		return intent
	}

	private fun getVideoGalleryIntent(): Intent {
		val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
		intent.type = "video/*"
		intent.addCategory(Intent.CATEGORY_OPENABLE)
		intent.type = "video/*"
		intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/*"))
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1048576L)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
		//	intent.addCategory(Intent.CATEGORY_OPENABLE)
		//	intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
		return intent
	}

	private fun getCameraIntent(onCameraIntent: (Boolean, Intent, Uri) -> Unit) {
		val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
		var fileUri: Uri
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			fileUri = getOutputMediaFileUri(FileColumns.MEDIA_TYPE_IMAGE)
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
		} else {
			val file = File(getOutputMediaFileUri(FileColumns.MEDIA_TYPE_IMAGE).path !!)
			fileUri = Uri.parse(file.path)

			val uri = FileProvider.getUriForFile(activity, activity.packageName + ".provider", file)
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
		}
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		if (intent.resolveActivity(activity.packageManager) != null) {
			onCameraIntent(true, intent, fileUri)
		} else {
			onCameraIntent(false, intent, fileUri)
		}
	}

	private fun getOutputMediaFileUri(type: Int): Uri {
		return Uri.fromFile(getOutputMediaFile(type))
	}

	private fun getOutputMediaFile(type: Int): File? {		// External sdcard location
		val mediaStorageDir = File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null
			}
		}

		// Create a media file name
		val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

		return when (type) {
			FileColumns.MEDIA_TYPE_IMAGE -> {
				File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
			}
			FileColumns.MEDIA_TYPE_VIDEO -> {
				File(mediaStorageDir.path + File.separator + "VID_" + timeStamp + ".mp4")
			}
			else -> {
				return null
			}
		}
	}

	fun photoPressed() {
		if (giveCameraPermission(activity, false)) {
			takePhotoPressed()
		}
	}

	private fun openImageGallery() {
		if (giveCameraPermission(activity, true)) {
			selectFromImageGallery()
		}
	}

	fun openVideoGallery() {
		if (giveCameraPermission(activity, true)) {
			selectFromVideoGallery()
		}
	}

	private fun giveCameraPermission(activity: Activity, isGallery: Boolean): Boolean {
		PermissionManagerImpl.checkPermissions(activity = activity, permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), onPermissionResult = { _ ->
			if (isGallery) {
				selectFromImageGallery()
			} else {
				takePhotoPressed()
			}
		}, permissionRequestPreExecuteExplanation = null, permissionRequestRetryExplanation = null, requestCode = 123)
		return false
	}

	private fun takePhotoPressed() {
		if (isDeviceSupportCamera(activity)) {
			getCameraIntent { intentResolved, intent, uri ->
				if (intentResolved) {
					fileUri = uri
					activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
				}
			}
		}
	}

	private fun selectFromImageGallery() {
		activity.startActivityForResult(getPhotoGalleryIntent(), REQUEST_CODE_IMAGE_FROM_GALLERY)
	}

	private fun selectFromVideoGallery() {
		activity.startActivityForResult(getVideoGalleryIntent(), REQUEST_CODE_VIDEO_FROM_GALLERY)
	}
}