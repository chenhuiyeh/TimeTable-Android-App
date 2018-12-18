package com.chenhuiyeh.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.Utils.LetterImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class WeekActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;
    public static SharedPreferences sharedPreferences; // data to be accessed across activities
    public static String SEL_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        setupUIViews();
        initToolbar();

        setUpListView();
    }

    private void setupUIViews() {
        mToolbar = findViewById(R.id.ToolbarWeek);
        mListView = findViewById(R.id.lavWeek);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Week");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpListView() {
        String[] week = getResources().getStringArray(R.array.week);

        WeekAdapter weekAdapter = new WeekAdapter(this,
                R.layout.activity_week_single_item, week);
        mListView.setAdapter(weekAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Monday").apply();
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Tuesday").apply();
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Wednesday").apply();
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Thursday").apply();
                        break;
                    }
                    case 4: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Friday").apply();
                        break;
                    }
                    case 5: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Saturday").apply();
                        break;
                    }
                    case 6: {
                        startActivity(new Intent(WeekActivity.this, DayDetail.class));
                        sharedPreferences.edit().putString(SEL_DAY, "Sunday").apply();
                        break;
                    }
                    default: break;
                }
            }
        });
    }

    public class WeekAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater mLayoutInflater;
        private String[] week = new String[]{};

        public WeekAdapter(@NonNull Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.week = objects;
            this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mLayoutInflater.inflate(resource, null);
                holder.ivLetter = convertView.findViewById(R.id.ivLetter);
                holder.tvWeek = (TextView) convertView.findViewById(R.id.tvWeek);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivLetter.setOval(true);
            holder.ivLetter.setLetter(week[position].charAt(0));
            holder.tvWeek.setText(week[position]);
            return convertView;
        }

        class ViewHolder {
            private LetterImageView ivLetter;
            private TextView tvWeek;
        }
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
}
