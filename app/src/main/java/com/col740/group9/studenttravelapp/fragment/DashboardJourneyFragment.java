package com.col740.group9.studenttravelapp.fragment;

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
import com.col740.group9.studenttravelapp.classes.Journey;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.col740.group9.studenttravelapp.classes.Constants.*;

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
    private View DashboardJourneyFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

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
        DashboardJourneyFragmentView = inflater.inflate(R.layout.fragment_dashboard_journey, container, false);

        mRecyclerView = (RecyclerView) DashboardJourneyFragmentView.findViewById(R.id.dashboard_journey_card_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);

        return DashboardJourneyFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = this.getActivity();
        if (journeyAdapter == null)
            journeyAdapter = new JourneyAdapter(mContext, journeyList);
        journeyAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(journeyAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // populate this list from server
        journeyList = new ArrayList<Journey>();
        fetchDatafromServer("journeys");

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

    public void fetchDatafromServer(String type) {
        // assumes that the base activity is home
        final Home baseHomeActivity = (Home) getActivity();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        serverURL + "/" + type + "/",
                        null,
                        this, this) {
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
        //            String type = "";
        //            if (response.length() > 0) {
        //                JSONObject firstElement = (JSONObject) response.get(0);
        //                if (firstElement.has("journey_id")) {
        //                    type = "journeys";
        //                }
        //            }
        Log.w("Journeys", response.toString());
        for (int i = 0; i < response.length(); i++) {
            try {
                journeyList.add(new Journey(response.getJSONObject(i)));
            } catch (JSONException e) {
                continue;
            } catch (ParseException e) {
                continue;
            }
        }
        journeyAdapter = new JourneyAdapter(mContext, journeyList);
        journeyAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(journeyAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
            TextView name, src_dest, start_date, participants;

            public MyViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                name = view.findViewById(R.id.dashboard_journey_card_name);
                src_dest = view.findViewById(R.id.dashboard_journey_card_src_dest);
                start_date = view.findViewById(R.id.dashboard_journey_card_start_date);
                participants = view.findViewById(R.id.dashboard_journey_card_participants);
            }
        }


        public JourneyAdapter(Context mContext, ArrayList<Journey> journeyList) {
            this.mContext = mContext;
            this.journeyList = journeyList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dashboard_journey_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Journey journey = journeyList.get(position);

            // Set values of views from Journey object
            holder.name.setText(journey.journey_id);
            holder.src_dest.setText("From " + journey.source + " to " + journey.destination);
            if (journey.date.compareTo(new Date()) < 0) {
                holder.start_date.setText("Started on " + journey.display_time);
                if (journey.participants.size() == 1)
                    holder.participants.setText("Only you went");
                else
                    holder.participants.setText(journey.participants.size() + " persons went");
            } else {
                holder.start_date.setText("Starting on " + journey.display_time);
                if (journey.participants.size() == 1)
                    holder.participants.setText("Only you are going");
                else
                    holder.participants.setText(journey.participants.size() + " persons going");
            }
        }

        @Override
        public int getItemCount() {
            return journeyList.size();
        }
    }
}
