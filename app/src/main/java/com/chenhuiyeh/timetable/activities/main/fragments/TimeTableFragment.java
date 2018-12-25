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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
    private Button addButton;
    private Button cancelButton;

    private List<CourseInfo> courseInfoList= new ArrayList<>(); // for course list frag
    private StudentCourse studentCourse = new StudentCourse();

    List<CourseInfo> courses = new ArrayList<>();

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
        mCoursesViewModel = ViewModelProviders.of(getActivity()).get(CoursesViewModel.class);

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
        mCoursesViewModel.loadLiveDataFromDb().observe(this, new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                Log.d(TAG, "onChanged: changed data!!");
                studentCourse.setCourseList(courseInfos);
                executor.mainThread().execute(()->{
                    courseTable.setStudentCourse(studentCourse);
                    courseTable.updateTable();
                });
            }
        });

        // Set timetable
        executor.diskIO().execute(()->{
            studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());
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
                if (item != null)
                    showInfoDialog(view.getId(), item.getName(), item);
                else {
                    int row = block.getRow();
                    int col = block.getCol();
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

                boolean isInList = false;
                for(CourseInfo c: courses) {
                    if (c.getCourseCode().equalsIgnoreCase(code)){
                        isInList = true;
                        break;
                    }
                }

                if (!isInList) {
                    CourseInfo newCourse = new CourseInfo(name, code, professorText, descriptionText, locationText);

                    switch (col) {
                        case 1: {
                            newCourse.setTimes(new String[]{Integer.toString(row), "", "", "", "", "", ""});
                            break;
                        }
                        case 2: {
                            newCourse.setTimes(new String[]{"", Integer.toString(row), "", "", "", "", ""});
                            break;
                        }
                        case 3: {
                            newCourse.setTimes(new String[]{"", "", Integer.toString(row), "", "", "", ""});
                            break;
                        }
                        case 4: {
                            newCourse.setTimes(new String[]{"", "", "", Integer.toString(row), "", "", ""});
                            break;
                        }
                        case 5: {
                            newCourse.setTimes(new String[]{"", "", "", "", Integer.toString(row), "", ""});
                            break;
                        }
                        case 6: {
                            newCourse.setTimes(new String[]{"", "", "", "", "", Integer.toString(row), ""});
                            break;
                        }
                        case 7: {
                            newCourse.setTimes(new String[]{"", "", "", "", "", "", Integer.toString(row)});
                            break;
                        }

                    }
                    courseInfoList.add(newCourse); // for course list frag
                    Log.d(TAG, "onClick: " + newCourse.getName() + "added");

                    mCoursesViewModel.saveData(newCourse);
                } else {
                    executor.diskIO().execute(()->{
                        CourseInfo addedCourse = mCoursesViewModel.loadDataByIdFromDb(code);
                        String[] times = addedCourse.getTimes();
                        executor.mainThread().execute(()->{
                            switch (col) {
                                case 1: {
                                    times[0] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 2: {
                                    times[1] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 3: {
                                    times[2] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 4: {
                                    times[3] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 5: {
                                    times[4] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 6: {
                                    times[5] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                                case 7: {
                                    times[6] = Integer.toString(row);
                                    addedCourse.setTimes(times);
                                    break;
                                }
                            }
                            mCoursesViewModel.saveData(addedCourse);
                        });

                    });

                }

                executor.diskIO().execute(()->{
                    studentCourse.setCourseList(mCoursesViewModel.loadLiveDataFromDb().getValue());
                });

                updateCourseTable();

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
            studentCourse.setCourseList(mCoursesViewModel.loadDataFromDb());
            executor.mainThread().execute(()->{
                courseTable.setStudentCourse(studentCourse);
                courseTable.updateTable();
            });

        });
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
