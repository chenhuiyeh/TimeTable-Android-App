package com.chenhuiyeh.timetable.activities.main.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.ui.TimeTableUI.CourseBlock;
import com.chenhuiyeh.timetable.ui.TimeTableUI.CourseTableLayout;
import com.chenhuiyeh.timetable.activities.main.fragments.model.CourseInfo;
import com.chenhuiyeh.timetable.activities.main.fragments.model.StudentCourse;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment {
    private static final String TAG = "TimeTableFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CourseTableLayout courseTable;
    private Button addButton;
    private Button cancelButton;

    private ArrayList<CourseInfo> courseInfoList = new ArrayList<>();
    private StudentCourse studentCourse = new StudentCourse();



    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_time_table, container, false);
        courseTable = rootView.findViewById(R.id.courseTable);

//        // Add course1 - sample1
//        CourseInfo customCourseInfo = new CourseInfo();
//        customCourseInfo.setName("Course 1");
//        customCourseInfo.setCourseTime("1 2", "", "2", "3", "4", "", "");
//        courseInfoList.add(customCourseInfo);
//
//        // Set timetable
        studentCourse.setCourseList(courseInfoList);
        courseTable.setStudentCourse(studentCourse);

        courseTable.setTableInitializeListener(new CourseTableLayout.TableInitializeListener() {
            @Override
            public void onTableInitialized(CourseTableLayout course_table) {
//                Toast.makeText(getActivity(), "Finish intialized", Toast.LENGTH_SHORT).show();
            }
        });
        courseTable.setOnCourseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseInfo item = (CourseInfo) view.getTag();
                CourseBlock block = (CourseBlock) view;
                if (item != null)
                    showInfoDialog(view.getId(), item.getName(), item);
                else {
                    int row = block.getRow();
                    int col = block.getCol();
                    showAddCourseDialog(row, col);
                }
            }
        });

        return rootView;
    }

    private void showAddCourseDialog(final int row, final int col) {
        android.app.AlertDialog.Builder addCourseDialogBuilder = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_course_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_course, null);
        addButton = dialogView.findViewById(R.id.dialog_save_button);
        cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);

        final EditText courseName = dialogView.findViewById(R.id.add_course_name);
        final EditText courseCode = dialogView.findViewById(R.id.add_course_course_code);
        final EditText prof = dialogView.findViewById(R.id.add_course_prof);
        final EditText location = dialogView.findViewById(R.id.add_course_location);
        final EditText description = dialogView.findViewById(R.id.add_course_description);



        addCourseDialogBuilder.setView(dialogView);

        final android.app.AlertDialog alertDialog = addCourseDialogBuilder.create();
        alertDialog.setCancelable(true);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = courseName.getText().toString();
                String code = courseCode.getText().toString();
                String descriptionText = description.getText().toString();
                String locationText = location.getText().toString();
                String professorText = prof.getText().toString();

                CourseInfo newCourse = new CourseInfo(name, code, professorText, descriptionText, locationText);
//                newCourse.setCourseTime("1 2", "", "2", "3", "4", "", "");
                switch (col) {
                    case 1: newCourse.setCourseTime(Integer.toString(row), "","","","","","");
                            break;
                    case 2: newCourse.setCourseTime("", Integer.toString(row),"","","","","");
                        break;
                    case 3: newCourse.setCourseTime("", "",Integer.toString(row),"","","","");
                        break;
                    case 4: newCourse.setCourseTime("", "","",Integer.toString(row),"","","");
                        break;
                    case 5: newCourse.setCourseTime("", "","","",Integer.toString(row),"","");
                        break;
                    case 6: newCourse.setCourseTime("", "","","","",Integer.toString(row),"");
                        break;
                    case 7: newCourse.setCourseTime("", "","","","","",Integer.toString(row));
                        break;

                }
                courseInfoList.add(newCourse);
                Log.d(TAG, "onClick: " + newCourse.getName() + "added");
                studentCourse.setCourseList(courseInfoList);
                courseTable.setStudentCourse(studentCourse);
                courseTable.updateTable();
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showInfoDialog(int id, String courseName, CourseInfo course) {
        String message = String.format(
                "Course Name：", course.getName());
        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(courseName)
                .setMessage(message)
                .setPositiveButton("Detail", null);
        courseDialogBuilder.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
