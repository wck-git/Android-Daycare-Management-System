package com.bignerdranch.android.ckare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;

public class AppointmentListActivity extends AppCompatActivity {
    private static final String EXTRA_TEACHER_ID = "com.bignerdranch.android.ckare.teacher_id";
    private TextView mLayoutTextView;
    private UUID teacherId;

    public static Intent newIntent(Context packageContext, UUID teacherId) {
        Intent intent = new Intent(packageContext, AppointmentListActivity.class);
        intent.putExtra(EXTRA_TEACHER_ID, teacherId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // set the fragment container with a title
        mLayoutTextView = (TextView) findViewById(R.id.layout_title_text);
        mLayoutTextView.setVisibility(View.VISIBLE);
        mLayoutTextView.setText(R.string.appointment_list_title);

        teacherId = (UUID) getIntent().getSerializableExtra(EXTRA_TEACHER_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = AppointmentListFragment.newInstance(teacherId);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
