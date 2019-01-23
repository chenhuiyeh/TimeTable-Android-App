package com.chenhuiyeh.timetable.activities.main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chenhuiyeh.module_cache_data.OnSaveDataListener;
import com.chenhuiyeh.module_cache_data.utils.AppExecutor;
import com.chenhuiyeh.module_cache_data.viewmodel.CoursesViewModel;
import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.activities.main.MainActivity;
import com.chenhuiyeh.timetable.ui.TimeTableUI.CourseBlock;
import com.chenhuiyeh.timetable.ui.TimeTableUI.CourseTableLayout;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.module_cache_data.model.StudentCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment implements OnSaveDataListener {

    private static final String TAG = "TimeTableFragment";

    private CourseTableLayout courseTable;

    private StudentCourse studentCourse = new StudentCourse();

    List<CourseInfo> courses = new ArrayList<>();

    private CoursesViewModel mCoursesViewModel;
    private AppExecutor executor;

    @Override
    public void onSaveCompleted() {
        studentCourse.setCourseList(courses);
        courseTable.setStudentCourse(studentCourse);
        courseTable.updateTable();
    }

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TimeTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance() {
        TimeTableFragment fragment = new TimeTableFragment();

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
        mCoursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).setMainTitle(R.string.app_name);

        executor = AppExecutor.getInstance();

        // Set default timetable - leave it as is
        executor.diskIO().execute(()->{
            courses = mCoursesViewModel.loadDataFromDb();
            studentCourse.setCourseList(courses);
            courseTable.setStudentCourse(studentCourse);
            executor.mainThread().execute(()->{
                courseTable.updateTable();
            });

        });

        mCoursesViewModel.loadLiveDataFromDb().observeForever(new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                Log.d(TAG, "onChanged: changed data!!");
                courses = courseInfos;
                studentCourse.setCourseList(courseInfos);
                courseTable.setStudentCourse(studentCourse);
                courseTable.updateTable();
            }
        });


        courseTable.setTableInitializeListener(new CourseTableLayout.TableInitializeListener() {
            @Override
            public void onTableInitialized(CourseTableLayout course_table) {
            }
        });
        courseTable.setOnCourseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseInfo item = (CourseInfo) view.getTag();
                CourseBlock block = (CourseBlock) view;
                int row = block.getRow();
                int col = block.getCol();
                if (item != null)
                    showInfoDialog(item, row, col);
                else {
                    showAddCourseDialog(row, col);
                }
            }
        });

        courseTable.setOnCourseLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: long clicked");
                CourseInfo item = (CourseInfo) v.getTag();
                CourseBlock block = (CourseBlock) v;
                int row = block.getRow();
                int col = block.getCol();
                if (item != null) {
                    showDeleteDialog(item, row, col);
                }
                else {
                    showAddCourseDialog(row, col);
                }
                return true;
            }
        });

    }

    private void showDeleteDialog(CourseInfo course, final int row, final int col) {
        android.app.AlertDialog.Builder addCourseDialogBuilder = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_dialog_title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete, null);
        addCourseDialogBuilder.setView(dialogView);

        final android.app.AlertDialog alertDialog = addCourseDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        Button yesButton = dialogView.findViewById(R.id.yes_button);
        Button noButton = dialogView.findViewById(R.id.no_button);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmptyTime = true;
                String[]times = course.getTimes();
                Log.d(TAG, "onClick: times before deletion: " + times[col-1]);

                String[][] locations = course.getLocations();
                locations[row-1][col-1] = " ";
                course.setLocations(locations);

                String[][] descriptions = course.getDescriptions();
                descriptions[row-1][col-1] = " ";
                course.setDescriptions(descriptions);

                String timesOfDay = times[col-1];
                String[] timesOfDayArr = timesOfDay.split(" ");
                for (int i = 0; i < timesOfDayArr.length; i++) {
                    if (timesOfDayArr[i].equals(Integer.toString(row))){
                        timesOfDayArr[i] = "";
                        Log.d(TAG, "onClick: time "+ row + "is being deleted");
                    }

                }

                String newTimes = "";
                for (int i = 0; i < timesOfDayArr.length; i++) {
                    Log.d(TAG, "onClick: " +timesOfDayArr[i]);
                    newTimes += timesOfDayArr[i] + " ";

                times[col-1] = newTimes;
                Log.d(TAG, "onClick: times after deletion: " + times[col-1]);
                Log.d(TAG, "onClick: row: " + (row));
                course.setTimes(times);

                }
                for (int i = 0; i < times.length; i++) {
                    if (times[i] != null || times[i] != "")
                        isEmptyTime = false;
                }

                if (!isEmptyTime)
                    mCoursesViewModel.saveData(course);
                else {
                    mCoursesViewModel.deleteData(course);
                    studentCourse.setCourseList(courses);
                    updateCourseTable();
                }


                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showAddCourseDialog(final int row, final int col) {
        android.app.AlertDialog.Builder addCourseDialogBuilder = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_course_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_course, null);

        Button addButton;
        Button cancelButton;
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
                String code = courseCode.getText().toString().toUpperCase(Locale.CANADA);
                String descriptionText = description.getText().toString();
                String locationText = location.getText().toString();
                String professorText = prof.getText().toString();

                boolean isInList = false;
                for(CourseInfo c: courses) {
                    if (c.getCourseCode().equalsIgnoreCase(code)){
                        isInList = true;
                        break;
                    }
                }

                if (!isInList) {
                    Log.d(TAG, "onClick: course: " + code + " not in list");
                    String[][] locations = new String[][]{
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""}
                    };
                    String[][] descriptions = new String[][]{
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""},
                            {"","","","","","",""}
                    };

                    CourseInfo newCourse = new CourseInfo(name, code, professorText, descriptions, locations);

                    switch (col) {
                        case 1: {
                            newCourse.setTimes(new String[]{Integer.toString(row), "", "", "", "", "", ""});
                            locations[row-1][0] = locationText;
                            descriptions[row-1][0] = descriptionText;

                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 2: {
                            newCourse.setTimes(new String[]{"", Integer.toString(row), "", "", "", "", ""});
                            locations[row-1][1] = locationText;
                            descriptions[row-1][1] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 3: {
                            newCourse.setTimes(new String[]{"", "", Integer.toString(row), "", "", "", ""});
                            locations[row-1][2] = locationText;
                            descriptions[row-1][2] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 4: {
                            newCourse.setTimes(new String[]{"", "", "", Integer.toString(row), "", "", ""});
                            locations[row-1][3] = locationText;
                            descriptions[row-1][3] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 5: {
                            newCourse.setTimes(new String[]{"", "", "", "", Integer.toString(row), "", ""});
                            locations[row-1][4] = locationText;
                            descriptions[row-1][4] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 6: {
                            newCourse.setTimes(new String[]{"", "", "", "", "", Integer.toString(row), ""});
                            locations[row-1][5] = locationText;
                            descriptions[row-1][5] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }
                        case 7: {
                            newCourse.setTimes(new String[]{"", "", "", "", "", "", Integer.toString(row)});
                            locations[row-1][6] = locationText;
                            descriptions[row-1][6] = descriptionText;
                            newCourse.setLocations(locations);
                            newCourse.setDescriptions(descriptions);
                            break;
                        }

                    }

                    mCoursesViewModel.saveData(newCourse);
                    Log.d(TAG, "onClick: course: " + newCourse.getName() + "saved");

                } else { // in list already
                    Log.d(TAG, "onClick: course: " + code + " in list");
                    executor.diskIO().execute(()->{
                        CourseInfo addedCourse = mCoursesViewModel.loadDataByIdFromDb(code);
                        String[][] locations = addedCourse.getLocations();
                        String[][] descriptions = addedCourse.getDescriptions();
                        String[] times = addedCourse.getTimes();
                        String currName = courseName.getText().toString();
                        String currProf = prof.getText().toString();

                        addedCourse.setName(currName);
                        addedCourse.setProfessor(currProf);

                        if (times[col-1] == null || times[col-1].isEmpty())
                            times[col-1] = Integer.toString(row);
                        else {
                            String currTime = times[col-1];
                            times[col-1] = Integer.toString(row) + ' ' + currTime;
                        }
                        Log.d(TAG, "onClick: times: " + times[col-1]);
                        addedCourse.setTimes(times);

                        locations[row-1][col-1] = locationText;
                        addedCourse.setLocations(locations);

                        descriptions[row-1][col-1] = descriptionText;
                        addedCourse.setDescriptions(descriptions);

                        mCoursesViewModel.saveData(addedCourse);
                    });


                }

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

    private void updateCourseTable() {
        executor.diskIO().execute(()->{
            studentCourse.setCourseList(courses);
            executor.mainThread().execute(()->{
                courseTable.setStudentCourse(studentCourse);
                courseTable.updateTable();
            });

        });
    }


    private void showInfoDialog(CourseInfo course, final int row, final int col) {

        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(course.getCourseCode());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_course_detail, null);

        Button saveButton;
        Button cancelButton;
        saveButton = dialogView.findViewById(R.id.dialog_save_button);
        cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        saveButton.setText("Save");

        final EditText courseNameEditText = dialogView.findViewById(R.id.add_course_name);
        final EditText courseCodeEditText = dialogView.findViewById(R.id.add_course_course_code);
        final EditText profEditText = dialogView.findViewById(R.id.add_course_prof);
        final EditText locationEditText = dialogView.findViewById(R.id.add_course_location);
        final EditText descriptionEditText = dialogView.findViewById(R.id.add_course_description);

        courseNameEditText.setClickable(true);
        courseNameEditText.setFocusable(true);
        if (course.getName()!= null)
            courseNameEditText.setText(course.getName());

        courseCodeEditText.setClickable(true);
        courseCodeEditText.setFocusable(true);
        if (course.getCourseCode()!= null)
            courseCodeEditText.setText(course.getCourseCode());

        profEditText.setClickable(true);
        profEditText.setFocusable(true);
        if (course.getProfessor()!= null)
            profEditText.setText(course.getProfessor());

        locationEditText.setClickable(true);
        locationEditText.setFocusable(true);
        if (course.getLocations()!= null && course.getLocations()[row-1].length != 0)
            locationEditText.setText(course.getLocations()[row-1][col-1]);

        descriptionEditText.setClickable(true);
        descriptionEditText.setFocusable(true);
        if(course.getDescriptions()!= null && course.getDescriptions()[row-1].length != 0)
            descriptionEditText.setText(course.getDescriptions()[row-1][col-1]);

        courseDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = courseDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] times = course.getTimes();
                String[][] locations = course.getLocations();
                String[][] descriptions = course.getDescriptions();

                String name = courseNameEditText.getText().toString();
                String code = courseCodeEditText.getText().toString().toUpperCase(Locale.CANADA);
                String professor = profEditText.getText().toString();
                String thisDescrip = descriptionEditText.getText().toString();
                String thisLocation = locationEditText.getText().toString();

                course.setTimes(times);
                course.setName(name);
                course.setCourseCode(code);
                course.setProfessor(professor);

                locations[row-1][col-1] = thisLocation;
                course.setLocations(locations);

                descriptions[row-1][col-1] = thisDescrip;
                course.setDescriptions(descriptions);

                mCoursesViewModel.saveData(course);
                studentCourse.setCourseList(courses);

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

    @Override
    public void onStop() {
        super.onStop();
    }


}
