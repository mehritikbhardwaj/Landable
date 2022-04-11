package com.landable.app.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference

class FragmentHelper {

    /**
     * This method pops all the fragments which matches with the specified tag from back-stack and
     * replaces the given fragment.
     *
     * @param manager   Fragment Manager
     * @param container container view Id
     * @param fragment  Fragment
     * @param tag       optional tag name for the fragment
     */
    fun popBackStackAndReplace(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment?,
        tag: String?
    ) {
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        manager.beginTransaction().replace(container, fragment!!, tag).commit()
    }

    /**
     * Replace the fragment
     *
     * @param manager   Calling FragmentManager instance
     * @param container Container in which we are replacing fragment
     * @param fragment  Instance of fragment want to replace
     */
    fun replaceFragment(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment,
        tag: String?
    ) {
        try {
            manager.beginTransaction().replace(container, fragment, tag).commitAllowingStateLoss()
            ScreenTagManager.fragmentStack.add(WeakReference(fragment))
        } catch (e: Exception) {
            e.message
        }
    }

    /**
     * Add the fragment
     *
     * @param manager   Calling FragmentManager instance
     * @param container Container in which we are replacing fragment
     * @param fragment  Instance of fragment want to add
     */
    fun addFragment(manager: FragmentManager, container: Int, fragment: Fragment, tag: String?) {
        manager.beginTransaction().add(container, fragment, tag).commitAllowingStateLoss()
        ScreenTagManager.fragmentStack.add(WeakReference(fragment))
    }

    /**
     * Removes the fragment
     *
     * @param fragment Instance of fragment want to remove
     */
    fun removeFragment(manager: FragmentManager, fragment: Fragment?) {
        manager.beginTransaction().remove(fragment!!).commitAllowingStateLoss()
        ScreenTagManager.fragmentStack.remove(WeakReference(fragment))
    }

    /**
     * Add the fragment
     *
     * @param manager   Calling FragmentManager instance
     * @param container Container in which we are replacing fragment
     * @param fragment  Instance of fragment want to add
     */
    fun addFragment(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment,
        enterAnimation: Int,
        exitAnimation: Int,
        tag: String?
    ) {
        manager.beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation, enterAnimation, exitAnimation)
            .add(container, fragment, tag).commitAllowingStateLoss()
        ScreenTagManager.fragmentStack.add(WeakReference(fragment))
    }

    fun replaceFragmentAddToBackstack(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment,
        tag: String?
    ) {
        try {
            manager.beginTransaction().addToBackStack(tag).replace(container, fragment, tag)
                .commitAllowingStateLoss()
            ScreenTagManager.fragmentStack.add(WeakReference(fragment))
        } catch (e: Exception) {
            e.message
        }
    }

    fun replaceFragmentNoBackstack(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment,
        tag: String?
    ) {
        manager.beginTransaction().addToBackStack(null).replace(
            container, fragment, tag
        ).commit()
        ScreenTagManager.fragmentStack.remove(WeakReference(fragment))
    }

    fun replaceFragmentAddToBackstackNoLoss(
        manager: FragmentManager,
        container: Int,
        fragment: Fragment,
        tag: String?
    ) {
        manager.beginTransaction().addToBackStack(tag).replace(
            container, fragment, tag
        ).commit()
        ScreenTagManager.fragmentStack.add(WeakReference(fragment))
    }


    /**
     * Add the fragment and add it to local stack for stacking logic
     *
     * @param fm          Calling FragmentManager instance
     * @param container   Container in which we are replacing fragment
     * @param newFragment Instance of fragment want to add
     */
    fun addFragmentToBackStack(
        fm: FragmentManager, container: Int, newFragment: Fragment,
        tag: String?
    ) {
        fm.beginTransaction().add(container, newFragment, tag).addToBackStack(tag)
            .commitAllowingStateLoss()
        //fm.executePendingTransactions();
        ScreenTagManager.fragmentStack.add(WeakReference(newFragment))
    }

    fun addFragmentToBackStackNoLoss(
        fm: FragmentManager, container: Int, newFragment: Fragment,
        tag: String?
    ) {
        fm.beginTransaction().add(container, newFragment, tag).addToBackStack(tag).commit()
        //fm.executePendingTransactions();
        ScreenTagManager.fragmentStack.add(WeakReference(newFragment))
    }

    fun popBackStackImmediate(activity: FragmentActivity?) {
        if (activity != null && !activity.isFinishing) activity.supportFragmentManager.popBackStackImmediate()
    }
}