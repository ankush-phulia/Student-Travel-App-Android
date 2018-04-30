package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Create;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.Journey;
import com.col740.group9.studenttravelapp.classes.JourneyPoint;
import com.col740.group9.studenttravelapp.classes.LocationPoint;
import com.col740.group9.studenttravelapp.classes.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.col740.group9.studenttravelapp.classes.Constants.serverURL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateJourneyFragment.OnCreateJourneyFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateJourneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateJourneyFragment extends Fragment
    implements Response.Listener, Response.ErrorListener{

    private OnCreateJourneyFragmentInteractionListener mListener;
    private ArrayList<LocationPoint> journeyPointList;

    public CreateJourneyFragment() {
        // Required empty public constructor
    }

    public static CreateJourneyFragment newInstance(String param1, String param2) {
        CreateJourneyFragment fragment = new CreateJourneyFragment();
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_journey, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // populate this list with journey points
        journeyPointList = new ArrayList<LocationPoint>();
        fetchDatafromServer("/journey_points/");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateJourneyFragmentInteractionListener) {
            mListener = (OnCreateJourneyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateJourneyFragmentInteractionListener");
        }
    }

    public void fetchDatafromServer(String type){
        // assumes that the base activity is home
        final Create baseCreateActivity = (Create) getActivity();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        serverURL + "/" + type + "/",
                        null,
                        this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseCreateActivity.mToken);
                return headers;
            }
        };
        baseCreateActivity.mQueue.add(jsonArrayRequest);
    }

    public void postDatatoServer(String type, JSONObject data){
        // assumes that the base activity is home
        final Create baseCreateActivity = (Create) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        serverURL + "/" + type + "/",
                        data,
                        this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseCreateActivity.mToken);
                return headers;
            }
        };
        baseCreateActivity.mQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(Object response) {
        try {
            if (response.getClass() == JSONArray.class) {
                Log.w("Create Journey", response.toString());
                for (int i = 0; i < ((JSONArray) response).length(); i++) {
                    journeyPointList.add(new LocationPoint(((JSONArray) response).getJSONObject(i)));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

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
    public interface OnCreateJourneyFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnCreateJourneyFragmentInteraction(Uri uri);
    }
}
