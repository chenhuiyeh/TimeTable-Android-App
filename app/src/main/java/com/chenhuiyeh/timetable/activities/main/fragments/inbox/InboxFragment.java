package com.chenhuiyeh.timetable.activities.main.fragments.inbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chenhuiyeh.module_cache_data.model.CourseInfo;
import com.chenhuiyeh.module_cache_data.model.InboxItem;
import com.chenhuiyeh.module_cache_data.utils.AppExecutor;
import com.chenhuiyeh.module_cache_data.viewmodel.CoursesViewModel;
import com.chenhuiyeh.module_cache_data.viewmodel.InboxItemViewModel;
import com.chenhuiyeh.timetable.R;
import com.chenhuiyeh.timetable.activities.main.MainActivity;

import com.chenhuiyeh.timetable.activities.main.fragments.utils.ItemClickSupport;
import com.chenhuiyeh.timetable.ui.LetterImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
 * {@link InboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {
    private static final String TAG = "InboxFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private InboxItemViewModel inboxItemViewModel;

    private OnFragmentInteractionListener mListener;

    private List<InboxItem> mInboxItemList;
    private RecyclerView mRecyclerView;
    private InboxAdapter adapter;
    private FloatingActionButton mFloatingActionButton;

    private InboxItemViewModel mInboxItemViewModel;
    private AppExecutor executor;

    public InboxFragment() {
        // Required empty public constructor
    }


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        mRecyclerView = rootView.findViewById(R.id.inbox_recyclerview);
        mFloatingActionButton = rootView.findViewById(R.id.fabInbox);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).setMainTitle(R.string.inbox_actionbar);
        adapter = new InboxAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        executor = AppExecutor.getInstance();

        mInboxItemViewModel = ViewModelProviders.of(this).get(InboxItemViewModel.class);


        mInboxItemViewModel.loadLiveDataFromDb().observeForever( new Observer<List<InboxItem>>() {
            @Override
            public void onChanged(List<InboxItem> inboxItems) {
                Log.d(TAG, "onChanged: item added");
//                executor.diskIO().execute(()->{
//                    mInboxItemList = mInboxItemViewModel.loadDataFromDb();
//                });
                mInboxItemList = inboxItems;
                adapter.notifyDataSetChanged();
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddInboxItemDialog();
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
                        InboxItem toDelete = adapter.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Deleting " + toDelete.getTitle(), Toast.LENGTH_LONG).show();
                        mInboxItemList.remove(position);
                        mRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, mInboxItemList.size());
                        adapter.notifyDataSetChanged();
                        mInboxItemViewModel.deleteData(toDelete);
                    }
                }
        );
        touchHelper.attachToRecyclerView(mRecyclerView);

        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showDetailInboxItemDialog(position);
                    }
                });
    }
    private void showDetailInboxItemDialog(int position){
        android.app.AlertDialog.Builder editInboxItemDialogBuilder = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.detail_inbox_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_inbox_item, null);
        editInboxItemDialogBuilder.setView(dialogView);

        final android.app.AlertDialog alertDialog = editInboxItemDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();

        EditText titleEditText = dialogView.findViewById(R.id.add_item_title);
        EditText descripEditText = dialogView.findViewById(R.id.add_item_descrip);

        titleEditText.setClickable(true);
        titleEditText.setFocusable(true);
        if (adapter.getItemAtPosition(position).getTitle() != null)
            titleEditText.setText(adapter.getItemAtPosition(position).getTitle());

        descripEditText.setClickable(true);
        descripEditText.setFocusable(true);
        if (adapter.getItemAtPosition(position).getDescription()!= null)
            descripEditText.setText(adapter.getItemAtPosition(position).getDescription());

        Button saveButton = dialogView.findViewById(R.id.add_inbox_dialog_save_button);
        Button cancelButton = dialogView.findViewById(R.id.add_inbox_dialog_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descripEditText.getText().toString();

                int id = adapter.getItemAtPosition(position).getId();
                InboxItem item = mInboxItemList.get(position);
                item.setDescription(description);
                item.setTitle(title);
                mInboxItemList.set(position, item);
                adapter.notifyItemChanged(position);
                adapter.notifyDataSetChanged();

                executor.diskIO().execute(()->{
                    mInboxItemViewModel.saveData(item);
                });


                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }


    private void showAddInboxItemDialog() {
        android.app.AlertDialog.Builder addInboxDialogBuilder = new android.app.AlertDialog.Builder(getActivity())
                .setTitle(R.string.inbox_actionbar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_inbox_item, null);
        EditText titleEditText = dialogView.findViewById(R.id.add_item_title);
        EditText descripEditText = dialogView.findViewById(R.id.add_item_descrip);
        Button saveButton = dialogView.findViewById(R.id.add_inbox_dialog_save_button);
        Button cancelButton = dialogView.findViewById(R.id.add_inbox_dialog_cancel_button);
        addInboxDialogBuilder.setView(dialogView);

        final android.app.AlertDialog alertDialog = addInboxDialogBuilder.create();
        alertDialog.setCancelable(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descripEditText.getText().toString();

                InboxItem newInboxItem = new InboxItem(title, description);
                mInboxItemList.add(newInboxItem);
                adapter.notifyItemInserted(mInboxItemList.indexOf(newInboxItem));
                adapter.notifyItemRangeChanged(mInboxItemList.indexOf(newInboxItem), mInboxItemList.size());
                adapter.notifyDataSetChanged();
                executor.diskIO().execute(()->{
                    mInboxItemViewModel.saveData(newInboxItem);
                    Log.d(TAG, "onClick: saved item: " + newInboxItem.getTitle());
                });
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

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

    class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_inbox_single_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.inboxTitleTextView.setText(mInboxItemList.get(position).getTitle());
            holder.inboxNameLetterImageView.setLetter(mInboxItemList.get(position).getTitle().charAt(0));
            holder.descripTextView.setText(mInboxItemList.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            if (mInboxItemList != null) return mInboxItemList.size();

            return 0;
        }

        public InboxItem getItemAtPosition(int pos) {
            return mInboxItemList.get(pos);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView inboxTitleTextView;
            TextView descripTextView;
            LetterImageView inboxNameLetterImageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                inboxTitleTextView = itemView.findViewById(R.id.inbox_title_textview);
                descripTextView = itemView.findViewById(R.id.inbox_descrip_textview);
                inboxNameLetterImageView = itemView.findViewById(R.id.inbox_item_letter_imageview);
            }
        }
    }
}
