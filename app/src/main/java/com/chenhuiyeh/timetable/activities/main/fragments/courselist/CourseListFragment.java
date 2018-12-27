package com.chenhuiyeh.timetable.activities.main.fragments.courselist;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chenhuiyeh.module_cache_data.utils.AppExecutor;
import com.chenhuiyeh.module_cache_data.viewmodel.CoursesViewModel;
import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.timetable.activities.main.MainActivity;
import com.chenhuiyeh.timetable.activities.main.fragments.TimeTableFragment;
import com.chenhuiyeh.timetable.activities.main.fragments.utils.ItemClickSupport;
import com.chenhuiyeh.timetable.ui.LetterImageView;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseListFragment extends Fragment {
    private static final String TAG = "CourseListFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView courseListRecyclerView;
    private CoursesAdapter adapter;
    private List<CourseInfo> currentCourses;

    private CoursesViewModel mCoursesViewModel;
    private AppExecutor executor;

    public CourseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseListFragment newInstance(String param1, String param2) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_course_list, container, false);
        courseListRecyclerView = rootView.findViewById(R.id.course_list_recyclerview);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).setMainTitle(R.string.course_list_actionbar);
        adapter = new CoursesAdapter();
        courseListRecyclerView.setAdapter(adapter);
        courseListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        executor = AppExecutor.getInstance();
        mCoursesViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);

        mCoursesViewModel.loadLiveDataFromDb().observe(this, new Observer<List<CourseInfo>>() {
            @Override
            public void onChanged(List<CourseInfo> courseInfos) {
                Log.d(TAG, "onChanged: recyclerview update");
                executor.diskIO().execute(()->{
                    currentCourses = mCoursesViewModel.loadDataFromDb();
                });
                currentCourses = courseInfos;
                adapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        CourseInfo toDelete = adapter.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Deleting " + toDelete.getName(), Toast.LENGTH_LONG).show();
                        currentCourses.remove(position);
                        courseListRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, currentCourses.size());
                        adapter.notifyDataSetChanged();
                        mCoursesViewModel.deleteData(toDelete);
                    }
                }
        );
        touchHelper.attachToRecyclerView(courseListRecyclerView);
        ItemClickSupport.addTo(courseListRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showCourseDetailDialog(position);
                    }
                });
    }

    private void showCourseDetailDialog(int position) {
        String professor = adapter.getItemAtPosition(position).getProfessor();
        if (professor == null || professor.isEmpty()) professor = "N/A";
        String description = adapter.getItemAtPosition(position).getDescription();
        if (description == null || description.isEmpty()) description = "N/A";

        String message = String.format(Locale.CANADA, "%s%s\n%s%s\n%s%s\n%s%s",
                "Course Name: ", adapter.getItemAtPosition(position).getName(),
                "Course Code: ", adapter.getItemAtPosition(position).getCourseCode(),
                "Professor：", professor,
                "Description: ", description);
        AlertDialog.Builder courseDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(adapter.getItemAtPosition(position).getCourseCode() + " Details")
                .setMessage(message)
                .setPositiveButton("Done", null);

        courseDialogBuilder.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mCoursesViewModel.loadLiveDataFromDb().removeObservers(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

        @NonNull
        @Override
        public CoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_course_list_single_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder holder, int position) {
            holder.courseNameTextView.setText(currentCourses.get(position).getName());
            holder.courseNameImageView.setLetter(currentCourses.get(position).getName().charAt(0));
            holder.courseCodeTextView.setText(currentCourses.get(position).getCourseCode());
        }

        @Override
        public int getItemCount() {
            if (currentCourses != null) return currentCourses.size();

            return 0;
        }

        public CourseInfo getItemAtPosition (int position) {
            return currentCourses.get(position);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView courseNameTextView;
            TextView courseCodeTextView;
            LetterImageView courseNameImageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                courseNameTextView = itemView.findViewById(R.id.course_name_textview);
                courseCodeTextView = itemView.findViewById(R.id.course_code_textview);
                courseNameImageView = itemView.findViewById(R.id.course_item_letter_imageview);
            }


        }
    }
}
