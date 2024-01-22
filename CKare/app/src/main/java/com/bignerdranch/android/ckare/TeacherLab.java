package com.bignerdranch.android.ckare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.ckare.database.DayCareBaseHelper;
import com.bignerdranch.android.ckare.database.DayCareCursorWrapper;
import com.bignerdranch.android.ckare.database.DayCareDbSchema.TeacherTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeacherLab {
    private static TeacherLab sMTeacherLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TeacherLab get(Context context) {
        if (sMTeacherLab == null) {
            sMTeacherLab = new TeacherLab(context);
        }
        return sMTeacherLab;
    }

    private TeacherLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DayCareBaseHelper(mContext).getWritableDatabase();
    }


    public List<Teacher> getTeachers() {

        List<Teacher> teachers = new ArrayList<>();

        DayCareCursorWrapper cursor = queryTeachersByActiveStatus(TeacherTable.Cols.TEACHER_IS_ACTIVE + " DESC");

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                teachers.add(cursor.getTeacher());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return teachers;
    }

    public Teacher getTeacher(UUID teacherId) {

        DayCareCursorWrapper cursor = queryTeachers(
                TeacherTable.Cols.TEACHER_UUID + " =?",
                new String[] { teacherId.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTeacher();
        }
        finally {
            cursor.close();
        }
    }

    public void addTeacher(Teacher teacher) {
        ContentValues values = getContentValues(teacher);

        mDatabase.insert(TeacherTable.NAME, null, values);
    }

    public void updateTeacherInformation(Teacher teacher) {
        String teacher_uuidString = teacher.getTeacherId().toString();
        ContentValues values = getContentValues(teacher);

        mDatabase.update(TeacherTable.NAME, values,
                TeacherTable.Cols.TEACHER_UUID + " =?",
                new String[] { teacher_uuidString });
    }

    public void deleteTeacher(Teacher teacher) {
        String teacher_uuidString = teacher.getTeacherId().toString();
        ContentValues values = getContentValues(teacher);

        mDatabase.delete(TeacherTable.NAME,
                TeacherTable.Cols.TEACHER_UUID + " =?",
                new String[] { teacher_uuidString });

    }

    private DayCareCursorWrapper queryTeachers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TeacherTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, // having
                null // orderBy
        );

        return new DayCareCursorWrapper(cursor);
    }

    private DayCareCursorWrapper queryTeachersByActiveStatus(String orderBy) {
        Cursor cursor = mDatabase.query(
                TeacherTable.NAME,
                null, // columns - null selects all columns
                null,
                null,
                null, //groupBy
                null, // having
                orderBy // orderBy
        );

        return new DayCareCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Teacher teacher) {
        ContentValues values = new ContentValues();
        values.put(TeacherTable.Cols.TEACHER_UUID, teacher.getTeacherId().toString());
        values.put(TeacherTable.Cols.TEACHER_NAME, teacher.getTeacherName());
        values.put(TeacherTable.Cols.TEACHER_GENDER, teacher.getGender());
        values.put(TeacherTable.Cols.TEACHER_SUBJECT_TEACHES, teacher.getSubjectTeaches());
        values.put(TeacherTable.Cols.TEACHER_PAYMENT_PER_DAY, teacher.getPaymentPerDay());
        values.put(TeacherTable.Cols.TEACHER_IS_ACTIVE, teacher.isActive() ? 1 : 0);

        return values;
    }
}
