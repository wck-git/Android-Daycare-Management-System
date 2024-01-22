package com.bignerdranch.android.ckare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private TextView mLayoutTitleTextView;

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // set the fragment container with a title
        mLayoutTitleTextView = (TextView) findViewById(R.id.layout_title_text);
        mLayoutTitleTextView.setVisibility(View.VISIBLE);
        mLayoutTitleTextView.setText(R.string.teacher_list_title);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
