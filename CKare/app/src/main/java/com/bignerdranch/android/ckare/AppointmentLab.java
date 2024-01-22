package com.bignerdranch.android.ckare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.ckare.database.DayCareBaseHelper;
import com.bignerdranch.android.ckare.database.DayCareCursorWrapper;
import com.bignerdranch.android.ckare.database.DayCareDbSchema.AppointmentTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentLab {
    private static AppointmentLab sMAppointmentLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static AppointmentLab get(Context context) {
        if (sMAppointmentLab == null) {
            sMAppointmentLab = new AppointmentLab(context);
        }
        return sMAppointmentLab;
    }

    // populating the appointments in the recycler view fragment
    private AppointmentLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DayCareBaseHelper(mContext).getWritableDatabase();
    }

    public List<Appointment> getAppointments(UUID teacherId) {
        List<Appointment> appointments = new ArrayList<>();

        DayCareCursorWrapper cursor = queryAppointmentsByPaidAndDate(AppointmentTable.Cols.APPOINTMENT_TEACHER_UUID + " =?", new String[] {teacherId.toString()}, AppointmentTable.Cols.APPOINTMENT_SETTLED + " ASC" + " , " + AppointmentTable.Cols.APPOINTMENT_DATE + " DESC");

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                appointments.add(cursor.getAppointment());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return appointments;
    }


    public Appointment getAppointment(UUID appointmentId) {
        DayCareCursorWrapper cursor = queryAppointments(
                AppointmentTable.Cols.APPOINTMENT_UUID + " =?",
                new String[] { appointmentId.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getAppointment();
        }
        finally {
            cursor.close();
        }
    }

    public void addAppointment(Appointment appointment) {
        ContentValues values = getContentValues(appointment);

        mDatabase.insert(AppointmentTable.NAME, null, values);
    }

    public void updateAppointmentInformation(Appointment appointment) {
        String appointment_uuidString = appointment.getAppointmentId().toString();
        ContentValues values = getContentValues(appointment);

        mDatabase.update(AppointmentTable.NAME, values,
                AppointmentTable.Cols.APPOINTMENT_UUID + " =?",
                new String[] { appointment_uuidString });
    }

    public void deleteAppointment(Appointment appointment) {
        String appointment_uuidString = appointment.getAppointmentId().toString();
        ContentValues values = getContentValues(appointment);

        mDatabase.delete(AppointmentTable.NAME,
                AppointmentTable.Cols.APPOINTMENT_UUID + " =?",
                new String[] { appointment_uuidString });

    }

    public File getPhotoFile(Appointment appointment) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, appointment.getPhotoFilename());
    }

    private DayCareCursorWrapper queryAppointments(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                AppointmentTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, // having
                null // orderBy
        );

        return new DayCareCursorWrapper(cursor);
    }

    private DayCareCursorWrapper queryAppointmentsByPaidAndDate(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                AppointmentTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, // having
                orderBy // orderBy
        );

        return new DayCareCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Appointment appointment) {
        ContentValues values = new ContentValues();

        values.put(AppointmentTable.Cols.APPOINTMENT_UUID, appointment.getAppointmentId().toString());
        values.put(AppointmentTable.Cols.APPOINTMENT_TEACHER_UUID, appointment.getTeacherId().toString());
        values.put(AppointmentTable.Cols.APPOINTMENT_CHILD_NAME, appointment.getChildName());
        values.put(AppointmentTable.Cols.APPOINTMENT_PERSON_CONTACT_NUMBER, appointment.getPersonContactNumber());
        values.put(AppointmentTable.Cols.APPOINTMENT_DATE, appointment.getDate().getTime());
        values.put(AppointmentTable.Cols.APPOINTMENT_CHECK_IN_TIME, appointment.getCheckInTime().getTime());
        values.put(AppointmentTable.Cols.APPOINTMENT_SETTLED, appointment.isSettled());

        return values;
    }
}
