package com.landable.app.common

import android.os.CountDownTimer
import android.widget.TextView

class MyCountDownTimer(private var millisInFuture: Long, countDownInterval: Long, private var timerTextView: TextView? = null, private var listener: ICompleteTimerListener? = null): CountDownTimer(millisInFuture, countDownInterval) {

    var tvTimer: TextView? = null
    override fun onTick(millisUntilFinished: Long) {
        val time = millisInFuture - millisUntilFinished
        timerTextView!!.text = "${millisUntilFinished/1000}"
    }

    override fun onFinish() {
        timerTextView!!.text = "0"
        listener!!.onCompleteTimer("CompleteTimer")
    }

    interface ICompleteTimerListener {
        fun onCompleteTimer(action: String)
    }
}