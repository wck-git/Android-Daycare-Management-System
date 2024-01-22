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

import java.util.List;

public class TeacherListFragment extends Fragment {
    private TeacherAdapter mTeacherAdapter;
    private SoundEffect soundEffect = new SoundEffect();

    // widgets
    private RecyclerView mTeacherRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_container, container, false);

        mTeacherRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_container);
        mTeacherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        inflater.inflate(R.menu.fragment_teacher_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        soundEffect.loadButtonClickingSoundEffect(getActivity());

        switch (item.getItemId()) {
            case R.id.menu_add_teacher:
                // add new teacher record
                Teacher teacher = new Teacher();
                TeacherLab.get(getActivity()).addTeacher(teacher);

                Toast.makeText(getActivity(), R.string.toast_add_teacher, Toast.LENGTH_SHORT).show();
                Intent intent = TeacherPagerActivity.newIntent(getActivity(), teacher.getTeacherId());
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        TeacherLab teacherLab = TeacherLab.get(getActivity());
        List<Teacher> teachers = teacherLab.getTeachers();

        if (mTeacherAdapter == null) {
            mTeacherAdapter = new TeacherAdapter(teachers);
            mTeacherRecyclerView.setAdapter(mTeacherAdapter);
        }
        else {
            mTeacherAdapter.setTeachers(teachers);
            mTeacherAdapter.notifyDataSetChanged();
        }
    }

    // TEACHER HOLDER
    private class TeacherHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Teacher mTeacher;

        private ConstraintLayout mTeacherLayout;
        private TextView mTeacherNameTextView;
        private TextView mTeacherSubjectTeachesTextView;
        private TextView mTeacherPaymentPerDayTextView;
        private TextView mTeacherIsActiveTextView;
        private ImageView mGenderIcon;
        private SoundEffect soundEffect = new SoundEffect();

        public TeacherHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_teacher_list, parent, false));
            itemView.setOnClickListener(this);

            mTeacherLayout = (ConstraintLayout) itemView.findViewById(R.id.teacher_layout);
            mTeacherNameTextView = (TextView) itemView.findViewById(R.id.teacher_name);
            mTeacherSubjectTeachesTextView = (TextView) itemView.findViewById(R.id.teacher_subject_teaches);
            mTeacherPaymentPerDayTextView = (TextView) itemView.findViewById(R.id.teacher_payment_per_day);
            mTeacherIsActiveTextView = (TextView) itemView.findViewById(R.id.teacher_is_active);
            mGenderIcon = (ImageView) itemView.findViewById(R.id.gender_icon);
        }

        public void bind(Teacher teacher) {
            mTeacher = teacher;

            // set the values from database to the resource holder in resource file
            String teacherNameFormat = String.format(getResources().getString(R.string.teacher_list_name), mTeacher.getTeacherName());
            String teacherSubjectTeachesFormat = String.format(getResources().getString(R.string.teacher_list_subject_teaches), mTeacher.getSubjectTeaches());
            String teacherPaymentPerDayFormat = String.format(getResources().getString(R.string.teacher_list_payment_per_day), mTeacher.getPaymentPerDay());
            String teacherIsActiveFormat = String.format(getResources().getString(R.string.teacher_list_is_active_status), String.valueOf(mTeacher.isActive()));

            // setting the widgets with the formatted values
            mTeacherNameTextView.setText(teacherNameFormat);
            mTeacherSubjectTeachesTextView.setText(teacherSubjectTeachesFormat);
            mTeacherPaymentPerDayTextView.setText(teacherPaymentPerDayFormat);
            mTeacherIsActiveTextView.setText(teacherIsActiveFormat);

            if (mTeacher.getGender() == null) { mGenderIcon.setImageResource(R.drawable.ic_person_icon); }
            else if (mTeacher.getGender().equals(getResources().getStringArray(R.array.gender_list)[1])) { mGenderIcon.setImageResource(R.drawable.ic_male_icon); }
            else if (mTeacher.getGender().equals(getResources().getStringArray(R.array.gender_list)[2])) { mGenderIcon.setImageResource(R.drawable.ic_female_icon); }
            else { mGenderIcon.setImageResource(R.drawable.ic_person_icon); }

            if (!mTeacher.isActive()) { mTeacherLayout.setAlpha(0.5f); }
            else { mTeacherLayout.setAlpha(1); }

        }

        @Override
        public void onClick(View view) {
            soundEffect.loadButtonClickingSoundEffect(getActivity());
            Intent intent = TeacherPagerActivity.newIntent(getActivity(), mTeacher.getTeacherId());
            startActivity(intent);
        }
    }

    // TEACHER ADAPTER
    private class TeacherAdapter extends RecyclerView.Adapter<TeacherHolder> {
        private List<Teacher> mTeachers;

        private TeacherAdapter(List<Teacher> teachers) {
            mTeachers = teachers;
        }

        @Override
        public TeacherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TeacherHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TeacherHolder holder, int position) {
            Teacher teacher = mTeachers.get(position);
            holder.bind(teacher);
        }

        @Override
        public int getItemCount() {
            return mTeachers.size();
        }

        public void setTeachers(List<Teacher> teachers) {
            mTeachers = teachers;
        }
    }
}
