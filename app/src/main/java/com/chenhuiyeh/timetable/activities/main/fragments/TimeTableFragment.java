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


    private List<CourseInfo> courseInfoList= new ArrayList<>(); // for course list frag
    private StudentCourse studentCourse = new StudentCourse();

    List<CourseInfo> courses = new ArrayList<>();
    public MediatorLiveData<List<CourseInfo>> currCourse = new MediatorLiveData<>();

    private CoursesViewModel mCoursesViewModel;
    private AppExecutor executor;


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
        mCoursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).setMainTitle(R.string.app_name);

        executor = AppExecutor.getInstance();
        executor.diskIO().execute(()->{
            courses = mCoursesViewModel.loadDataFromDb();
        });

        currCourse.addSource(mCoursesViewModel.loadLiveDataFromDb(), new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                Log.d(TAG, "onChanged: changed data!!");
                courses = courseInfos;
                studentCourse.setCourseList(courseInfos);
                courseTable.setStudentCourse(studentCourse);
                courseTable.updateTable();
            }
        });


        // Set timetable
        executor.diskIO().execute(()->{
            studentCourse.setCourseList(courses);
            courseTable.setStudentCourse(studentCourse);
            executor.mainThread().execute(()->{
                courseTable.updateTable();
            });

        });


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
                int row = block.getRow();
                int col = block.getCol();
                if (item != null)
                    showInfoDialog(item, row, col);
                else {
                    showAddCourseDialog(row, col);
                }
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
                    String[][] locations = new String[][]{
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "}
                    };
                    String[][] descriptions = new String[][]{
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "},
                            {" "," "," "," "," "," "," "}
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
                    for (int i = 0; i < locations.length; i++) {
                        for (int j = 0; j < locations[i].length; j++) {
                            Log.d(TAG, "onClick: add new location:" + i + " " + j + " " + locations[i][j]);
                        }
                    }
                    courseInfoList.add(newCourse); // for course list frag
                    Log.d(TAG, "onClick: " + newCourse.getName() + "added");

                    mCoursesViewModel.saveData(newCourse);

                    executor.diskIO().execute(()->{
                        studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());
                    });

                    updateCourseTable();
                } else {
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
                        addedCourse.setTimes(times);

                        locations[row-1][col-1] = locationText;
                        addedCourse.setLocations(locations);
                        for (int i = 0; i < locations.length; i++) {
                            for (int j = 0; j < locations[i].length; j++) {
                                Log.d(TAG, "onClick: add new location:" + i + " " + j + " " + locations[i][j]);
                            }
                        }

                        descriptions[row-1][col-1] = descriptionText;
                        addedCourse.setDescriptions(descriptions);

                        for (int i = 0; i < times.length; i++) {
                            Log.d(TAG,"time added: " + i + " " + times[i]);
                        }
                        mCoursesViewModel.saveData(addedCourse);
                        studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());

                        updateCourseTable();
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
        List<CourseInfo> courseInfoListSample = new ArrayList<>();
        String[][] tempLoca = new String[][]{
        {"Sample Location"," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "}};
        String[][] tempDesctip = new String[][]{
                {"This is a sample description"," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "}};
        courseInfoListSample.add(new CourseInfo("Sample", "Sample Code", "Sample Prof", tempLoca, tempDesctip));
        executor.diskIO().execute(()->{
            studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());
            executor.mainThread().execute(()->{
                courseTable.setStudentCourse(studentCourse);
                if (studentCourse == null || studentCourse.getCourseList() == null || studentCourse.getCourseList().isEmpty()) {
                    studentCourse.setCourseList(courseInfoListSample);
                    courseTable.setStudentCourse(studentCourse);
                }

                courseTable.updateTable();
            });

        });
    }

    private void showInfoDialog(CourseInfo course, final int row, final int col) {

        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(course.getCourseCode());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_course, null);
        Button addButton;
        Button cancelButton;
        addButton = dialogView.findViewById(R.id.dialog_save_button);
        cancelButton = dialogView.findViewById(R.id.dialog_cancel_button);
        addButton.setText("Save");

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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executor.diskIO().execute(()-> {
                    CourseInfo addedCourse = mCoursesViewModel.loadDataByIdFromDb(course.getCourseCode());
                    String[] times = course.getTimes();
                    String[][] locations = course.getLocations();
                    String[][] descriptions = course.getDescriptions();

                    String name = courseNameEditText.getText().toString();
                    String code = courseCodeEditText.getText().toString().toUpperCase(Locale.CANADA);
                    String professor = profEditText.getText().toString();
                    String thisDescrip = descriptionEditText.getText().toString();
                    String thisLocation = locationEditText.getText().toString();

                    addedCourse.setTimes(times);
                    addedCourse.setName(name);
                    addedCourse.setCourseCode(code);
                    addedCourse.setProfessor(professor);


                    for (int i = 0; i < locations.length; i++) {
                        for (int j = 0; j < locations[i].length; j++) {
                            Log.d(TAG, "onClick: location:" + i + " " + j + " " + locations[i][j]);
                        }
                    }
                    locations[row-1][col-1] = thisLocation;
                    addedCourse.setLocations(locations);

                    descriptions[row-1][col-1] = thisDescrip;
                    addedCourse.setDescriptions(descriptions);

                    mCoursesViewModel.saveData(addedCourse);
                    studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());

                    updateCourseTable();
                });
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
        mCoursesViewModel.loadLiveDataFromDb().removeObservers(this);
    }
}
