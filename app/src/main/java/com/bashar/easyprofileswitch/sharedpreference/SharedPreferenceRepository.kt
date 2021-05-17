package com.bashar.easyprofileswitch.sharedpreference

/**
 * Created by Khairul Bashar on 5/8/2021.
 */

interface SharedPreferenceRepository {
    fun storeProfileSelection(pos: String)
    fun storeMinVolumeLevel(value: Int)
    fun storeNormalVolumeLevel(value: Int)



    fun getProfileSelection(): String
    fun getMinVolumeLevel(): Int
    fun getNormalVolumeLevel(): Int

}