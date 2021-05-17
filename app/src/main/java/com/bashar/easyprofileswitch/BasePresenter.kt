package com.bashar.easyprofileswitch

/**
 * Created by Khairul Bashar on 5/8/2021.
 */

interface BasePresenter<T> {
    fun unregister()
    fun register(view: T)
}