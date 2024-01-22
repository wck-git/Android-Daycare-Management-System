package com.bignerdranch.android.ckare;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.ckare.TeacherListFragment;

public class TeacherListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() { return new TeacherListFragment(); }
}
