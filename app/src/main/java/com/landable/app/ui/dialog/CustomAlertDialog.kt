package com.landable.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.landable.app.R
import com.landable.app.databinding.DialogCustomAlertBinding

class CustomAlertDialog(context: Context, private var title: String, private var message: String) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: DialogCustomAlertBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_custom_alert,
            null,
            false
        )
        setContentView(binding.root)

        if (window != null) {
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window!!.setGravity(Gravity.CENTER)
            window!!.setBackgroundDrawableResource(R.color.transparent)
        }

        if (title.isNotEmpty()) {
            binding.tvTitle.text = title
        }
        binding.tvMessage.text = message

        binding.llAlertButtonLayout.visibility = View.GONE

        binding.tvOk.setOnClickListener { dismiss() }

        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
    }
}
