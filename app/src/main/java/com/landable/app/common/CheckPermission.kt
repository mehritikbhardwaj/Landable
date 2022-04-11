package com.landable.app.common

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object CheckPermission {

    fun hasCameraPermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA)
    }

    fun hasGalleryPermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun hasWriteExternalStoragePermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}