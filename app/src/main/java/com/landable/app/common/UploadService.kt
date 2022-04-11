package com.landable.app.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.landable.app.R
import java.io.File

class UploadService : Service(), IUploadImageListener{
    private var uploadFileArrayList = ArrayList<String>()
    private val TAG: String = UploadService::class.java.simpleName
    private val CHANNEL_ID = "FILE_UPLOAD"
    private val NOTIFICATION_ID = 111
    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var uploadedImageCount = 0
    private var _id: Int = 0
    private var propertyId: String = ""
    private var type:String = ""

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        uploadFileArrayList = intent.getStringArrayListExtra("photoList") as ArrayList<String>
        _id = intent.getIntExtra("_id",0)
        propertyId = intent.getStringExtra("propertyId")!!
        type = intent.getStringExtra("type")!!

        if (uploadFileArrayList.size > 0) {
            if (Build.VERSION.SDK_INT >= 19) {
                uploadedImageCount = 1
                uploadImages(Uri.parse(uploadFileArrayList[uploadedImageCount - 1]))
                showUploadNotification()
            }
        }

        return START_NOT_STICKY
    }

    private fun showUploadNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "TestChannel", importance)
            channel.description = "TestChannelDesc"
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        } else {
            notificationManager = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
                getSystemService(NotificationManager::class.java)
            else getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        var messageText = ""
        if (uploadFileArrayList.size > 0) {
            messageText = "$uploadedImageCount of ${uploadFileArrayList.size} uploading"
        }
        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder!!.setContentTitle("Upload...")
            .setContentText(messageText)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(100, 0, true)
            .setAutoCancel(false)
            .priority = NotificationCompat.PRIORITY_DEFAULT

        startForeground(NOTIFICATION_ID, notificationBuilder!!.build())
    }

    private fun updateNotification(percent: Int) {
        try {
            Log.i(
                TAG,
                "uploaded: $uploadedImageCount of ${uploadFileArrayList.size} ... $percent %"
            )
            val messageText =
                "$uploadedImageCount of ${uploadFileArrayList.size} uploaded $percent%"
            notificationBuilder!!.setContentText(messageText)
                .setContentTitle("Upload...")
                .setOngoing(true)
                .setSilent(true)
                .setContentInfo("$percent %")
                .setProgress(100, percent, false)
                .priority = NotificationCompat.PRIORITY_DEFAULT

            if (notificationManager != null) {
                notificationManager!!.notify(NOTIFICATION_ID, notificationBuilder!!.build())
            }
        } catch (e: Exception) {
            Log.e("Error...Notification.", e.message + ".....")
            e.printStackTrace()
        }
    }


    private fun deleteNotification() {
        notificationManager!!.cancel(NOTIFICATION_ID)
        notificationBuilder = null
    }

    override fun onProgressUpdate(percentage: Int) {
        updateNotification(percentage)
    }

    override fun onError(errorMessage: String) {
        deleteNotification()
    }

    override fun onFinish(response: String) {
        Log.i(TAG, "completed : $uploadedImageCount of ${uploadFileArrayList.size}")
        if (uploadedImageCount < uploadFileArrayList.size) {
            uploadedImageCount += 1
            showUploadNotification()
            if (Build.VERSION.SDK_INT >= 19)
                uploadImages(Uri.parse(uploadFileArrayList[uploadedImageCount - 1]))
        } else {
            //refresh photo library page if user exist in same page.
            try {
                Toast.makeText(this, "Images are uploaded successfully.", Toast.LENGTH_LONG).show()
               /* if (ScreenTagManager.fragmentStack.last != null) {
                    if (ScreenTagManager.fragmentStack.last.get()!!.tag.toString()
                            .contains("PhotoLibraryFragment")
                    ) {
                        val fragment: PhotoLibraryFragment =
                            ScreenTagManager.fragmentStack.last.get()!! as PhotoLibraryFragment
                        fragment.updatePhotoLibrary()
                    }
                }*/
            } catch (e: Exception) {
            }

            deleteNotification()
            onDestroy()
        }
    }

    override fun uploadStart() {
        //progressDialog!!.updateProgress(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadImages(uri: Uri) {
        Log.i(TAG, "Start uploading : $uploadedImageCount of ${uploadFileArrayList.size}")
        val filePath = if ("content" == uri.scheme) {
            Log.e("contains", "getUri.getScheme()")
            ClsGlobal.getPathFromUri(this, uri)
        } else {
            uri.path
        }

        UploadImage(
            "imageUpload", File(filePath!!), this@UploadService,"",_id,propertyId,"Image","","","","" )
    }


}


