package com.chenhuiyeh.timetable.ui.TimeTableUI;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.module_cache_data.model.StudentCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

/** Copyright (c) 2018 Wei Chen Yao (https://yaoandy107.github.io/).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. **/

/**
 * Created by blackmaple on 2017/5/9.
 * modified by chenhuiyeh
 */

public class CourseTableLayout extends LinearLayout {
    private static final String TAG = "CourseTableLayout";

    private static final int TABLE_COL = 9;
    private static final int TABLE_ROW = 14;
    private boolean isInitialized = false;
    private boolean isDisplayABCD = false;
    private boolean isDisplaySat = false;
    private boolean isDisplaySun = false;
    private boolean isDisplayNoTime = false;
    private int ROW_HEIGHT;
    private View.OnClickListener onClickListener = null;
    private View.OnLongClickListener mOnLongClickListener = null;
    private TableInitializeListener initializeListener = null;
    private LinearLayout courseContainer;
    private StudentCourse studentCourse = new StudentCourse();
    private OnTouchListener onTouchListener;

    public CourseTableLayout(Context context) {
        super(context);
        inflate(context, R.layout.course_table_layout, this);
    }

    public CourseTableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.course_table_layout, this);
    }

    private static ArrayList<String> splitTime(String timeString) {
        ArrayList<String> infos = new ArrayList<>();
        String[] temp = timeString.split(" ");
        for (String t : temp) {
            switch (t) {
                case "A":
                    infos.add("10");
                    break;
                case "B":
                    infos.add("11");
                    break;
                case "C":
                    infos.add("12");
                    break;
                case "D":
                    infos.add("13");
                    break;
                default:
                    infos.add(t);
                    break;
            }
        }
        return infos;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isInitialized) {
            ROW_HEIGHT = Math.round((bottom - top) / 9.5f);
            initCourseTable();
            isInitialized = true;
            if (initializeListener != null) {
                initializeListener.onTableInitialized(this);
            }
            showCourse(studentCourse);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e(getClass().getSimpleName(), "onFinishInflate");
        courseContainer = findViewById(R.id.course_container);
    }

    public void setOnCourseClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnCourseLongClickListener(View.OnLongClickListener onCourseLongClickListener) {
        this.mOnLongClickListener = onCourseLongClickListener;
    }

    public void setTableInitializeListener(
            TableInitializeListener initializeListener) {
        this.initializeListener = initializeListener;
    }

    private void initCourseTable() {
        courseContainer.removeAllViews();
        LayoutParams title_row_params = new LayoutParams(
                LayoutParams.MATCH_PARENT, ROW_HEIGHT / 2);
        LayoutParams row_params = new LayoutParams(LayoutParams.MATCH_PARENT,
                ROW_HEIGHT);
        LayoutParams cell_params = new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1f);
        LayoutParams title_col_params = new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 0.5f);
        for (int i = 0; i < TABLE_ROW; i++) {
            LinearLayout tableRow = new LinearLayout(getContext());
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            tableRow.setLayoutParams(i == 0 ? title_row_params : row_params);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setBackgroundResource(i % 2 != 0 ? R.color.cloud
                    : R.color.white);
            for (int j = 0; j < TABLE_COL; j++) {
                CourseBlock tableCell = new CourseBlock(getContext());
                if (j == 0 && i > 0) {
                    tableCell.setText(Integer.toHexString(i).toUpperCase(
                            Locale.US));
                }
                tableCell.setId(j != TABLE_COL - 1 ? i : 14);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tableCell.setZ(5.0f);
                }
                cell_params.setMargins(5, 5, 5, 5);
//                    tableCell.setBackgroundResource(R.drawable.shape);
//                    tableCell.setElevation(20.0f);
                tableCell.setLayoutParams(j == 0 ? title_col_params
                        : cell_params);
                tableRow.addView(tableCell);

            }
            Log.e(getClass().getSimpleName(), "addRowView");
            courseContainer.addView(tableRow);
        }
        LinearLayout titleRow = (LinearLayout) courseContainer.getChildAt(0);
        CourseBlock text = (CourseBlock) titleRow.getChildAt(1);
        text.setText("Mon");
        text = (CourseBlock) titleRow.getChildAt(2);
        text.setText("Tue");
        text = (CourseBlock) titleRow.getChildAt(3);
        text.setText("Wed");
        text = (CourseBlock) titleRow.getChildAt(4);
        text.setText("Thur");
        text = (CourseBlock) titleRow.getChildAt(5);
        text.setText("Fri");
        text = (CourseBlock) titleRow.getChildAt(6);
        text.setText("Sat");
        text = (CourseBlock) titleRow.getChildAt(7);
        text.setText("Sun");
    }

    private void resetCourseTable() {
        for (int i = 1; i < TABLE_ROW; i++) {
            for (int j = 1; j < TABLE_COL; j++) {
                LinearLayout tableRow = (LinearLayout) courseContainer.getChildAt(i);
                if (tableRow != null) {
                    CourseBlock tableCell = (CourseBlock) tableRow.getChildAt(j);
                    tableCell.resetBlock();
                    tableCell.setOnClickListener(onClickListener);
                    tableCell.setOnLongClickListener(mOnLongClickListener);
                    tableCell.setLongClickable(true);
                    tableCell.setClickable(true);
                    tableCell.setRow(i);
                    tableCell.setCol(j);
                }
            }
        }
        isDisplayABCD = false;
        isDisplaySat = false;
        isDisplaySun = false;
        isDisplayNoTime = false;
        requestLayout();
    }

    private void controlColRowShow() {
        for (int i = 0; i < TABLE_ROW; i++) {
            LinearLayout tableRow = (LinearLayout) courseContainer
                    .getChildAt(i);
            if (tableRow != null) {
                CourseBlock satText = (CourseBlock) tableRow.getChildAt(6);
                satText.setVisibility(View.GONE);
                CourseBlock sunText = (CourseBlock) tableRow.getChildAt(7);
                sunText.setVisibility(View.GONE);
                CourseBlock noTimeText = (CourseBlock) tableRow.getChildAt(8);
                noTimeText.setVisibility(View.GONE);
                if (i > 9) {
                    tableRow.setVisibility(isDisplayABCD ? View.VISIBLE
                            : View.GONE);
                }
            }
        }
    }

    public void showCourse(StudentCourse studentCourse) {
        resetCourseTable();
        int color_index = 0;
        int[] color_array = getColorArray(9);
        int count = 0;
        for (CourseInfo item : studentCourse.getCourseList()) {
            boolean isHaveTime = false;
            for (int i = 0; i < 5; i++) {
                String time = item.getTimes()[i];
                Log.d(TAG, "showCourse: time: " +item.getName() + time);
                if(time!=null) {
                    ArrayList<String> s = splitTime(time);
                    for (String t : s) {
                        if (t != null && t.length()!= 0 && !t.equalsIgnoreCase("null")) {
                            Log.d(TAG, "showCourse: t: " + t);
                            int row = Integer.parseInt(t);
                            int col = i + 1;
                            isDisplayABCD = isDisplayABCD || row > 9;
                            isDisplaySun = false;
                            isDisplaySat = false;
                            setTableCell(row, col, color_array[color_index], item);
                            isHaveTime = true;
                        }
                    }
                }
            }
            if (!isHaveTime) {
                count++;
                isDisplayNoTime = true;
                setTableCell(count, 8, color_array[color_index], item);
            }
            color_index++;
        }
        controlColRowShow();
    }

    public void updateTable() {
        showCourse(studentCourse);
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    private int[] getColorArray(int color_count) {
        int[] colorArray = new int[color_count];
        int[] ints = getContext().getResources().getIntArray(R.array.course_table);
        List<Integer> defaoultColor = new ArrayList<>();
        for (int i : ints) {
            defaoultColor.add(i);
        }
        for (int i = 0; i < color_count; i++) {
            int random = (int) (Math.random() * defaoultColor.size());
            Log.d(TAG, "getColorArray: " + random);
            colorArray[i] = defaoultColor.remove(random);
        }
        return colorArray;
    }

    private void setTableCell(int row, int col, int color, CourseInfo course) {
        Log.e(getClass().getSimpleName(), "setTableCell");
        LinearLayout tableRow = (LinearLayout) courseContainer.getChildAt(row);
        if (tableRow != null) {
            CourseBlock table_cell = (CourseBlock) tableRow.getChildAt(col);
//            table_cell.setVisibility(View.INVISIBLE);
            table_cell.setText(course.getCourseCode().trim());
            table_cell.setTag(course);
            table_cell.setBackgroundColor(color);
            table_cell.setOnClickListener(onClickListener);
            table_cell.setOnLongClickListener(mOnLongClickListener);
            table_cell.setLongClickable(true);
            table_cell.setClickable(true);
//            setAnimation(table_cell);
        }
    }

//    private void setAnimation(final CourseBlock textview) {
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        WindowManager windowManager = (WindowManager) getContext()
//                .getSystemService(Context.WINDOW_SERVICE);
//        if (windowManager != null) {
//            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
//        }
//        final TranslateAnimation translateAnimation = new TranslateAnimation(
//                displaymetrics.widthPixels, 0, 0, 0);
//        translateAnimation.setDuration(500);
//        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//                textview.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // nothing to do
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // nothing to do
//            }
//
//        });
//        translateAnimation.setInterpolator(new OvershootInterpolator());
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                textview.startAnimation(translateAnimation);
//            }
//        }, (long) ((Math.random() * 500) + 500));
//    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return onTouchListener != null && onTouchListener.onTouch(this, ev) || super.dispatchTouchEvent(ev);
    }

    public interface TableInitializeListener {
        void onTableInitialized(CourseTableLayout course_table);
    }

}
