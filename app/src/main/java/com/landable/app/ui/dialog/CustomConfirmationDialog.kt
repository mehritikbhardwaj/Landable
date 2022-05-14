package com.landable.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.landable.app.R
import com.landable.app.databinding.DialogCustomAlertBinding

class CustomConfirmationDialog(
    context: Context, private var title: String, private var message: String,
    private var action: String,
    private var isOkButtonEnable: Boolean, private var listener: ICustomConfirmationDialogListener
) : Dialog(context) {

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

        // hide Ok button
        if (isOkButtonEnable) {
            binding.llAlertButtonLayout.visibility = View.GONE
        } else {
            binding.llOkLayout.visibility = View.GONE
        }

        binding.tvNo.setOnClickListener {
            listener.onPressedCustomDialogButton("no", action)
            dismiss()
        }

        binding.tvYes.setOnClickListener {
            listener.onPressedCustomDialogButton("yes", action)
            dismiss()
        }

        binding.tvOk.setOnClickListener {
            listener.onPressedCustomDialogButton("ok", action)
            dismiss()
        }

        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
    }

    interface ICustomConfirmationDialogListener {
        fun onPressedCustomDialogButton(pressedButtonName: String?, action: String?)
    }
}
