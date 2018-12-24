package com.chenhuiyeh.timetable.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.ui.LetterImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class DayDetail extends AppCompatActivity {

    private ListView mListView;
    private Toolbar mToolbar;
    public static String[] Monday;
    public static String[] Tuesday;
    public static String[] Wednesday;
    public static String[] Thursday;
    public static String[] Friday;
    public static String[] Saturday;
    public static String[] Sunday;

    public String[] PreferredDay;
    public String[] PreferredTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        setupUIViews();
        initToolbar();
    }

    private void setupUIViews(){
        mListView = findViewById(R.id.lvDayDetail);
        mToolbar = findViewById(R.id.ToolbarDayDetail);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpListView(){
        // Day content

        String selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, null);

        if (selected_day.equalsIgnoreCase("Monday")){

        }

        SimpleAdapter adapter = new SimpleAdapter(this, PreferredDay, PreferredTime );
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

    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private TextView subject, time;
        private String[] subjectArray;
        private String[] timeArray;
        private LetterImageView mImageView;

        public SimpleAdapter(Context context, String[] subjectArray, String[] timeArray) {
            this.mContext = context;
            this.subjectArray = subjectArray;
            this.timeArray = timeArray;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (subjectArray != null)
                return subjectArray.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            if (subjectArray != null)
                return subjectArray[i];
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mLayoutInflater.inflate(R.layout.main_activity_single_item, null);
            }

            subject = view.findViewById(R.id.tvDayDetail);
            time = view.findViewById(R.id.tvTimeDayDetail);
            mImageView = (LetterImageView) view.findViewById(R.id.ivDayDetail);

            subject.setText(subjectArray[position]);
            time.setText(timeArray[position]);



            return view;
        }
    }
}
