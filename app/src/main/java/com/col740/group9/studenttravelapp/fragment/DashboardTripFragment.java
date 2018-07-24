package com.col740.group9.studenttravelapp.fragment;

import static com.col740.group9.studenttravelapp.classes.Constants.*;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.Trip;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link DashboardTripFragment.OnDashboardTripFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link DashboardTripFragment} factory method to create an instance of
 * this fragment.
 */
public class DashboardTripFragment extends Fragment
        implements Response.Listener<JSONArray>, Response.ErrorListener {

    private OnDashboardTripFragmentInteractionListener mListener;
    private ArrayList<Trip> tripList;
    private TripAdapter tripAdapter;
    private View DashboardTripFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    public DashboardTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DashboardTripFragmentView =
                inflater.inflate(R.layout.fragment_dashboard_trip, container, false);

        mRecyclerView =
                (RecyclerView)
                        DashboardTripFragmentView.findViewById(
                                R.id.dashboard_trip_card_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);

        return DashboardTripFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = this.getActivity();
        if (tripAdapter == null) tripAdapter = new TripAdapter(mContext, tripList);
        tripAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(tripAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        // populate this list from server
        tripList = new ArrayList<Trip>();
        fetchDatafromServer("trips");

        if (context instanceof OnDashboardTripFragmentInteractionListener) {
            mListener = (OnDashboardTripFragmentInteractionListener) context;
        } 
        else {
            throw new RuntimeException(
                    context.toString()
                            + " must implement OnDashboardTripFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void fetchDatafromServer(String type) {
        //  assumes that the base activity is home
        final Home baseHomeActivity = (Home) getActivity();

        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(
                        Request.Method.GET, serverURL + "/" + type + "/", null, this, this) {
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
            Log.w("Trips", response.toString());
            for (int i = 0; i < response.length(); i++) {
                tripList.add(new Trip(response.getJSONObject(i)));
            }
            tripAdapter = new TripAdapter(mContext, tripList);
            tripAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(tripAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);
        } 
        catch (JSONException e) {
            e.printStackTrace();
        } 
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     *
     * <p>See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating
     * with Other Fragments</a> for more information.
     */
    public interface OnDashboardTripFragmentInteractionListener {
        void onDashboardTripFragmentInteraction(Uri uri);
    }

    public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<Trip> tripList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card
            TextView name, src_dest, start_date, duration, participants;

            public MyViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                name = view.findViewById(R.id.dashboard_trip_card_name);
                src_dest = view.findViewById(R.id.dashboard_trip_card_src_dest);
                start_date = view.findViewById(R.id.dashboard_trip_card_start_date);
                duration = view.findViewById(R.id.dashboard_trip_card_duration);
                participants = view.findViewById(R.id.dashboard_trip_card_participants);
            }
        }

        public TripAdapter(Context mContext, ArrayList<Trip> tripList) {
            this.mContext = mContext;
            this.tripList = tripList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.dashboard_trip_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Trip trip = tripList.get(position);

            // Set values of views from Trip object
            holder.name.setText(trip.trip_id);
            holder.src_dest.setText("From " + trip.source);
            holder.duration.setText("for " + trip.duration + " days");
            if (trip.date.compareTo(new Date()) < 0) {
                holder.start_date.setText("Started on " + trip.display_time);
                if (trip.participants.size() == 1) 
                    holder.participants.setText("Only you went");
                else 
                    holder.participants.setText(trip.participants.size() + " persons went");
            } 
            else {
                holder.start_date.setText("Starting on " + trip.display_time);
                if (trip.participants.size() == 1)
                    holder.participants.setText("Only you are going");
                else 
                    holder.participants.setText(trip.participants.size() + " persons going");
            }
        }

        @Override
        public int getItemCount() {
            return tripList.size();
        }
    }
}
