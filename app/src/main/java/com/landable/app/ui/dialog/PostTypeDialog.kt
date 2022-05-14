package com.landable.app.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.landable.app.R
import com.landable.app.databinding.DialogSelectUploadOptionBinding

class PostTypeDialog(
    val activity: Activity,
    val listener: UploadTypeListener,
) :
    Dialog(activity) {

    var binding: DialogSelectUploadOptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_select_upload_option,
            null,
            false
        )
        setContentView(binding!!.root)

        if (window != null) {
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window!!.setGravity(Gravity.CENTER)
            window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        binding!!.llPostProperty.setOnClickListener {
            listener.onClickButtonForUploadType("llPostProperty")
            dismiss()
        }
        binding!!.llPostSupergroup.setOnClickListener {
            listener.onClickButtonForUploadType("llPostSupergroup")
            dismiss()
        }


    }

    interface UploadTypeListener {
        fun onClickButtonForUploadType(typeOfUpload: String)
    }

}