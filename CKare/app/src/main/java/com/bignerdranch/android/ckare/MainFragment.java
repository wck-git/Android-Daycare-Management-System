package com.bignerdranch.android.ckare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {
    private SoundEffect soundEffect = new SoundEffect();
    private Button mGetStartedButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGetStartedButton = (Button)view.findViewById(R.id.get_started_button);
        mGetStartedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                startActivity(new Intent(getActivity(), TeacherListActivity.class));
                // end the activity and remove it from the back stack of the device
                getActivity().finish();
            }
        });

        return view;
    }

}
