package com.bignerdranch.android.ckare.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.ckare.Appointment;
import com.bignerdranch.android.ckare.Teacher;
import com.bignerdranch.android.ckare.database.DayCareDbSchema.AppointmentTable;
import com.bignerdranch.android.ckare.database.DayCareDbSchema.TeacherTable;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class DayCareCursorWrapper extends CursorWrapper {
    public DayCareCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Teacher getTeacher() {
        // getting the values from the TEACHER table
        String teacher_uuidString = getString(getColumnIndex(TeacherTable.Cols.TEACHER_UUID));
        String teacher_name = getString(getColumnIndex(TeacherTable.Cols.TEACHER_NAME));
        String teacher_gender = getString(getColumnIndex(TeacherTable.Cols.TEACHER_GENDER));
        String teacher_subject_teaches = getString(getColumnIndex(TeacherTable.Cols.TEACHER_SUBJECT_TEACHES));
        double teacher_payment_per_day = getDouble(getColumnIndex(TeacherTable.Cols.TEACHER_PAYMENT_PER_DAY));
        int teacher_is_active = getInt(getColumnIndex(TeacherTable.Cols.TEACHER_IS_ACTIVE));

        // setting the values
        Teacher teacher = new Teacher(UUID.fromString(teacher_uuidString));
        teacher.setTeacherName(teacher_name);
        teacher.setGender(teacher_gender);
        teacher.setSubjectTeaches(teacher_subject_teaches);
        teacher.setPaymentPerDay(teacher_payment_per_day);
        teacher.setActive(teacher_is_active != 0);

        return teacher;
    }

    public Appointment getAppointment() {
        // getting the values from the APPOINTMENT table
        String appointment_uuidString = getString(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_UUID));
        String appointment_teacher_uuidString = getString(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_TEACHER_UUID));
        String appointment_child_name = getString(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_CHILD_NAME));
        String appointment_person_contact_number = getString(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_PERSON_CONTACT_NUMBER));
        long appointment_date = getLong(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_DATE));
        long appointment_check_in_time = getLong(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_CHECK_IN_TIME));
        int appointment_settled = getInt(getColumnIndex(AppointmentTable.Cols.APPOINTMENT_SETTLED));


        // setting the values
        Appointment appointment = new Appointment(UUID.fromString(appointment_uuidString));
        appointment.setTeacherId(UUID.fromString(appointment_teacher_uuidString));
        appointment.setChildName(appointment_child_name);
        appointment.setPersonContactNumber(appointment_person_contact_number);
        appointment.setDate(new Date(appointment_date));
        appointment.setCheckInTime(new Time(appointment_check_in_time));
        appointment.setSettled(appointment_settled != 0);

        return appointment;
    }

}
