package com.chenhuiyeh.timetable.activities.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.activities.main.fragments.CourseListFragment;
import com.chenhuiyeh.timetable.activities.main.fragments.InboxFragment;
import com.chenhuiyeh.timetable.activities.main.fragments.TimeTableFragment;
import com.chenhuiyeh.timetable.ui.TimeTableUI.CourseTableLayout;
import com.chenhuiyeh.timetable.ui.TimeTableUI.model.CourseInfo;
import com.chenhuiyeh.timetable.ui.TimeTableUI.model.StudentCourse;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements InboxFragment.OnFragmentInteractionListener,
    CourseListFragment.OnFragmentInteractionListener{

    private InboxFragment inboxFragment;
    private TimeTableFragment timeTableFragment;
    private CourseListFragment courseListFragment;

    private ActionBar mActionBar;

    private View actionBar;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

    }

    private void setupUIViews() {

        mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);

        actionBar = inflater.inflate(R.layout.custom_action_bar, null);
        mTitleTextView = (TextView)actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar)actionBar.getParent()).setContentInsetsAbsolute(0,0);

        BoomMenuButton leftBmb = (BoomMenuButton)actionBar.findViewById(R.id.action_bar_left_bmb);

        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_2);
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_2);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++){
            leftBmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder());
        }

        initFragment();
    }


    private void initFragment() {
        inboxFragment = new InboxFragment();
        courseListFragment = new CourseListFragment();
        timeTableFragment = new TimeTableFragment();

        if (!timeTableFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parentLayout, timeTableFragment).commit();
            setMainTitle(R.string.app_name);
        }
    }

    public void setMainTitle(int stringRes) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(stringRes);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}