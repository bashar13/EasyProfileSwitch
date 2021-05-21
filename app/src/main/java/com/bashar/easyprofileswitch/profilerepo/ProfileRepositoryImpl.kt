package com.bashar.easyprofileswitch.profilerepo

import android.database.Cursor
import com.bashar.easyprofileswitch.database.DBhelper
import com.bashar.easyprofileswitch.database.SQLController
import com.bashar.easyprofileswitch.models.Profile
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val db: SQLController): ProfileRepository {

    override fun createProfile(profile: Profile) {
        db.open()
        db.insertData(profile.getName(),
                profile.getRingMode(), profile.getRingVol(), profile.getAlarmVol(), profile.getOtherVol(),
                profile.getWifi(), profile.getMobileData(),
                profile.getImage(),
                profile.getStart1(), profile.getStart2(), profile.getStart3(), profile.getStart4(), profile.getStart5(),
                profile.getStart1Select(), profile.getStart2Select(), profile.getStart3Select(), profile.getStart4Select(), profile.getStart5Select(),
                profile.getDelay(), profile.getDelaySelect(), profile.getAfterTimer())

        db.close()
    }

    override fun deleteProfile(id: Int) {
        db.open()
        db.deleteData(id)
        db.close()
    }

    override fun updateProfile(profile: Profile) {
        TODO("Not yet implemented")
    }

    override fun getProfileList(): ArrayList<Profile> {
        db.open()
        val profileList = ArrayList<Profile>()
        val cursor: Cursor = db.readData()
        //val count = cursor.count
        val check = cursor.moveToFirst()
        if (check) {
            do {
                profileList.add(Profile(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8).toInt(),
                        cursor.getString(0).toInt()))
            } while(cursor.moveToNext())
        }
        db.close()
        return profileList
    }

    override fun getProfile(id: Int): Profile {
        db.open()
        var cursor = db.readSpecificData(DBhelper.TABLE_PROFILE, id)
        val profile = Profile(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8).toInt(),
                cursor.getString(0).toInt(),
                cursor.getString(9),
                cursor.getString(10))

        return profile
    }

    override fun deleteTable() {
        db.open()
        db.deleteTable()
        db.close()
    }

    override fun isTableEmpty(): Boolean {
        db.open()
        val c: Cursor = db.readData()
        val checkRec = c.moveToFirst()
        db.close()
        return !checkRec
    }


}