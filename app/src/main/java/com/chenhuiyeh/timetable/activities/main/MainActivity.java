package com.chenhuiyeh.timetable.activities.main;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.activities.main.fragments.inbox.InboxFragment;
import com.chenhuiyeh.timetable.activities.main.fragments.TimeTableFragment;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements InboxFragment.OnFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    private InboxFragment inboxFragment;
    private TimeTableFragment timeTableFragment;

    private ActionBar mActionBar;
    private BoomMenuButton leftBmb;

    private View actionBar;
    private TextView mTitleTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            Log.d(TAG, "onBackPressed: count is 0");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d(TAG, "onBackPressed: return to timetable");
            returnToTimetable();
        }
    }

    public boolean returnToTimetable() {
        if (timeTableFragment.isAdded())  return true;
        setMainTitle(R.string.app_name);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.parentLayout, timeTableFragment
        ).commit();
        return true;
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

        leftBmb = (BoomMenuButton)actionBar.findViewById(R.id.action_bar_left_bmb);

        leftBmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        leftBmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_2);
        leftBmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_2);
        for (int i = 0; i < leftBmb.getPiecePlaceEnum().pieceNumber(); i++){
            leftBmb.addBuilder(BuilderManager.getTextOutsideCircleButtonBuilder());
        }

        leftBmb.setOnBoomListener(new OnBoomListenerAdapter(){
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                super.onClicked(index, boomButton);
                selectFragment(index);
            }
        });
        initFragment();
    }

    private boolean selectFragment(int index) {
        TextOutsideCircleButton.Builder builder = (TextOutsideCircleButton.Builder) leftBmb.getBuilder(index);

        if (index == 0) {
            if (inboxFragment.isAdded()) return true;
            setMainTitle(R.string.inbox_actionbar);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.parentLayout, inboxFragment
            ).commit();

        } else if (index == 1) {
            if (timeTableFragment.isAdded())  return true;
            setMainTitle(R.string.app_name);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.parentLayout, timeTableFragment
            ).commit();

        }


        return true;
    }


    private void initFragment() {
        inboxFragment = new InboxFragment();
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