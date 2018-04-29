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
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnUserProfileFragmentInteractionListener} interface
 * to handle interaction events.
 */

public class UserProfileFragment extends Fragment
        implements Response.Listener, Response.ErrorListener{
    private OnUserProfileFragmentInteractionListener mListener;

    private User user; // To be used between server and app

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfileFragmentInteractionListener) {
            mListener = (OnUserProfileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserProfileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void fetchDatafromServer(String type){
        // assumes that the base activity is home
        final Home baseHomeActivity = (Home) getActivity();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        baseHomeActivity.serverURL + "/" + type + "/",
                        null,
                        this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseHomeActivity.mToken);
                return headers;
            }
        };
        baseHomeActivity.mQueue.add(jsonArrayRequest);
    }

    public void postDatatoServer(String type, JSONObject data){
        // assumes that the base activity is home
        final Home baseHomeActivity = (Home) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        baseHomeActivity.serverURL + "/" + type + "/",
                        data,
                        this, this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseHomeActivity.mToken);
                return headers;
            }
        };
        baseHomeActivity.mQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(Object response) {
        try {
            if (response.getClass() == JSONArray.class) {
                String type = "";
                if (((JSONArray)response).length() > 0) {
                    JSONObject firstElement = (JSONObject) ((JSONArray)response).get(0);
                    if (firstElement.has("username")) {
                        type = "user_info";
                    }
                    user.updateUser(firstElement);
                }
            }
            Log.w("User Info", response.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        fetchDatafromServer("user_info");
        super.onResume();
    }

    @Override
    public void onPause(){
        // TODO send user object to server to change the data.
        try {
            postDatatoServer("/accounts/update/", user.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
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
    public interface OnUserProfileFragmentInteractionListener {
        void onUserProfileFragmentInteraction(Uri uri);
    }
}
