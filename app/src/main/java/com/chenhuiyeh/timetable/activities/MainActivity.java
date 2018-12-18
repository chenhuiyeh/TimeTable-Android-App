package com.chenhuiyeh.timetable.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.TimeTableUI.coursetable.CourseTableLayout;
import com.chenhuiyeh.timetable.TimeTableUI.coursetable.model.CourseInfo;
import com.chenhuiyeh.timetable.TimeTableUI.coursetable.model.StudentCourse;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private ActionBar mActionBar;
    private CourseTableLayout courseTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();
    }

    private void setupUIViews() {
        courseTable = findViewById(R.id.courseTable);

        // action bar
        mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);

        View actionBar = inflater.inflate(R.layout.custom_action_bar, null);
        TextView mTitleTextView = (TextView)actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("Time Table");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar)actionBar.getParent()).setContentInsetsAbsolute(0,0);

        BoomMenuButton leftBmb = (BoomMenuButton)actionBar.findViewById(R.id.action_bar_left_bmb);

        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_2);
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_2);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++){
            leftBmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilderWithDifferentPieceColor());
        }


        StudentCourse studentCourse = new StudentCourse();
        ArrayList<CourseInfo> courseInfoList = new ArrayList<>();

        // Add empty courses - sample1
        CourseInfo customCourseInfo = new CourseInfo();
        customCourseInfo.setName(" ");
        customCourseInfo.setCourseTime("1 3 5 7 9", "1 3 5 7 9", "1 3 5 7 9", "1 3 5 7 9", "1 3 5 7 9", "1 3 5 7 9", "1 3 5 7 9");
        courseInfoList.add(customCourseInfo);

        CourseInfo customCourseInfoEven = new CourseInfo();
        customCourseInfoEven.setName("  ");
        customCourseInfoEven.setCourseTime("2 4 6 8", "2 4 6 8", "2 4 6 8", "2 4 6 8", "2 4 6 8", "2 4 6 8", "2 4 6 8");
        courseInfoList.add(customCourseInfoEven);

        // Set timetable
        studentCourse.setCourseList(courseInfoList);
        courseTable.setStudentCourse(studentCourse);

        courseTable.setTableInitializeListener(new CourseTableLayout.TableInitializeListener() {
            @Override
            public void onTableInitialized(CourseTableLayout course_table) {
                Toast.makeText(MainActivity.this, "Finish intialized", Toast.LENGTH_SHORT).show();
            }
        });
        courseTable.setOnCourseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseInfo item = (CourseInfo) view.getTag();
//                showInfoDialog(view.getId(), item.getName(), item);
                addCourseToTableDialog(view.getId());
            }
        });

    }



//    private void initToolbar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Time Table");
//    }
    private void showInfoDialog(int id, String courseName, CourseInfo course) {
        String message = String.format(
                "Course Name：", course.getName());
        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(this)
                .setTitle(courseName)
                .setMessage(message)
                .setPositiveButton("Detail", null);
        courseDialogBuilder.show();
    }

    private void addCourseToTableDialog(int id) {
        AlertDialog.Builder newCourseDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Add Course")
                .setPositiveButton("Add", null); // add on click listener to add course
        newCourseDialogBuilder.show();
    }

}