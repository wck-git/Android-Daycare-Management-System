package com.bignerdranch.android.ckare.database;

public class DayCareDbSchema {
    // TEACHER TABLE
    public static final class TeacherTable {
        public static final String NAME = "teachers";

        public static final class Cols {
            public static final String TEACHER_UUID = "teacher_uuid";
            public static final String TEACHER_NAME = "teacher_name";
            public static final String TEACHER_GENDER = "teacher_gender";
            public static final String TEACHER_SUBJECT_TEACHES = "teacher_subject_teaches";
            public static final String TEACHER_PAYMENT_PER_DAY = "teacher_payment_per_day";
            public static final String TEACHER_IS_ACTIVE = "teacher_is_active";
        }
    }

    // APPOINTMENT TABLE
    public static final class AppointmentTable {
        public static final String NAME = "appointments";

        public static final class Cols {
            public static final String APPOINTMENT_UUID = "appointment_uuid";
            public static final String APPOINTMENT_TEACHER_UUID = "appointment_teacher_uuid";
            public static final String APPOINTMENT_CHILD_NAME = "appointment_child_name";
            public static final String APPOINTMENT_PERSON_CONTACT_NUMBER = "appointment_person_contact_number";
            public static final String APPOINTMENT_DATE = "appointment_date";
            public static final String APPOINTMENT_CHECK_IN_TIME = "appointment_check_in_time";
            public static final String APPOINTMENT_SETTLED = "appointment_settled";
        }
    }
}
