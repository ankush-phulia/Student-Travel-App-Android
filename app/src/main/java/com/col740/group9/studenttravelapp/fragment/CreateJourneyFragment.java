package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Create;
import com.col740.group9.studenttravelapp.classes.Checkpoint;
import com.col740.group9.studenttravelapp.classes.Journey;
import com.col740.group9.studenttravelapp.classes.JourneyPoint;
import com.col740.group9.studenttravelapp.classes.LocationPoint;
import com.col740.group9.studenttravelapp.classes.User;
import com.github.clans.fab.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.col740.group9.studenttravelapp.classes.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateJourneyFragment.OnCreateJourneyFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateJourneyFragment extends Fragment
        implements Response.Listener, Response.ErrorListener, View.OnClickListener {

    private User originalPoster;
    private OnCreateJourneyFragmentInteractionListener mListener;
    private Journey journey;
    private ArrayList<LocationPoint> locationPointList;
    private ArrayList<CharSequence> locationPointNameList;
    private ArrayList<Checkpoint> checkpointList;
    private CheckpointAdapter checkpointAdapter;
    private View CreateJourneyFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    private EditText create_journey_name;
    private Button create_journey_date_button, create_journey_time_button;
    private Spinner create_journey_source_spinner, create_journey_mode_spinner, create_journey_destination_spinner;
    private Calendar calendar;


    public CreateJourneyFragment() {
        // Required empty public constructor
        journey = new Journey();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkpointList = new ArrayList<Checkpoint>();
        mContext = this.getActivity();
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
        create_journey_date_button.setOnClickListener(this);
        create_journey_time_button = (Button) CreateJourneyFragmentView.findViewById(R.id.create_journey_time_button);
        create_journey_time_button.setOnClickListener(this);

        create_journey_source_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_source_spinner);
        create_journey_destination_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_destination_spinner);

        create_journey_mode_spinner = (Spinner) CreateJourneyFragmentView.findViewById(R.id.create_journey_mode_spinner);
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(mContext, R.array.tranport_mode_array, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        create_journey_mode_spinner.setAdapter(modeAdapter);


        Button create_journey_add_checkpoint_button = (Button) CreateJourneyFragmentView.findViewById(R.id.create_journey_add_checkpoint_button);
        create_journey_add_checkpoint_button.setOnClickListener(this);
        FloatingActionButton fab_create_journey_post = (FloatingActionButton) CreateJourneyFragmentView.findViewById(R.id.fab_create_journey_post);
        fab_create_journey_post.setOnClickListener(this);
        FloatingActionButton fab_create_journey_search = (FloatingActionButton) CreateJourneyFragmentView.findViewById(R.id.fab_create_journey_search);
        fab_create_journey_search.setOnClickListener(this);

        return CreateJourneyFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkpointAdapter == null)
            checkpointAdapter = new CheckpointAdapter(mContext, checkpointList);
        checkpointAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(checkpointAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // populate this list with journey points
        locationPointList = new ArrayList<LocationPoint>();
        locationPointNameList = new ArrayList<CharSequence>();

        fetchDatafromServer("journey_points");
        fetchDatafromServer("user_info");

        if (mContext instanceof OnCreateJourneyFragmentInteractionListener) {
            mListener = (OnCreateJourneyFragmentInteractionListener) mContext;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateJourneyFragmentInteractionListener");
        }
    }

    public void fetchDatafromServer(String type) {
        // assumes that the base activity is home
        final Create baseCreateActivity = (Create) getActivity();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        serverURL + "/" + type + "/",
                        null,
                        this, this) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Token " + baseCreateActivity.mToken);
                return headers;
            }
        };
        baseCreateActivity.mQueue.add(jsonArrayRequest);
    }

    public void postDatatoServer(String type, JSONObject data) {
        // assumes that the base activity is home
        final Create baseCreateActivity = (Create) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        serverURL + "/" + type + "/",
                        data,
                        this, this) {
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
                if (((JSONArray) response).length() > 0) {
                    JSONObject firstElement = (JSONObject) ((JSONArray) response).get(0);
                    if (firstElement.has("user")) { // user object
                        originalPoster = new User(firstElement);
                    }
                    else { // list of locations
                        for (int i = 0; i < ((JSONArray) response).length(); i++) {
                            LocationPoint locationPoint = new LocationPoint(((JSONArray) response).getJSONObject(i));
                            locationPointList.add(locationPoint);
                            locationPointNameList.add(locationPoint.location_name);
                        }
                        ArrayAdapter<CharSequence> locationAdapter = new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_spinner_item, locationPointNameList);
                        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        create_journey_source_spinner.setAdapter(locationAdapter);
                        create_journey_destination_spinner.setAdapter(locationAdapter);
                    }
                }
            }
        } catch (JSONException e) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_journey_date_button:
                if (calendar == null)
                    calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                        String monthOfYearString = monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear;
                        //                        String yearString = year < 10 ? "0" + year : "" + year;
                        create_journey_date_button.setText(dayOfMonthString + "-" + monthOfYearString + "-" + year);
                    }
                }, calendar);
                datePickerDialog.show(getChildFragmentManager(),"Date Picker");
                break;

            case R.id.create_journey_time_button:
                if (calendar == null)
                    calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, second);
                        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                        String minuteString = minute < 10 ? "0" + minute : "" + minute;
                        String secondString = second < 10 ? "0" + second : "" + second;
                        create_journey_time_button.setText(hourString + ":" + minuteString + ":" + secondString);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show(getChildFragmentManager(),"Time Picker");
                break;

            case R.id.create_journey_add_checkpoint_button:
                Checkpoint checkpoint = new Checkpoint();
                checkpoint.source = create_journey_source_spinner.getSelectedItem().toString();
                checkpoint.transport = create_journey_mode_spinner.getSelectedItem().toString();
                checkpoint.destination = create_journey_destination_spinner.getSelectedItem().toString();
                checkpointList.add(checkpoint);

                checkpointAdapter = new CheckpointAdapter(mContext, checkpointList);
                checkpointAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(checkpointAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
                break;

            case R.id.fab_create_journey_post:
                if(!setJourneyObject())
                    break;
                // send journey object to server for post
                // send journey object to server for creation
                try {
                    Log.w("Journey", journey.toJSON().toString());
                    postDatatoServer("create_journey", journey.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.fab_create_journey_search:
                if(!setJourneyObject())
                    break;

                final Create baseCreateActivity = (Create) getActivity();
                Intent intent = new Intent(mContext,Create.class);
                intent.putExtra("type",JOURNEY_TRAVEL_TYPE);
                intent.putExtra("token",baseCreateActivity.mToken);
                intent.putExtra("journey",journey.journey_id);
                startActivityForResult(intent,REQUEST_SEARCH_TRAVEL);
                break;
        }
    }

    public LocationPoint searchLocationPointinArray(String key, ArrayList<LocationPoint> locationPointList) {
        for (LocationPoint l : locationPointList) {
            if (l.location_name.equals(key)) {
                return l;
            }
        }
        return null;
    }

    private boolean setJourneyObject(){
        journey.journey_id = create_journey_name.getText().toString();
        if(journey.journey_id == null || journey.journey_id == ""){
            Toast.makeText(getActivity(), "Enter a name for the journey", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(create_journey_date_button.getText().toString().equals("SELECT DATE")){
            Toast.makeText(getActivity(), "Enter a date for the journey", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(create_journey_time_button.getText().toString().equals("SELECT TIME")){
            Toast.makeText(getActivity(), "Enter a time for the journey", Toast.LENGTH_SHORT).show();
            return false;
        }
        journey.date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        journey.start_time = df.format(journey.date);

        if(checkpointList.isEmpty()){
            Toast.makeText(getActivity(), "Enter atleast on checkpoint for the journey", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(journey.checkpoints == null)
            journey.checkpoints = new ArrayList<JourneyPoint>();
        String destination = "";
        int i=0;
        for(Checkpoint checkpoint: checkpointList){
            JourneyPoint journeyPoint = new JourneyPoint();
            journeyPoint.point_id = Integer.toString(i);
            journeyPoint.location = searchLocationPointinArray(checkpoint.source, locationPointList);
            journeyPoint.transport = checkpoint.transport;
            journey.checkpoints.add(journeyPoint);
            destination = checkpoint.destination;
            i++;
        }
        JourneyPoint journeyPoint = new JourneyPoint();
        journeyPoint.point_id = Integer.toString(i);
        journeyPoint.location = searchLocationPointinArray(destination, locationPointList);
        journeyPoint.transport = "BUS";
        journey.checkpoints.add(journeyPoint);

        journey.source = journey.checkpoints.get(0).location.location_name;
        journey.destination = destination;

        journey.participants.add(originalPoster);

        return true;
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
