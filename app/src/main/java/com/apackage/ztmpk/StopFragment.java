package com.apackage.ztmpk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StopFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static String TAG = "przystFrag";

    public StopFragment() {
        // Required empty public constructor
    }

    public static StopFragment newInstance() {
        StopFragment fragment = new StopFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_stop, container, false);

        TextView name = view.findViewById(R.id.stop_name);
        TextView lines = view.findViewById(R.id.stop_lines);

        String fullStop = "";
        try {
            fullStop = ((StopActivity)getActivity()).superStop.name + " " + ((StopActivity)getActivity()).underStop.id;
            if (!((StopActivity)getActivity()).superStop.borough.equals("WARSZAWA")){
                fullStop += " (" + ((StopActivity)getActivity()).superStop.borough + ")";
            }
            lines.setText(((StopActivity)getActivity()).underStop.lines.toString().replace("[", "").replace("]", ""));

        } catch(Exception e){
            fullStop = ((NotificationActivityStop)getActivity()).superStop.name + " " + ((NotificationActivityStop)getActivity()).underStop.id;
            if (!((NotificationActivityStop)getActivity()).superStop.borough.equals("WARSZAWA")){
                fullStop += " (" + ((NotificationActivityStop)getActivity()).superStop.borough + ")";
            }
            lines.setText(((NotificationActivityStop)getActivity()).underStop.lines.toString().replace("[", "").replace("]", ""));
        }
        name.setText(fullStop);

        return view;
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
        void onFragmentInteraction(Uri uri);
    }
}
