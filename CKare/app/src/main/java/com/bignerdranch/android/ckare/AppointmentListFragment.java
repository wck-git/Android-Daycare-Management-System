package com.bignerdranch.android.ckare;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

public class AppointmentListFragment extends Fragment {
    private static final String ARG_TEACHER_ID = "teacher_id";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("EEEE");
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:a");
    private AppointmentAdapter mAppointmentAdapter;
    private Intent intent;
    private Menu myMenuItem;
    private Teacher mTeacher;
    private UUID teacherId;
    private SoundEffect soundEffect = new SoundEffect();

    // widgets
    private RecyclerView mAppointmentRecyclerView;

    public static AppointmentListFragment newInstance(UUID teacherId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEACHER_ID, teacherId);

        AppointmentListFragment fragment = new AppointmentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // retrieve values from the arguments passed by the activity
        teacherId = (UUID) getArguments().getSerializable(ARG_TEACHER_ID);
        // get the teacher object
        mTeacher = TeacherLab.get(getActivity()).getTeacher(teacherId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_container, container, false);

        mAppointmentRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_container);
        mAppointmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_appointment_list, menu);

        myMenuItem = menu;
        updateAddAppointmentMenuItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        soundEffect.loadButtonClickingSoundEffect(getActivity());

        switch (item.getItemId()) {
            case android.R.id.home:
                // end the activity and remove it from the back stack of the device
                getActivity().finish();

                return true;

            case R.id.menu_add_appointment:
                // add new appointment to the respective teacher by setting the teacher id to the appointment
                Appointment appointment = new Appointment();
                appointment.setTeacherId(teacherId);
                AppointmentLab.get(getActivity()).addAppointment(appointment);

                Toast.makeText(getActivity(), R.string.toast_add_appointment, Toast.LENGTH_SHORT).show();
                intent = AppointmentPagerActivity.newIntent(getActivity(), appointment.getAppointmentId(), appointment.getTeacherId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        AppointmentLab appointmentList = AppointmentLab.get(getActivity());
        List<Appointment> appointments = appointmentList.getAppointments(teacherId);

        if (mAppointmentAdapter == null) {
            mAppointmentAdapter = new AppointmentAdapter(appointments);
            mAppointmentRecyclerView.setAdapter(mAppointmentAdapter);
        }
        else {
            mAppointmentAdapter.setAppointments(appointments);
            mAppointmentAdapter.notifyDataSetChanged();
        }
    }

    private void updateAddAppointmentMenuItem() {
        if (mTeacher.isActive()) {
            myMenuItem.findItem(R.id.menu_add_appointment).setVisible(true);
        }
        else {
            Toast.makeText(getActivity(), R.string.toast_teacher_inactive_actions, Toast.LENGTH_SHORT).show();
            myMenuItem.findItem(R.id.menu_add_appointment).setVisible(false);
        }
    }

    // APPOINTMENT HOLDER
    private class AppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Appointment mAppointment;
        private ConstraintLayout mAppointmentLayout;
        private TextView mAppointmentChildNameTextView;
        private TextView mAppointmentPersonContactNumberTextView;
        private TextView mAppointmentDateTextView;
        private TextView mAppointmentDayOfWeekTextView;
        private TextView mAppointmentCheckInTimeTextView;
        private ImageView mAppointmentSettledImageView;
        private SoundEffect soundEffect = new SoundEffect();

        public AppointmentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_appointment_list, parent, false)); // CHANGE
            itemView.setOnClickListener(this);

            mAppointmentLayout = (ConstraintLayout) itemView.findViewById(R.id.appointment_layout);
            mAppointmentChildNameTextView = (TextView) itemView.findViewById(R.id.appointment_child_name);
            mAppointmentPersonContactNumberTextView = (TextView) itemView.findViewById(R.id.appointment_person_contact_number);
            mAppointmentDateTextView = (TextView) itemView.findViewById(R.id.appointment_date);
            mAppointmentDayOfWeekTextView= (TextView) itemView.findViewById(R.id.appointment_day_of_week);
            mAppointmentCheckInTimeTextView = (TextView) itemView.findViewById(R.id.appointment_check_in_time);
            mAppointmentSettledImageView = (ImageView) itemView.findViewById(R.id.appointment_settled);
        }

        public void bind(Appointment appointment) {
            mAppointment = appointment;

            // setting up the format
            String dateFormat = dateFormatter.format(mAppointment.getDate());
            String dayOfWeekFormat = dayOfWeekFormatter.format(mAppointment.getDate());
            String timeFormat = timeFormatter.format(mAppointment.getCheckInTime());

            // set the values from database to the resource holder in resource file
            String appointmentChildNameFormat = String.format(getResources().getString(R.string.appointment_list_child_name), mAppointment.getChildName());
            String appointmentPersonContactNumberFormat = String.format(getResources().getString(R.string.appointment_list_person_contact_number), mAppointment.getPersonContactNumber());
            String appointmentDateFormat = String.format(getResources().getString(R.string.appointment_list_date), dateFormat);
            String appointmentDayOfWeekFormat = String.format(getResources().getString(R.string.appointment_list_day_of_week), dayOfWeekFormat);
            String appointmentCheckInTimeFormat = String.format(getResources().getString(R.string.appointment_list_check_in_time), timeFormat);

            // setting the widgets with the formatted values
            mAppointmentChildNameTextView.setText(appointmentChildNameFormat);
            mAppointmentPersonContactNumberTextView.setText(appointmentPersonContactNumberFormat);
            mAppointmentDateTextView.setText(appointmentDateFormat);
            mAppointmentDayOfWeekTextView.setText(appointmentDayOfWeekFormat);
            mAppointmentCheckInTimeTextView.setText(appointmentCheckInTimeFormat);
            mAppointmentSettledImageView.setVisibility(appointment.isSettled() ? View.VISIBLE : View.GONE);
            mAppointmentLayout.setAlpha(mAppointment.isSettled() ? 0.5f : 1);
        }

        @Override
        public void onClick(View view) {
            soundEffect.loadButtonClickingSoundEffect(getActivity());

            Intent intent = AppointmentPagerActivity.newIntent(getActivity(), mAppointment.getAppointmentId(), mAppointment.getTeacherId());
            startActivity(intent);
        }
    }

    // APPOINTMENT ADAPTER
    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentHolder> {
        private List<Appointment> mAppointments;

        private AppointmentAdapter(List<Appointment> appointments) {
            mAppointments = appointments;
        }

        @Override
        public AppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AppointmentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AppointmentHolder holder, int position) {
            Appointment appointment = mAppointments.get(position);
            holder.bind(appointment);
        }

        @Override
        public int getItemCount() {
            return mAppointments.size();
        }

        public void setAppointments(List<Appointment> appointments) {
            mAppointments = appointments;
        }
    }

}
