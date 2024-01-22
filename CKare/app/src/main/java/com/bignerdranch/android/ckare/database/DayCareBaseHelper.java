package com.bignerdranch.android.ckare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.ckare.database.DayCareDbSchema.AppointmentTable;
import com.bignerdranch.android.ckare.database.DayCareDbSchema.TeacherTable;

public class DayCareBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "dayCareBase.db";

    public DayCareBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TeacherTable.NAME + "(" +
                "_id integer PRIMARY KEY AUTOINCREMENT, " +
                TeacherTable.Cols.TEACHER_UUID + ", " +
                TeacherTable.Cols.TEACHER_NAME + ", " +
                TeacherTable.Cols.TEACHER_GENDER + ", " +
                TeacherTable.Cols.TEACHER_SUBJECT_TEACHES + ", " +
                TeacherTable.Cols.TEACHER_PAYMENT_PER_DAY + ", " +
                TeacherTable.Cols.TEACHER_IS_ACTIVE +
                ")"
        );
        db.execSQL("CREATE TABLE " + AppointmentTable.NAME + "(" +
                "_id integer PRIMARY KEY AUTOINCREMENT, " +
                AppointmentTable.Cols.APPOINTMENT_UUID + ", " +
                AppointmentTable.Cols.APPOINTMENT_TEACHER_UUID + ", " +
                AppointmentTable.Cols.APPOINTMENT_CHILD_NAME + ", " +
                AppointmentTable.Cols.APPOINTMENT_PERSON_CONTACT_NUMBER + ", " +
                AppointmentTable.Cols.APPOINTMENT_DATE + ", " +
                AppointmentTable.Cols.APPOINTMENT_CHECK_IN_TIME + ", " +
                AppointmentTable.Cols.APPOINTMENT_SETTLED + ", " +
                "FOREIGN KEY(" + AppointmentTable.Cols.APPOINTMENT_TEACHER_UUID + ") REFERENCES " + TeacherTable.NAME + "(" + TeacherTable.Cols.TEACHER_UUID + ") ON DELETE CASCADE ON UPDATE CASCADE" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
