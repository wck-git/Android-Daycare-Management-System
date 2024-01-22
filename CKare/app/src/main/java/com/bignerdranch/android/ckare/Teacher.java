package com.bignerdranch.android.ckare;

import java.util.UUID;

public class Teacher {
    private UUID mTeacherId;
    private String mTeacherName;
    private String mGender;
    private String mSubjectTeaches;
    private double mPaymentPerDay;
    private boolean mActive;

    public Teacher() {
        mTeacherId = UUID.randomUUID();
    }

    public Teacher(UUID teacherId) {
        mTeacherId = teacherId;
    }

    // setter
    public void setTeacherId(UUID teacherId) { mTeacherId = teacherId; }

    public void setTeacherName(String teacherName) { mTeacherName = teacherName; }

    public void setGender(String gender) { mGender = gender; }

    public void setSubjectTeaches(String subjectTeaches) { mSubjectTeaches = subjectTeaches; }

    public void setPaymentPerDay(double paymentPerDay) { mPaymentPerDay = paymentPerDay; }

    public void setActive(boolean active) { mActive = active; }

    // getter
    public UUID getTeacherId() { return mTeacherId; }

    public String getTeacherName() { return mTeacherName; }

    public String getGender() { return mGender; }

    public String getSubjectTeaches() { return mSubjectTeaches; }

    public double getPaymentPerDay() { return mPaymentPerDay; }

    public boolean isActive() { return mActive; }

}
