package com.bignerdranch.android.ckare;

import java.util.Date;
import java.util.UUID;

public class Appointment {
    private UUID mAppointmentId;
    private UUID mTeacherId;
    private String mChildName;
    private String mPersonContactNumber;
    private Date mDate;
    private Date mCheckInTime;
    private boolean mSettled;

    // constructor
    public Appointment() {
        mAppointmentId = UUID.randomUUID();

        mDate = new Date();
        mCheckInTime = mDate;
    }

    public Appointment(UUID appointmentId) {
        mAppointmentId = appointmentId;
    }

    // setter
    public void setAppointmentId(UUID appointmentId) {
        mAppointmentId = appointmentId;
    }

    public void setTeacherId(UUID teacherId) {
        mTeacherId = teacherId;
    }

    public void setChildName(String childName) {
        mChildName = childName;
    }

    public void setPersonContactNumber(String personContactNumber) { mPersonContactNumber = personContactNumber; }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setCheckInTime(Date checkInTime) {
        mCheckInTime = checkInTime;
    }

    public void setSettled(boolean settled) {
        mSettled = settled;
    }

    // getter
    public UUID getAppointmentId() {
        return mAppointmentId;
    }

    public UUID getTeacherId() {
        return mTeacherId;
    }

    public String getChildName() {
        return mChildName;
    }

    public String getPersonContactNumber() {
        return mPersonContactNumber;
    }

    public Date getDate() {
        return mDate;
    }

    public Date getCheckInTime() {
        return mCheckInTime;
    }

    public String getPhotoFilename() {
        return "IMG_" + getAppointmentId().toString() + ".jpg";
    }

    public boolean isSettled() {
        return mSettled;
    }
}
