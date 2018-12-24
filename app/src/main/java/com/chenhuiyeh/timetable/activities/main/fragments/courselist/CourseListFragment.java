package com.chenhuiyeh.timetable.activities.main.fragments.courselist;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.timetable.ui.LetterImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView courseListRecyclerView;
    private FloatingActionButton fabCourses;
    private List<CourseInfo> courses;

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
        fabCourses = rootView.findViewById(R.id.fabCourses);
        fabCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourseDialog();
            }
        });
        return rootView;
    }

    private void showAddCourseDialog() {
        AlertDialog.Builder addCourseDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_course_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_course, null);
        addCourseDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = addCourseDialogBuilder.create();
        alertDialog.show();
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
            holder.courseNameTextView.setText(courses.get(position).getName());
            holder.courseNameImageView.setLetter(courses.get(position).getName().charAt(0));
            holder.courseCodeTextView.setText(courses.get(position).getCourseCode());
        }

        @Override
        public int getItemCount() {
            return 0;
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
