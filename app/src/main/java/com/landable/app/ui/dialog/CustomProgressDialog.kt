package com.landable.app.ui.dialog

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.landable.app.R

class CustomProgressDialog(context: Context) : Dialog(context) {
    private var rotateProgressBarAnimation: RotateAnimation? = null
    private lateinit var imageView: ImageView
    private var tvPercentage: TextView ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress_layout)

        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window!!.setBackgroundDrawableResource(R.color.progressBarBgColor)
        imageView = findViewById(R.id.iv_rotate_progress_image)
        tvPercentage = findViewById(R.id.tvPercentage)

        startProgress(imageView)
    }

    private fun startProgress(image: ImageView) {
        if (rotateProgressBarAnimation == null)
            rotateProgressBarAnimation = RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )

        rotateProgressBarAnimation!!.repeatCount = ObjectAnimator.INFINITE
        rotateProgressBarAnimation!!.interpolator = LinearInterpolator()
        rotateProgressBarAnimation!!.duration = 1800

        image.startAnimation(rotateProgressBarAnimation)
    }

    fun updateProgress(progress: Int) {
        if(!tvPercentage!!.isVisible){
            tvPercentage!!.visibility = View.VISIBLE
        }
        tvPercentage!!.text = "${progress}%"
    }

    fun cancelProgress() {
        if (rotateProgressBarAnimation != null) {
            imageView.clearAnimation()
        }
        dismiss()
    }
}
