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

public class TeacherPagerActivity extends AppCompatActivity {
    private static final String EXTRA_TEACHER_ID = "com.bignerdranch.android.ckare.teacher_id";

    private ViewPager mTeacherViewPager;
    private UUID teacherId;
    private List<Teacher> mTeachers;

    public static Intent newIntent(Context packageContext, UUID teacherId) {
        Intent intent = new Intent(packageContext, TeacherPagerActivity.class);
        intent.putExtra(EXTRA_TEACHER_ID, teacherId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_container);

        // retrieve the teacher id
        teacherId = (UUID) getIntent().getSerializableExtra(EXTRA_TEACHER_ID);
        // get the teacher object
        mTeachers = TeacherLab.get(this).getTeachers();

        mTeacherViewPager = (ViewPager) findViewById(R.id.view_pager_container);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mTeacherViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Teacher teacher = mTeachers.get(position);
                return TeacherFragment.newInstance(teacher.getTeacherId());
            }

            @Override
            public int getCount() {
                return mTeachers.size();
            }
        });

        // set teachers to the view pager
        for (int i = 0; i < mTeachers.size(); i++) {
            if (mTeachers.get(i).getTeacherId().equals(teacherId)) {
                mTeacherViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
