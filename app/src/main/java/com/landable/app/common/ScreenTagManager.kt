package com.landable.app.common

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import java.util.*

object ScreenTagManager {
    var fragmentStack = LinkedList<WeakReference<Fragment>>()

    private var previousScreen: String? = null
    private var nextScreen: String? = null

    fun getPreviousScreen(): String? {
        return previousScreen
    }

    fun getNextScreen(): String? {
        return nextScreen
    }

    fun setPreviousScreen(previousScreen: String?) {
        this.previousScreen = previousScreen
    }

    fun setNextScreen(nextScreen: String?) {
        this.nextScreen = nextScreen
    }
}