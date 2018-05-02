package com.col740.group9.studenttravelapp.fragment;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Create;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.Checkpoint;
import com.col740.group9.studenttravelapp.classes.Journey;
import com.col740.group9.studenttravelapp.classes.JourneyPoint;
import com.col740.group9.studenttravelapp.classes.LocationPoint;
import com.col740.group9.studenttravelapp.classes.Notification;
import com.github.clans.fab.FloatingActionButton;

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
    private Journey journey;
    private ArrayList<LocationPoint> journeyPointList;
    private ArrayList<Checkpoint> checkpointList;
    private CheckpointAdapter checkpointAdapter;
    private View CreateJourneyFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    private EditText create_journey_name;
    private Button create_journey_date_button,create_journey_time_button,create_journey_add_checkpoint_button;
    private Spinner create_journey_source_spinner,create_journey_mode_spinner,create_journey_destination_spinner;


    public CreateJourneyFragment() {
        // Required empty public constructor
        journey = new Journey();
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
        checkpointList = new ArrayList<Checkpoint>();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CreateJourneyFragmentView = inflater.inflate(R.layout.fragment_create_journey, container, false);

        mRecyclerView = (RecyclerView) CreateJourneyFragmentView.findViewById(R.id.create_journey_checkpoints_recyclerview);
        mLayoutManager = new LinearLayoutManager(mContext);

        create_journey_name = (EditText) CreateJourneyFragmentView.findViewById(R.id.create_journey_name);

        create_journey_date_button = (Button) CreateJourneyFragmentView.findViewById(R.id.create_journey_date_button);
        create_journey_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create_journey_time_button = (Button) CreateJourneyFragmentView.findViewById(R.id.create_journey_time_button);
        create_journey_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create_journey_add_checkpoint_button = (Button) CreateJourneyFragmentView.findViewById(R.id.create_journey_add_checkpoint_button);
        create_journey_add_checkpoint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        create_journey_source_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_source_spinner);

        create_journey_mode_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_mode_spinner);

        create_journey_destination_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_destination_spinner);


        FloatingActionButton fab_create_journey_post = (FloatingActionButton) CreateJourneyFragmentView.findViewById(R.id.fab_create_journey_post);
        fab_create_journey_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        FloatingActionButton fab_create_journey_search = (FloatingActionButton) CreateJourneyFragmentView.findViewById(R.id.fab_create_journey_search);
        fab_create_journey_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return CreateJourneyFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mContext = this.getActivity();
        if(checkpointAdapter == null)
            checkpointAdapter = new CheckpointAdapter(mContext,checkpointList);
        checkpointAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(checkpointAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // populate this list with journey points
        journeyPointList = new ArrayList<LocationPoint>();
        fetchDatafromServer("/journey_points/");
        
        if (mContext instanceof OnCreateJourneyFragmentInteractionListener) {
            mListener = (OnCreateJourneyFragmentInteractionListener) mContext;
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

    public interface OnCreateJourneyFragmentInteractionListener {
        void OnCreateJourneyFragmentInteraction(Uri uri);
    }

    public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<Checkpoint> checkpointList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card
            TextView content;

            public MyViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                content = view.findViewById(R.id.checkpoint_card_content);
            }
        }


        public CheckpointAdapter(Context mContext, ArrayList<Checkpoint> checkpointList) {
            this.mContext = mContext;
            this.checkpointList = checkpointList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.checkpoint_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Checkpoint checkpoint = checkpointList.get(position);

            // Set values of views from Journey object
            holder.content.setText("From " + checkpoint.source + " to " + checkpoint.destination + " via " + checkpoint.transport);
        }

        @Override
        public int getItemCount() {
            return checkpointList.size();
        }
    }
}
