package com.bignerdranch.android.ckare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class AppointmentPagerActivity extends AppCompatActivity {
    private static final String EXTRA_APPOINTMENT_ID = "com.bignerdranch.android.ckare.appointment_id";
    private static final String EXTRA_TEACHER_ID = "com.bignerdranch.android.ckare.teacher_id";

    private ViewPager mViewPager;
    private UUID appointmentId;
    private UUID teacherId;
    private List<Appointment> mAppointments;

    public static Intent newIntent(Context packageContext, UUID appointmentId, UUID teacherId) {
        Intent intent = new Intent(packageContext, AppointmentPagerActivity.class);
        intent.putExtra(EXTRA_APPOINTMENT_ID, appointmentId);
        intent.putExtra(EXTRA_TEACHER_ID, teacherId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_container);

        // retrieve the appointment id and teacher id
        appointmentId = (UUID) getIntent().getSerializableExtra(EXTRA_APPOINTMENT_ID);
        teacherId = (UUID) getIntent().getSerializableExtra(EXTRA_TEACHER_ID);
        // get the appointment object
        mAppointments = AppointmentLab.get(this).getAppointments(teacherId);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Appointment appointment = mAppointments.get(position);
                return AppointmentFragment.newInstance(appointment.getAppointmentId());
            }

            @Override
            public int getCount() {
                return mAppointments.size();
            }
        });

        // get the selected appointment from the recycler view and set it to the view pager
        for (int i = 0; i < mAppointments.size(); i++) {
            if (mAppointments.get(i).getAppointmentId().equals(appointmentId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}