package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.util.Map;


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
    private final static int REQUEST_CODE_A = 0;

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
    private final static String TAG = "NotFragment";

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
            String id = ((StopActivity)getActivity()).superStop.id + ((StopActivity)getActivity()).underStop.id;
            dRef = dRef.child(id);
        }
        else{
            dRef = FD.getReference("PojazdyZgl");
            String id = ((BusActivity)getActivity()).bus.line + ";" + ((BusActivity)getActivity()).bus.brigade;
            dRef = dRef.child(id);
        }

        refreshDatabase();

        Button commit = view.findViewById(R.id.bus_make_notification);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                String id = "";
                if (type.equals("bus")){
                    intent = new Intent(getActivity(), NotificationActivity.class);
                    id = ((BusActivity)getActivity()).bus.line + ";" + ((BusActivity)getActivity()).bus.brigade;
                    intent.putExtra("idx1", ((BusActivity)getActivity()).line);
                    intent.putExtra("idx2", ((BusActivity)getActivity()).brigade);
                }
                else{
                    intent = new Intent(getActivity(), NotificationActivityStop.class);
                    id = ((StopActivity)getActivity()).superStop.id + ((StopActivity)getActivity()).underStop.id;
                    intent.putExtra("idx1", ((StopActivity)getActivity()).superId);
                    intent.putExtra("idx2", ((StopActivity)getActivity()).underId);
                }
                intent.putExtra("id", id);
                getActivity().startActivityForResult(intent, REQUEST_CODE_A);
            }
        });

        Button yes = view.findViewById(R.id.bus_yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner == null || spinner.getSelectedItem().toString().equals("Brak Aktywnych zgłoszeń")){
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setMessage("Czy na pewno chcesz potwierdzić zgłoszenie treści:\n" + spinner.getSelectedItem().toString())
                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addOne("yes");
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        Button no = view.findViewById(R.id.bus_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner == null  || spinner.getSelectedItem().toString().equals("Brak Aktywnych zgłoszeń")){
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setMessage("Czy na pewno chcesz zanegować zgłoszenie treści:\n" + spinner.getSelectedItem().toString())
                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addOne("no");
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        return view;
    }

    private void addOne(String yesNo){
        Map<String, Object> notificationObject = NotificationActivity.createNotificationObject("brak", yesNo);
        dRef.child(spinner.getSelectedItem().toString().replace(" ", "_").replace(",", "$")).child(MainActivity.login.getEmail().replace(".", "\'")).setValue(notificationObject);
        refreshDatabase();
        Toast.makeText(getActivity(), "Dokonano Zgłoszenia", Toast.LENGTH_SHORT).show();
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

    private void refreshDatabase(){
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        problems = new Problems();
                        while(iterable.iterator().hasNext()){
                            DataSnapshot type = iterable.iterator().next();
                            problems.add(type.getKey().replace("_", " ").replace("$", ","));
                            Iterable<DataSnapshot> it2 = type.getChildren();
                            while(it2.iterator().hasNext()){
                                DataSnapshot single = it2.iterator().next();
                                String yesNo = single.child("yesNo").toString();
                                if (yesNo.equals("yes")){
                                    problems.yesAdd();
                                }
                                else{
                                    problems.noAdd();
                                }
                            }
                        }
                        ArrayAdapter adapter;
                        if (problems.name.size() == 0){
                            String[] arr = new String[]{"Brak Aktywnych zgłoszeń"};
                            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arr);
                        }
                        else {
                            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, problems.name);
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner = getActivity().findViewById(R.id.bus_notification_spinner);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (spinner.getAdapter().isEmpty() || spinner.getSelectedItem().toString().equals("Brak Aktywnych zgłoszeń")){
                                    return;
                                }
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
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
