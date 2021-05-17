package com.bashar.easyprofileswitch.models

class Profile(private val name: String,
              private val ringMode: String, private val  ringVol: String, private val alarmVol: String, private val otherVol: String,
              private val wifi: String, private val mobileData: String,
              private val image: Int,
              private val profileId: Int = 1,
              private val delaySelect: String = "no", private val delay:String = "Set", private val afterTimer: String = "1",
              private val start1: String = "Set", private val start2: String = "Set", private val start3: String = "Set", private val start4: String = "Set", private val start5: String = "Set",
              private val start1Select: String = "no", private val start2Select: String = "no", private val start3Select: String = "no", private val start4Select: String = "no", private val start5Select: String = "no") {

    fun getName(): String {
        return name;
    }

    fun getRingMode(): String {
        return ringMode;
    }

    fun getRingVol(): String {
        return ringVol;
    }

    fun getAlarmVol(): String {
        return alarmVol;
    }

    fun getOtherVol(): String {
        return otherVol;
    }

    fun getWifi(): String {
        return wifi;
    }

    fun getMobileData(): String {
        return mobileData;
    }

    fun getStart1(): String {
        return start1;
    }

    fun getStart2(): String {
        return start2;
    }

    fun getStart3(): String {
        return start3;
    }

    fun getStart4(): String {
        return start4;
    }

    fun getStart5(): String {
        return start5;
    }

    fun getStart1Select(): String {
        return start1Select
    }

    fun getStart2Select(): String {
        return start2Select
    }

    fun getStart3Select(): String {
        return start3Select
    }

    fun getStart4Select(): String {
        return start4Select
    }

    fun getStart5Select(): String {
        return start5Select
    }

    fun getDelay(): String {
        return delay
    }

    fun getDelaySelect(): String {
        return delaySelect
    }

    fun getAfterTimer(): String {
        return afterTimer
    }

    fun getImage() : Int {
        return image
    }

    fun getProfileId(): Int{
        return profileId
    }
}