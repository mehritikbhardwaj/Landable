package com.landable.app.common

import android.content.Context
import android.content.SharedPreferences

object AppInfo {

    private var sharedPreferencesUser: SharedPreferences? = null
    private var editorUser: SharedPreferences.Editor? = null
    private var context:Context? = null

    fun setContext(context: Context) {
        this.context = context
        if (sharedPreferencesUser == null) {
            sharedPreferencesUser = context.getSharedPreferences("AppInfoUser", Context.MODE_PRIVATE);
            editorUser = sharedPreferencesUser!!.edit();
        }
    }

    fun getContext():Context{
        return context!!

    }

    fun getFCMToken(): String {
        return sharedPreferencesUser?.getString("fcmToken", "")!!
    }

    fun setFCMToken(fcmToken: String) {
        editorUser?.putString("fcmToken", fcmToken)
        editorUser?.commit()
    }

    fun setUserId(userId: String) {
        editorUser?.putString("userId", userId)
        editorUser?.commit()
    }

    fun getUserId(): String {
        return sharedPreferencesUser?.getString("userId", "0")!!
    }

    fun setUserEmail(email: String) {
        editorUser?.putString("email", email)
        editorUser?.commit()
    }

    fun getUserMobile(): String {
        return sharedPreferencesUser?.getString("mobile", "")!!
    }


    fun setUserMobile(mobile: String) {
        editorUser?.putString("mobile", mobile)
        editorUser?.commit()
    }

    fun getUserEmail(): String {
        return sharedPreferencesUser?.getString("email", "")!!
    }



    fun getName(): String {
        return sharedPreferencesUser?.getString("name", "")!!
    }

    fun setUserPassword(userPassword: String) {
        editorUser?.putString("userPassword", userPassword)
        editorUser?.commit()
    }

    fun getUserPassword(): String {
        return sharedPreferencesUser?.getString("userPassword", "")!!
    }

    fun getUserName(): String {
        return sharedPreferencesUser?.getString("userName", "")!!
    }

    fun setName(name: String) {
        editorUser?.putString("name", name)
        editorUser?.commit()
    }

    fun setSCode(sCode: String) {
        editorUser?.putString("sCode", sCode)
        editorUser?.commit()
    }

    fun getSCode(): String {
        return sharedPreferencesUser?.getString("sCode", "0")!!
    }

    fun setLoginType(loginType: String) {
        editorUser?.putString("loginType", loginType)
        editorUser?.commit()
    }

    fun getLoginType(): String {
        return sharedPreferencesUser?.getString("loginType", "")!!
    }

    fun setUserImage(userImage: String) {
        editorUser?.putString("userImage", userImage)
        editorUser?.commit()
    }

    fun getUserImage(): String? {
        return sharedPreferencesUser?.getString("userImage", "")!!
    }

    fun setCustomerType(customerType: String) {
        editorUser?.putString("customerType", customerType)
        editorUser?.commit()
    }

    fun getCustomerType(): String? {
        return sharedPreferencesUser?.getString("customerType", "")!!
    }

    fun setNotificationID(notificationId: String) {
        editorUser?.putString("notificationId", notificationId)
        editorUser?.commit()
    }

    fun getNotificationID(): String? {
        return sharedPreferencesUser?.getString("notificationId", "")!!
    }

    fun clearUserData(){
       setUserId("0")
        setCustomerType("")
        setUserImage("")
        setName("")
        setSCode("")
        setUserEmail("")
        setLoginType("")
        setUserPassword("")
    }
}