package com.chenhuiyeh.timetable.activities.courses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.data.Courses;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.CoursesViewHolder> {

    private static final String TAG = "CoursesListAdapter";
    private final LayoutInflater mLayoutInflater;
    private List<Courses> mCourses;
    private Context mContext;

    public CoursesListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CoursesListAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.courses_single_item, parent, false);
        return new CoursesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        if (mCourses != null) {
            Courses curr = mCourses.get(position);
            holder.courseNameTextView.setText(curr.getName());
            holder.courseCodeTextView.setText(curr.getCode());
        } else {
            holder.courseNameTextView.setText("No Courses Added Yet");
        }
    }

    // to notify data changed in list
    void setCourses (List<Courses> courses) {
        this.mCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        }

        return 0;
    }

    public Courses getCourseAtPosition (int position) {
        if (mCourses != null) {
            return mCourses.get(position);
        } else {
            Log.d(TAG, "getCourseAtPosition: nothing at this position: " + position);
            return null;
        }
    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private final TextView courseCodeTextView;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            courseNameTextView = itemView.findViewById(R.id.tvNameCourseItem);
            courseCodeTextView = itemView.findViewById(R.id.tvCodeCourseItem);
        }
    }
}
