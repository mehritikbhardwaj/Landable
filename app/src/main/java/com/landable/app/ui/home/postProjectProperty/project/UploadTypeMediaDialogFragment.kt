package com.landable.app.ui.home.postProjectProperty.project

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.landable.app.R
import com.landable.app.databinding.UploadTypeDialogBinding
import com.landable.app.ui.dialog.CustomProgressDialog

class UploadTypeMediaDialogFragment(
    val listener: UploadTypeListener,

    ) : DialogFragment() {

    private var binding: UploadTypeDialogBinding? = null
    private var progressDialog: CustomProgressDialog? = null

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
        binding = DataBindingUtil.inflate(inflater, R.layout.upload_type_dialog, container, false)

        binding!!.llUploadImage.setOnClickListener {
            listener.onClickButtonForUploadType("UploadImage")
            dismiss()
        }

        binding!!.llUploadVideo.setOnClickListener {
            listener.onClickButtonForUploadType("UploadVideo")
            dismiss()
        }

        binding!!.llUploadDocument.setOnClickListener {
            listener.onClickButtonForUploadType("UploadDocument")
            dismiss()
        }



        return binding!!.root
    }

    interface UploadTypeListener {
        fun onClickButtonForUploadType(typeOfUpload: String)
    }
}