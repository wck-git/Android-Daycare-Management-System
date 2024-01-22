package com.bignerdranch.android.ckare;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AppointmentFragment extends Fragment {
    private static final String ARG_APPOINTMENT_ID = "appointment_id";
    private static final String DIALOG_APPOINTMENT_DATE = "DialogAppointmentDate";
    private static final String DIALOG_APPOINTMENT_CHECK_IN_TIME = "DialogAppointmentCheckInTime";
    private static final int REQUEST_APPOINTMENT_DATE = 0;
    private static final int REQUEST_APPOINTMENT_CHECK_IN_TIME = 1;
    private static final int REQUEST_APPOINTMENT_PHOTO = 2;
    private static final String AppointmentStudentNameRegExPattern = "^[a-zA-Z\\s]{1,25}$"; // allows only 1 to 25 letters or white spaces
    private static final String AppointmentPersonContactNumberRegExPattern = "^\\d{9,11}$"; // allows only 9 to 11 numbers
    private static final String InvalidTextFieldString = "";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:a");
    private NotificationSystem notificationSystem = new NotificationSystem();
    private File mPhotoFile;
    private Appointment mAppointment;
    private Teacher mTeacher;
    private UUID appointmentId;
    private SoundEffect soundEffect = new SoundEffect();

    // widgets
    private EditText mAppointmentChildNameField;
    private EditText mAppointmentPersonContactNumberField;
    private Button mAppointmentDateButton;
    private Button mAppointmentCheckInTimeButton;
    private CheckBox mAppointmentSettledCheckBox;
    private ImageButton mPhotoButton;
    private ImageButton mRemovePhotoButton;
    private ImageView mPhotoView;

    public static AppointmentFragment newInstance(UUID appointmentId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_APPOINTMENT_ID, appointmentId);

        AppointmentFragment fragment = new AppointmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // retrieve appointment id
        appointmentId = (UUID) getArguments().getSerializable(ARG_APPOINTMENT_ID);
        // get the appointment object
        mAppointment = AppointmentLab.get(getActivity()).getAppointment(appointmentId);
        // get the teacher object based on the appointment's teacher id from the object gotten
        mTeacher = TeacherLab.get(getActivity()).getTeacher(mAppointment.getTeacherId());

        mPhotoFile = AppointmentLab.get(getActivity()).getPhotoFile(mAppointment);
    }

    @Override
    public void onPause() {
        super.onPause();

        AppointmentLab.get(getActivity()).updateAppointmentInformation(mAppointment);
    }

    // MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_appointment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        soundEffect.loadButtonClickingSoundEffect(getActivity());

        switch (item.getItemId()) {
            case android.R.id.home:
                // end the activity and remove it from the back stack of the device
                getActivity().finish();
                return true;

            case R.id.menu_delete_appointment:
                // send a notification about the deletion
                notificationSystem.addNotification(getActivity(), mAppointment.getClass().getSimpleName(), mAppointment.getChildName());

                // delete the teacher record
                AppointmentLab.get(getActivity()).deleteAppointment(mAppointment);

                Toast.makeText(getActivity(), R.string.toast_delete_appointment, Toast.LENGTH_SHORT).show();
                // end the activity and remove it from the back stack of the device
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        // CHILD NAME
        mAppointmentChildNameField = (EditText) view.findViewById(R.id.appointment_child_name);
        mAppointmentChildNameField.setText(mAppointment.getChildName());
        updateEditTextFieldEditability(mAppointmentChildNameField);
        mAppointmentChildNameField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAppointmentChildNameField.getText().toString().matches(AppointmentStudentNameRegExPattern)) {
                    mAppointment.setChildName(s.toString());
                }
                else {
                    mAppointment.setChildName(InvalidTextFieldString);
                    Toast.makeText(getActivity(), R.string.toast_appointment_child_name_invalid ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // CONTACT NUMBER
        mAppointmentPersonContactNumberField= (EditText) view.findViewById(R.id.appointment_person_contact_number);
        mAppointmentPersonContactNumberField.setText(mAppointment.getPersonContactNumber());
        updateEditTextFieldEditability(mAppointmentPersonContactNumberField);
        mAppointmentPersonContactNumberField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAppointmentPersonContactNumberField.getText().toString().matches(AppointmentPersonContactNumberRegExPattern)) {
                    mAppointment.setPersonContactNumber(s.toString());
                }
                else {
                    mAppointment.setPersonContactNumber(InvalidTextFieldString);
                    Toast.makeText(getActivity(), R.string.toast_appointment_contact_number_invalid ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // DATE
        mAppointmentDateButton = (Button) view.findViewById(R.id.appointment_date);
        if (mAppointment.getDate() != null) {
            updateAppointmentDate();
        }
        updateEditTextFieldEditability(mAppointmentDateButton);
        mAppointmentDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mAppointment.getDate());
                dialog.setTargetFragment(AppointmentFragment.this, REQUEST_APPOINTMENT_DATE);
                dialog.show(manager, DIALOG_APPOINTMENT_DATE);
            }
        });

        // TIME
        mAppointmentCheckInTimeButton = (Button) view.findViewById(R.id.appointment_check_in_time);
        if (mAppointment.getCheckInTime() != null) {
            updateAppointmentCheckInTime();
        }
        updateEditTextFieldEditability(mAppointmentCheckInTimeButton);
        mAppointmentCheckInTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mAppointment.getCheckInTime());
                dialog.setTargetFragment(AppointmentFragment.this, REQUEST_APPOINTMENT_CHECK_IN_TIME);
                dialog.show(manager, DIALOG_APPOINTMENT_CHECK_IN_TIME);
            }
        });

        // SETTLED CHECKBOX
        mAppointmentSettledCheckBox = (CheckBox) view.findViewById(R.id.appointment_settled);
        mAppointmentSettledCheckBox.setChecked(mAppointment.isSettled());
        updateEditTextFieldEditability(mAppointmentSettledCheckBox);
        mAppointmentSettledCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                mAppointment.setSettled(isChecked);
            }
        });

        // PHOTO BUTTON
        mPhotoButton= (ImageButton) view.findViewById(R.id.appointment_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        updateEditTextFieldEditability(mPhotoButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.ckare.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().
                        getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_APPOINTMENT_PHOTO);
            }
        });

        // REMOVE PHOTO BUTTON
        mRemovePhotoButton = (ImageButton) view.findViewById(R.id.appointment_remove_photo);
        updateEditTextFieldEditability(mRemovePhotoButton);
        mRemovePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundEffect.loadButtonClickingSoundEffect(getActivity());
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.ckare.fileprovider", mPhotoFile);
                // delete the photo in the file provider
                getActivity().getContentResolver().delete(uri, null, null);

                updatePhotoView();
            }
        });

        // PHOTO VIEW
        mPhotoView= (ImageView) view.findViewById(R.id.appointment_photo);

        updatePhotoView();
        reminderInActiveStatusToast();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        soundEffect.loadButtonClickingSoundEffect(getActivity());
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        else if (requestCode == REQUEST_APPOINTMENT_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_APPOINTMENT_DATE);

            mAppointment.setDate(date);
            updateAppointmentDate();
        }
        else if (requestCode == REQUEST_APPOINTMENT_CHECK_IN_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_APPOINTMENT_TIME);

            mAppointment.setCheckInTime(time);
            updateAppointmentCheckInTime();
        }
        else if (requestCode == REQUEST_APPOINTMENT_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.ckare.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }

    private void updateAppointmentDate() {
        // set the date to the specified format
        String dateFormattedText = dateFormatter.format(mAppointment.getDate());
        mAppointmentDateButton.setText(dateFormattedText);
    }

    private void updateAppointmentCheckInTime() {
        // set the time to the specified format
        String timeFormattedText = timeFormatter.format(mAppointment.getCheckInTime());
        mAppointmentCheckInTimeButton.setText(timeFormattedText);
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }
        else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    // update the editability of all widgets if the teacher is not active
    // but still can delete the record
    private void updateEditTextFieldEditability(View widgets) {
        if (!mTeacher.isActive()) {
            widgets.setEnabled(false);
            widgets.setFocusable(false);
            widgets.setClickable(false);
            widgets.setLongClickable(false);
        }
        else {
            widgets.setEnabled(true);
            widgets.setFocusable(true);
            widgets.setClickable(true);
            widgets.setLongClickable(true);
        }
    }

    private void reminderInActiveStatusToast() {
        if (!mTeacher.isActive()) {
            Toast.makeText(getActivity(), R.string.toast_teacher_inactive_actions, Toast.LENGTH_SHORT).show();
        }
    }
}