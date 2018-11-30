package com.chenhuiyeh.timetable.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.chenhuiyeh.timetable.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FacultyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        setupUIViews();
        initToolbar();
    }

    private void setupUIViews() {
        mToolbar = findViewById(R.id.ToolbarFaculty);
        mListView = findViewById(R.id.lvFaculty);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Faculty");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
