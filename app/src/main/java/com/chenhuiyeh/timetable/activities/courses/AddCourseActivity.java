package com.chenhuiyeh.timetable.activities.courses;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chenhuiyeh.timetable.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddCourseActivity extends AppCompatActivity {

    public static final String REPLY_COURSENAME =
            "timetable.COURSENAME";

    public static final String REPLY_COURSECODE =
            "timetable.COURSECODE";

    public static final String REPLY_COURSEINFO =
            "timetable.COURSEINFO";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextInputEditText tiCourseName;
    private TextInputEditText tiCourseCode;
    private TextInputEditText tiCourseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        setupUIViews();
        initToolbar();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (TextUtils.isEmpty(tiCourseName.getText()) &&
                        TextUtils.isEmpty(tiCourseCode.getText()) &&
                        TextUtils.isEmpty(tiCourseInfo.getText())) {
                    setResult(RESULT_CANCELED);
                } else {
                    String courseName = tiCourseName.getText().toString();
                    String courseCode = tiCourseCode.getText().toString();
                    String courseInfo = tiCourseInfo.getText().toString();

                    intent.putExtra(REPLY_COURSENAME, courseName);
                    intent.putExtra(REPLY_COURSECODE, courseCode);
                    intent.putExtra(REPLY_COURSEINFO, courseInfo);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

    }

    private void setupUIViews() {
        fab = findViewById(R.id.fabCourses);
        toolbar = findViewById(R.id.ToolbarAddCourse);
        tiCourseName = findViewById(R.id.tvCourseName);
        tiCourseCode = findViewById(R.id.tvCourseCode);
        tiCourseInfo = findViewById(R.id.tvCourseInfo);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Course");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
