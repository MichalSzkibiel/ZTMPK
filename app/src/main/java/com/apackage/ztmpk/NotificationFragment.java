package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private String type;
    private DatabaseReference dRef;
    private Problems problems;
    private int idx;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        Activity top = StopActivity.allStops.get(StopActivity.allStops.size() - 1);
        try{
            StopActivity stop = (StopActivity) top;
            type = "stop";
        } catch (Exception e){
            type = "bus";
        }
        FirebaseDatabase FD = FirebaseDatabase.getInstance();
        if (type.equals("stop")){
            dRef = FD.getReference("PrzystankiZgl");
            String id = MyMap.currentSuperStop.id + MyMap.currentUnderStop.id;
            dRef = dRef.getDatabase().getReference(id);
        }
        else{
            dRef = FD.getReference("PojazdyZgl");
            String id = MyMap.currentBus.line + ";" + MyMap.currentBus.brigade;
            dRef = dRef.getDatabase().getReference(id);
        }

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.toString();
                if (text != null){
                    try {
                        JSONObject json = new JSONObject(text);
                        problems = new Problems();
                        Iterator<String> it = json.keys();
                        while(it.hasNext()){
                            String name = it.next();
                            problems.add(name.replace("_", " ").replace("$", ","), json.getInt("yes"), json.getInt("no"));
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, problems.name);
                        spinner = getActivity().findViewById(R.id.bus_notification_spinner);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                idx = position;
                                TextView yesView = getActivity().findViewById(R.id.bus_yes_number);
                                yesView.setText(String.valueOf(problems.yes.get(idx)));
                                TextView noView = getActivity().findViewById(R.id.bus_no_number);
                                noView.setText(String.valueOf(problems.no.get(idx)));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button commit = view.findViewById(R.id.bus_make_notification);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.putExtra("type", type);
                getActivity().startActivity(intent);
            }
        });

        Button yes = view.findViewById(R.id.bus_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOne("yes");
            }
        });

        Button no = view.findViewById(R.id.bus_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOne("no");
            }
        });

        return view;
    }

    private void addOne(final String yesNo){
        dRef = dRef.getDatabase().getReference(spinner.getSelectedItem().toString().replace(" ", "_").replace(",", "$"));
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dRef.toString();
                try {
                    JSONObject json = new JSONObject(text);
                    int value = json.getInt(yesNo);
                    dRef.setValue(yesNo, value + 1);
                    Toast.makeText(getActivity(), "Dokonano Zgłoszenia", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
}
