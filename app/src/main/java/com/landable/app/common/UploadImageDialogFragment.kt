package com.landable.app.common

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.landable.app.R
import com.landable.app.databinding.DialogAddPicsBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class UploadImageDialogFragment(
    val listener: IUploadImageListener,
    val action: String,
) : DialogFragment(), EasyPermissions.PermissionCallbacks {

    private var binding: DialogAddPicsBinding? = null
    private var imageUri: Uri? = null

    private var isClickedCameraButton: Boolean = false
    private var isClickedGalleryButton: Boolean = false

    override fun onStart() {
        super.onStart()

        if (dialog!!.window != null) {
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog!!.window!!.setGravity(Gravity.CENTER)
            dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_pics, container, false)


        binding!!.closeButton.setOnClickListener {
            dismiss()
        }

        binding!!.llTakePicture.setOnClickListener {
            isClickedCameraButton = true
            checkCameraPermission()
        }

        binding!!.llChoosePicture.setOnClickListener {
            isClickedCameraButton = false
            isClickedGalleryButton = true
            checkGalleryPermission()
        }
        return binding!!.root
    }


    private fun checkCameraPermission() {
        if (CheckPermission.hasCameraPermission(requireContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                openCamera()
                isClickedCameraButton = false
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CheckPermission.hasWriteExternalStoragePermission(requireContext())) {
                        openCamera()
                        isClickedCameraButton = false
                    } else {
                        EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.writeFilePermissionString),
                            LandableConstants.writeExternalStorageRequestCode,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
            }
        } else {

            // Ask for one permission
            EasyPermissions.requestPermissions(
                this@UploadImageDialogFragment,
                getString(R.string.cameraPermissionString),
                LandableConstants.cameraRequestCode,
                Manifest.permission.CAMERA
            )
        }
    }

    private fun checkGalleryPermission() {
        if (CheckPermission.hasGalleryPermission(requireContext())) {
            // open gallery for select picture
            openGallery()
            isClickedGalleryButton = false
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.galleryPermissionString),
                LandableConstants.galleryRequestCode,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val arrayList = ArrayList<String>()
            if (requestCode == LandableConstants.galleryRequestCode) {
                if (action == LandableConstants.actionForMultipleSelection) {
                    if (data!!.clipData != null) {
                        val mClipData = data.clipData
                        for (i in 0 until mClipData!!.itemCount) {
                            arrayList.add(mClipData.getItemAt(i).uri.toString())
                        }
                    } else {
                        imageUri = data.data as Uri
                        arrayList.add(imageUri.toString())
                    }
                } else {
                    imageUri = data!!.data as Uri
                    arrayList.add(imageUri.toString())
                }
                listener.onUploadImageDialogButtonClick(true, arrayList, action)
                dismiss()
            } else {
                arrayList.add(imageUri.toString())
                listener.onUploadImageDialogButtonClick(false, arrayList, action)
                dismiss()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (isClickedCameraButton) {
            if (LandableConstants.cameraRequestCode == requestCode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // ask access all file permission
                    openCamera()
                    isClickedCameraButton = false
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CheckPermission.hasWriteExternalStoragePermission(requireContext())) {
                        openCamera()

                        isClickedCameraButton = false
                    } else {
                        // ask write permission
                        EasyPermissions.requestPermissions(
                            this@UploadImageDialogFragment,
                            getString(R.string.writeFilePermissionString),
                            LandableConstants.writeExternalStorageRequestCode,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    }
                } else {
                    openCamera()
                    isClickedCameraButton = false
                }
            } else if (LandableConstants.writeExternalStorageRequestCode == requestCode) {
                openCamera()
                isClickedCameraButton = false
            }
        } else {
            if (LandableConstants.galleryRequestCode == requestCode) {
                openGallery()
                isClickedGalleryButton = false
            }
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) as Uri
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, LandableConstants.cameraRequestCode)
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        //  val photoPickerIntent = Intent(Intent.ACTION_PICK)

        photoPickerIntent.type = "image/*"
        if (action == LandableConstants.actionForMultipleSelection) {
            if (Build.VERSION.SDK_INT >= 18)
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(photoPickerIntent, LandableConstants.galleryRequestCode)
    }

    interface IUploadImageListener : com.landable.app.common.IUploadImageListener {
        fun onUploadImageDialogButtonClick(
            isClickedCameraButton: Boolean,
            selectedPhotoList: ArrayList<String>,
            action: String
        )
    }

    override fun onResume() {
        super.onResume()
        if (isClickedCameraButton) {
            if (CheckPermission.hasCameraPermission(requireContext())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    openCamera()
                    isClickedCameraButton = false
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (CheckPermission.hasWriteExternalStoragePermission(requireContext())) {
                            openCamera()
                            isClickedCameraButton = false
                        } else {
                            EasyPermissions.requestPermissions(
                                this,
                                getString(R.string.writeFilePermissionString),
                                LandableConstants.writeExternalStorageRequestCode,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        }
                    }
                }
            }
        } else if (isClickedGalleryButton) {
            if (CheckPermission.hasGalleryPermission(requireContext())) {
                // open gallery for select picture
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    openGallery()
                    isClickedGalleryButton = false
                } else {
                    openGallery()
                    isClickedGalleryButton = false
                }
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.galleryPermissionString),
                    LandableConstants.galleryRequestCode,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }


}