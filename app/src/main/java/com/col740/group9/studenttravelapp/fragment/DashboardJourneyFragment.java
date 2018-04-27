package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.Journey;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardJourneyFragment.OnDashboardJourneyFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DashboardJourneyFragment extends Fragment
    implements Response.Listener<JSONArray>, Response.ErrorListener {

    private OnDashboardJourneyFragmentInteractionListener mListener;
    private ArrayList<Journey> journeyList;
    private JourneyAdapter journeyAdapter;

    public DashboardJourneyFragment() {
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
        return inflater.inflate(R.layout.fragment_dashboard_journey, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // TODO : populate this list from server
        journeyList = new ArrayList<Journey>();
        fetchDatafromServer("journeys");

        journeyAdapter = new JourneyAdapter(context, journeyList);

        if (context instanceof OnDashboardJourneyFragmentInteractionListener) {
            mListener = (OnDashboardJourneyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDashboardJourneyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void fetchDatafromServer(String type){
        // TODO - assumes that the base activity is home
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

    @Override
    public void onResponse(JSONArray response) {
        try {
    //            String type = "";
    //            if (response.length() > 0) {
    //                JSONObject firstElement = (JSONObject) response.get(0);
    //                if (firstElement.has("journey_id")) {
    //                    type = "journeys";
    //                }
    //            }
            Log.w("Journeys", response.toString());
            for (int i = 0; i < response.length(); i++) {
                journeyList.add(new Journey(response.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
    public interface OnDashboardJourneyFragmentInteractionListener {

        void onDashboardJourneyFragmentInteraction(Uri uri);

    }

    public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<Journey> journeyList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card

            public MyViewHolder(View view) {
                super(view);
                // Populate View objects from layout
            }
        }


        public JourneyAdapter(Context mContext, ArrayList<Journey> journeyList) {
            this.mContext = mContext;
            this.journeyList = journeyList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.journey_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Journey journey = journeyList.get(position);

            // Set values of views from Journey object
        }

        @Override
        public int getItemCount() {
            return journeyList.size();
        }
    }
}
