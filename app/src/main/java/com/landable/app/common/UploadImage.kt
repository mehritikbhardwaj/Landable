package com.landable.app.common

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.landable.app.data.repositories.RegisterRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class UploadImage(
    isComingFor: String,
    file: File,
    listener: IUploadImageListener, name: String,
    id: Int,
    propertyId: String,
    type: String,
    link: String,
    utype: String,
    usize: String,
    uprice: String,
) {
    private var filesize = 0L

    init {
        filesize = file.length()
        Log.e("--URL--", "filesize: $filesize")

        val mFile = PRRequestBody(file, listener)
        val imageFile = MultipartBody.Part.createFormData("files", file.name, mFile)

        if (isComingFor == "profileUpdate") {
            RegisterRepository().post_updateuserimage(
                listener, id, imageFile
            )
        } else if (isComingFor == "imageUpload") {
            RegisterRepository().postPropertyImage(listener, id, propertyId, "Image", "", imageFile)
        } else if (isComingFor == "AddConfiguration") {
            RegisterRepository().post_AddUpdateProjectConfiguration(
                listener,
                id,
                propertyId,
                utype,
                usize,
                uprice,
                imageFile
            )
        } else if (isComingFor == "ProjectImageUpload") {
            RegisterRepository().post_AddProjectMedia(
                listener,
                id,
                propertyId,
                type,
                imageFile,
                link
            )
        } else if (isComingFor == "AddSupergroupMedia") {
            RegisterRepository().post_AddSupergroupMedia(listener, id, type, imageFile, link)
        }
    }
}

class PRRequestBody(
    private val mFile: File,
    listener: IUploadImageListener
) : RequestBody() {
    val mListener: IUploadImageListener = listener
    private val defaultBufferSize = 2048
    override fun contentType(): MediaType? {
        //return MediaType.parse("multipart/form-data");
        return MediaType.parse("image/png")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(defaultBufferSize)
        val inputStream = FileInputStream(mFile)
        var uploaded: Long = 0

        inputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                handler.post(ProgressUpdater(uploaded, fileLength, mListener))
            }
        }
    }
}

private class ProgressUpdater(
    private val mUploaded: Long,
    private val mTotal: Long,
    private val listener: IUploadImageListener
) :
    Runnable {
    override fun run() {
        try {
            val progress = (100 * mUploaded / mTotal).toInt()
            listener.onProgressUpdate(progress)
        } catch (e: ArithmeticException) {
            e.message?.let { listener.onError(it) }
            e.printStackTrace()
        }
    }
}