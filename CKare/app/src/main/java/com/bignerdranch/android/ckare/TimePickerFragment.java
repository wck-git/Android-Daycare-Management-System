package com.bignerdranch.android.ckare;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_APPOINTMENT_CHECK_IN_TIME = "appointment_check_in_time";
    public static final String EXTRA_APPOINTMENT_TIME = "com.bignerdranch.android.ckare.check_in_time";
    private final Calendar calendar = Calendar.getInstance();
    private final int YEAR_CONSTANT_VAL = 0;
    private final int MONTH_CONSTANT_VAL = 0;
    private final int DAY_CONSTANT_VAL = 0;
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_APPOINTMENT_CHECK_IN_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        Date time = (Date) getArguments().getSerializable(ARG_APPOINTMENT_CHECK_IN_TIME);

        // initializing the calendar with the date value from the database
        calendar.setTime(time);
        int hour =  calendar.get(Calendar.HOUR_OF_DAY);
        int min =  calendar.get(Calendar.MINUTE);

        // setup time picker
        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(false);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(min);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                // Ok Button
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = mTimePicker.getHour();
                                int min = mTimePicker.getMinute();

                                Date time = new GregorianCalendar(YEAR_CONSTANT_VAL, MONTH_CONSTANT_VAL, DAY_CONSTANT_VAL, hour, min).getTime();
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                // Cancel Button
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_APPOINTMENT_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
