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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Search;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.activity.Search;
import com.col740.group9.studenttravelapp.classes.Journey;
import com.col740.group9.studenttravelapp.classes.LocationPoint;
import com.col740.group9.studenttravelapp.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.col740.group9.studenttravelapp.classes.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchJourneyFragment.OnSearchJourneyFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchJourneyFragment extends Fragment
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private OnSearchJourneyFragmentInteractionListener mListener;
    private ArrayList<Journey> journeyList;
    private JourneyAdapter journeyAdapter;
    private View SearchJourneyFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    public SearchJourneyFragment() {
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
        SearchJourneyFragmentView = inflater.inflate(R.layout.fragment_search_journey, container, false);

        mRecyclerView = (RecyclerView) SearchJourneyFragmentView.findViewById(R.id.search_journey_card_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);

        return SearchJourneyFragmentView;
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

        // populate this list from overlap search result for journey_id
        journeyList = new ArrayList<Journey>();
        String journey_id = (String) getActivity().getIntent().getExtras().get("journey");
        try {
            JSONObject json = new JSONObject();
            json.put("journey_id", journey_id);
            postDatatoServer("search_journey", json);
        } catch (JSONException e) {
            Log.e("JSON ERROR", "   ");
        }

        if (context instanceof OnSearchJourneyFragmentInteractionListener) {
            mListener = (OnSearchJourneyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchJourneyFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void postDatatoServer(String type, JSONObject data) {
        // assumes that the base activity is home
        final Search baseSearchActivity = (Search) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        serverURL + "/" + type + "/",
                        data,
                        this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseSearchActivity.mToken);
                return headers;
            }
        };
        baseSearchActivity.mQueue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (response.has("checkpoints")) {
//                JSONArray jsons = (JSONArray) ((JSONObject) response).getJSONArray("");
//                for (int i = 0; i < jsons.length(); i++) {
//                    journeyList.add(new Journey(jsons.getJSONObject(i)));
//                }
                journeyList.add(new Journey(response));
            }
            else {
                Search baseSearchActivity = (Search) getActivity();
                baseSearchActivity.setResult(RESULT_OK);
                baseSearchActivity.finish();
            }
        } catch (JSONException e) {
            Log.e("JSON Error", response.toString());
        }
        catch (ParseException e) {
            Log.e("Parse Error", response.toString());
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
    public interface OnSearchJourneyFragmentInteractionListener {

        void OnSearchJourneyFragmentInteraction(Uri uri);

    }

    public class JourneyAdapter extends RecyclerView.Adapter<JourneyAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<Journey> journeyList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card
            TextView name, src_dest, start_date, participants;
            Button send_join_request_button;

            public MyViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                name = view.findViewById(R.id.search_journey_card_name);
                src_dest = view.findViewById(R.id.search_journey_card_src_dest);
                start_date = view.findViewById(R.id.search_journey_card_start_date);
                participants = view.findViewById(R.id.search_journey_card_participants);
                send_join_request_button = view.findViewById(R.id.search_journey_card_send_join_request_button);
            }
        }


        public JourneyAdapter(Context mContext, ArrayList<Journey> journeyList) {
            this.mContext = mContext;
            this.journeyList = journeyList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_journey_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Journey journey = journeyList.get(position);

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
            holder.send_join_request_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send request to join journey
                    JSONObject json = new JSONObject();
                    try {
                        json.put("journey_id", journey.journey_id);
                        postDatatoServer("make_request", json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return journeyList.size();
        }
    }
}
