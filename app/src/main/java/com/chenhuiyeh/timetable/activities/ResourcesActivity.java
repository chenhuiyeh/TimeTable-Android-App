package com.chenhuiyeh.timetable.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.chenhuiyeh.timetable.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ResourcesActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        setupUIViews();
        initToolbar();
    }

    private void setupUIViews() {
        mToolbar = findViewById(R.id.ToolbarResourses);
        mListView = findViewById(R.id.lvResources);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Resources");
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
