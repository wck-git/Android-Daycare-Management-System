package com.bignerdranch.android.ckare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.UUID;

public class TeacherFragment extends Fragment {
    private static final String ARG_TEACHER_ID = "teacher_id";
    private static final String TeacherNameRegExPattern = "^[a-zA-Z\\s]{1,25}$"; // allows only 1 to 25 letters or white spaces
    private static final String SubjectTeachesRegExPattern = "^[a-zA-Z\\s,]{2,40}$"; // allows only 2 to 40 letters or white spaces or comma
    private static final String PaymentPerDayRegExPattern = "^\\d{0,2}?(\\.\\d{1,2})?$"; // allows only 0 up to 2 whole numbers, and also optional of up to 2 decimal places
    private static final String InvalidTextFieldString = "";
    private NotificationSystem notificationSystem = new NotificationSystem();
    private Teacher mTeacher;
    private UUID teacherId;
    private SoundEffect soundEffect = new SoundEffect();
    private String[] spinner_gender_list;

    // widgets
    private EditText mTeacherNameField;
    private Spinner mTeacherGenderSpinner;
    private EditText mTeacherSubjectTeachesField;
    private EditText mTeacherPaymentPerDayField;
    private CheckBox mIsActiveCheckBox;

    public static TeacherFragment newInstance(UUID teacherId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEACHER_ID, teacherId);

        TeacherFragment fragment = new TeacherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // retrieve teacher id
        teacherId = (UUID) getArguments().getSerializable(ARG_TEACHER_ID);
        // get teacher object
        mTeacher = TeacherLab.get(getActivity()).getTeacher(teacherId);
    }

    @Override
    public void onPause() {
        super.onPause();

        TeacherLab.get(getActivity()).updateTeacherInformation(mTeacher);
    }

    // MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_teacher, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        soundEffect.loadButtonClickingSoundEffect(getActivity());

        switch (item.getItemId()) {
            case R.id.menu_navigate_teacher_appointment:
                Intent intent = AppointmentListActivity.newIntent(getActivity(), teacherId);
                startActivity(intent);
                return true;

            case R.id.menu_delete_teacher:
                // send a notification about the deletion
                notificationSystem.addNotification(getActivity(), mTeacher.getClass().getSimpleName(), mTeacher.getTeacherName());

                // delete the teacher record
                TeacherLab.get(getActivity()).deleteTeacher(mTeacher);

                Toast.makeText(getActivity(), R.string.toast_delete_teacher, Toast.LENGTH_SHORT).show();
                // end the activity and remove it from the back stack of the device
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        // NAME
        mTeacherNameField = (EditText) view.findViewById(R.id.teacher_name);
        mTeacherNameField.setText(mTeacher.getTeacherName());
        mTeacherNameField.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTeacherNameField.getText().toString().matches(TeacherNameRegExPattern)) {
                    mTeacher.setTeacherName(s.toString());
                }
                else {
                    mTeacher.setTeacherName(InvalidTextFieldString);
                    Toast.makeText(getActivity(), R.string.toast_teacher_teacher_name_invalid ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // GENDER
        mTeacherGenderSpinner = (Spinner) view.findViewById(R.id.teacher_gender);

        // setting up the adapter for the spinner widget
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTeacherGenderSpinner.setAdapter(spinnerAdapter);

        // setting up the initial value of the spinner based on the teacher's record
        spinner_gender_list = getResources().getStringArray(R.array.gender_list);
        int spinner_gender_index = Arrays.asList(spinner_gender_list).indexOf(mTeacher.getGender());
        mTeacherGenderSpinner.setSelection(spinner_gender_index);

        mTeacherGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTeacher.setGender(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // SUBJECT TEACHES
        mTeacherSubjectTeachesField = (EditText) view.findViewById(R.id.teacher_subject_teaches);
        mTeacherSubjectTeachesField.setText(mTeacher.getSubjectTeaches());
        mTeacherSubjectTeachesField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTeacher.setSubjectTeaches(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTeacherSubjectTeachesField.getText().toString().matches(SubjectTeachesRegExPattern)) {
                    mTeacher.setSubjectTeaches(s.toString());
                }
                else {
                    mTeacher.setSubjectTeaches(InvalidTextFieldString);
                    Toast.makeText(getActivity(), R.string.toast_teacher_subject_teaches_invalid ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // PAYMENT PER DAY
        mTeacherPaymentPerDayField = (EditText) view.findViewById(R.id.teacher_payment_per_day);
        mTeacherPaymentPerDayField.setText(String.valueOf(mTeacher.getPaymentPerDay()));
        mTeacherPaymentPerDayField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTeacherPaymentPerDayField.getText().toString().matches(PaymentPerDayRegExPattern) && s.length() != 0) {
                    mTeacher.setPaymentPerDay(Double.valueOf(s.toString()));
                }
                // setting the value to 0
                // prevents the system from converting empty string to double value
                // which would cause a compilation error
                else {
                    mTeacher.setPaymentPerDay(0);
                    Toast.makeText(getActivity(), R.string.toast_teacher_payment_per_day_invalid ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // CHECKBOX
        mIsActiveCheckBox = (CheckBox) view.findViewById(R.id.teacher_is_active);
        mIsActiveCheckBox.setChecked(mTeacher.isActive());
        mIsActiveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTeacher.setActive(isChecked);
            }
        });

        return view;
    }

}