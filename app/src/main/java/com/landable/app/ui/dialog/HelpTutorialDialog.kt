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
import com.landable.app.databinding.HelpTutorialLayoutDialogBinding

class HelpTutorialDialog(
    val activity: Activity,
    val listener: SelectedListener,
) :
    Dialog(activity) {

    var binding: HelpTutorialLayoutDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.help_tutorial_layout_dialog,
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

        binding!!.llFAQ.setOnClickListener {
            listener.onClickButton("llFAQ")
            dismiss()
        }

        binding!!.llDemo.setOnClickListener {
            listener.onClickButton("llDemo")
            dismiss()
        }
        binding!!.llWhatsapp.setOnClickListener {
            listener.onClickButton("llWhatsapp")
            dismiss()
        }

        binding!!.llCall.setOnClickListener {
            listener.onClickButton("llCall")
            dismiss()
        }

    }

    interface SelectedListener {
        fun onClickButton(clicked: String)
    }

}