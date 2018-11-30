package com.chenhuiyeh.timetable.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.activities.courses.CoursesActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();
        initToolbar();

        setUpListView();
    }

    private void setupUIViews() {
        toolbar = findViewById(R.id.ToolbarMain);
        listView = findViewById(R.id.lvMain);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Time Table");
    }

    private void setUpListView() {
        String[] title = getResources().getStringArray(R.array.Main);
        String[] description = getResources().getStringArray(R.array.Description);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, title, description);

        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position) {
                    case 0: {
                        Intent intent = new Intent(MainActivity.this, WeekActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(MainActivity.this, FacultyActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(MainActivity.this, ResourcesActivity.class);
                        startActivity(intent);
                        break;
                    }



                }
            }
        });
    }

    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private TextView title, description;
        private String[] titleArray;
        private String[] descriptionArray;
        private ImageView mImageView;

        public SimpleAdapter(Context context, String[] title, String[] description) {
            this.mContext = context;
            this.titleArray = title;
            this.descriptionArray = description;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if (titleArray != null)
                return titleArray.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            if (titleArray != null)
                return titleArray[i];
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

            title = view.findViewById(R.id.tvMain);
            description = view.findViewById(R.id.tvDescription);
            mImageView = view.findViewById(R.id.ivMain);

            title.setText(titleArray[position]);
            description.setText(descriptionArray[position]);

            if (titleArray[position].equalsIgnoreCase("Timetable"))
                mImageView.setImageResource(R.drawable.timetable);
            else if (titleArray[position].equalsIgnoreCase("Courses"))
                mImageView.setImageResource(R.drawable.book);
            else if (titleArray[position].equalsIgnoreCase("Faculty"))
                mImageView.setImageResource(R.drawable.ic_perm_contact_calendar);
            else if (titleArray[position].equalsIgnoreCase("Resources"))
                mImageView.setImageResource(R.drawable.settings);

            return view;
        }
    }
}
