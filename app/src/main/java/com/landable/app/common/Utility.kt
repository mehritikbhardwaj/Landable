package com.landable.app.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bumptech.glide.Glide
import com.landable.app.ui.dialog.CustomAlertDialog
import java.text.DateFormat
import java.text.ParseException
import java.util.*

object Utility {

     fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null)
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun hideKeyboardOutsideClick(activity: Activity, view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard(activity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i) as View
                hideKeyboardOutsideClick(activity, innerView)
            }
        }
    }


    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        return if (calendar[Calendar.DATE] < 10
        ) "0" + calendar[Calendar.DATE] else calendar[Calendar.DATE].toString() + " " + calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        ) +
                " " + calendar[Calendar.YEAR]
            .toString()
    }

    fun formattedDate(
        dateFormatter: DateFormat,
        dateString: String?
    ): Date? {
        try {
            return dateFormatter.parse(dateString)
        } catch (e: ParseException) {
            e.message
        }
        return null
    }

    fun formattedDateString(
        dateFormatter: DateFormat,
        date: Date?
    ): String? {
        try {
            return dateFormatter.format(date)
        } catch (e: Exception) {
            e.message
        }
        return null
    }

    fun formattedDateString(
        responseDateFormatter: DateFormat,
        displayDateFormatter: DateFormat,
        dateString: String?
    ): String? {
        try {
            return displayDateFormatter.format(responseDateFormatter.parse(dateString))
        } catch (e: ParseException) {
            e.message
        }
        return ""
    }


    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun isInternetConnected(context: Context): Boolean {
        if (context != null) {
            val message = "Please check your internet connection"
            val cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (cm.activeNetworkInfo != null) {
                val isConnected: Boolean = cm.activeNetworkInfo!!.isConnected
                return if (!isConnected) {
                    CustomAlertDialog(context, LandableConstants.noInternetErrorTitle, LandableConstants.noInternetErrorMessage).show()
                    false
                } else true
            } else {
                CustomAlertDialog(context, LandableConstants.noInternetErrorTitle, LandableConstants.noInternetErrorMessage).show()
                false
            }
        } else return true
    }
}