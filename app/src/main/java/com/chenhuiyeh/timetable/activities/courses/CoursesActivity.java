package com.chenhuiyeh.timetable.activities.courses;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.Utils.LetterImageView;
import com.chenhuiyeh.timetable.data.Courses;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesActivity extends AppCompatActivity {

    public static final int NEW_COURSE_ACTIVITY = 1;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;

    private ArrayList<Courses> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mCourses = new ArrayList<>();

        setupUIViews();
        initToolbar();
        setupRecyclerView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CoursesActivity.this, AddCourseActivity.class);
                startActivityForResult(i, NEW_COURSE_ACTIVITY);
            }
        });

    }

    private void setupUIViews() {
        mToolbar = findViewById(R.id.ToolbarSubject);
        mRecyclerView = findViewById(R.id.rvCourses);
        fab = findViewById(R.id.fabCourses);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Courses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        CoursesListAdapter adapter = new CoursesListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_COURSE_ACTIVITY) {
            if (requestCode == RESULT_OK) {
                Courses newCourse = new Courses(data.getStringExtra(AddCourseActivity.REPLY_COURSENAME),
                        data.getStringExtra(AddCourseActivity.REPLY_COURSECODE), AddCourseActivity.REPLY_COURSEINFO);
                mCourses.add(newCourse);
            }
        }
    }

    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;

        private ArrayList<Courses> mCourses;

        public SimpleAdapter(Context context, ArrayList<Courses> courses) {
            this.mContext = context;
            this.mCourses = courses;

            this.mLayoutInflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            if (mCourses != null) {
                return mCourses.size();
            } else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mCourses != null) {
                return mCourses.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mLayoutInflater.inflate(R.layout.courses_single_item, null);
                holder.mLetterImageView = convertView.findViewById(R.id.ivCourseItem);
                holder.courseNametv = convertView.findViewById(R.id.tvNameCourseItem);
                holder.courseCodetv = convertView.findViewById(R.id.tvCodeCourseItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mLetterImageView.setOval(true);
            holder.mLetterImageView.setLetter(mCourses.get(position).getName().charAt(0));

            holder.courseNametv.setText(mCourses.get(position).getName());
            holder.courseCodetv.setText(mCourses.get(position).getCode());

            return convertView;
        }

        class ViewHolder {
            private LetterImageView mLetterImageView;
            private TextView courseNametv;
            private TextView courseCodetv;
        }
    }
}
