package com.bashar.easyprofileswitch.profilerepo

import com.bashar.easyprofileswitch.models.Profile

interface ProfileRepository {
    fun createProfile(profile: Profile)
    fun deleteProfile(id: Int)
    fun updateProfile(profile: Profile)
    fun getProfileList(): ArrayList<Profile>
    fun getProfile(id: Int): Profile
    fun updateProfileSchedule(id: Int, position: Int, select: String)
    fun deleteTable()
    fun isTableEmpty(): Boolean
}