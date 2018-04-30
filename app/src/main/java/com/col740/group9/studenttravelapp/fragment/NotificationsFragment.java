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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.Notification;
import static com.col740.group9.studenttravelapp.classes.Constants.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnNotificationsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotificationsFragment extends Fragment
        implements Response.Listener, Response.ErrorListener {

    private OnNotificationsFragmentInteractionListener mListener;
    private ArrayList<Notification> notificationList;
    private NotificationAdapter notificationAdapter;
    private View NotificationsFragmentView;
    private Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    public NotificationsFragment() {
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
        NotificationsFragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        mRecyclerView = (RecyclerView) NotificationsFragmentView.findViewById(R.id.notification_card_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);

        return NotificationsFragmentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mContext = this.getActivity();
        if(notificationAdapter == null)
            notificationAdapter = new NotificationAdapter(mContext,notificationList);
        notificationAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(notificationAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // populates this list from server
        notificationList = new ArrayList<Notification>();
        fetchDatafromServer("notifications");

        if (context instanceof OnNotificationsFragmentInteractionListener) {
            mListener = (OnNotificationsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNotificationsFragmentInteractionListener");
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
                        serverURL + "/" + type + "/",
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
        Log.w("Note", data.toString());
        final Home baseHomeActivity = (Home) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        serverURL + "/" + type + "/",
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
                Log.w("Notifications", response.toString());
                for (int i = 0; i < ((JSONArray)response).length(); i++) {
                    notificationList.add(new Notification(((JSONArray)response).getJSONObject(i)));
                }
                notificationAdapter = new NotificationAdapter(mContext, notificationList);
                notificationAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(notificationAdapter);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
    public interface OnNotificationsFragmentInteractionListener {
        void onNotificationsFragmentInteraction(Uri uri);
    }

    public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private ArrayList<Notification> notificationList;

        public class NotificationCardTravelViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card
            TextView title,description,date;
            Button accept_button,reject_button;

            public NotificationCardTravelViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                title = view.findViewById(R.id.notification_card_travel_title);
                description = view.findViewById(R.id.notification_card_travel_description);
                date = view.findViewById(R.id.notification_card_travel_date);
                accept_button = view.findViewById(R.id.notification_card_travel_accept_button);
                reject_button = view.findViewById(R.id.notification_card_travel_reject_button);
            }
        }

        public class NotificationCardLogisticsViewHolder extends RecyclerView.ViewHolder {
            // Declare Views objects present inside the card
            TextView title,description,date;

            public NotificationCardLogisticsViewHolder(View view) {
                super(view);
                // Populate View objects from layout
                title = view.findViewById(R.id.notification_card_logistics_title);
                description = view.findViewById(R.id.notification_card_logistics_description);
                date = view.findViewById(R.id.notification_card_logistics_date);
            }
        }


        public NotificationAdapter(Context mContext, ArrayList<Notification> notificationList) {
            this.mContext = mContext;
            this.notificationList = notificationList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            RecyclerView.ViewHolder viewHolder = null;
            switch (viewType) {
                case 0:
                    view = View.inflate(parent.getContext(), R.layout.notification_card_travel, null);
                    viewHolder = new NotificationCardTravelViewHolder(view);
                    break;
                case 1:
                    view = View.inflate(parent.getContext(), R.layout.notification_card_logistics, null);
                    viewHolder = new NotificationCardLogisticsViewHolder(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            final Notification notification = notificationList.get(position);
            switch (viewType) {
                case 0:
                    NotificationCardTravelViewHolder notificationCardTravelViewHolder = (NotificationCardTravelViewHolder) holder;
                    notificationCardTravelViewHolder.title.setText(notification.title);
                    notificationCardTravelViewHolder.date.setText(notification.date);
                    notificationCardTravelViewHolder.description.setText(notification.description);

                    notificationCardTravelViewHolder.accept_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // api call to server to accept the request
                            try {
                                postDatatoServer("accept_request", notification.toJSON());
                                fetchDatafromServer("notifications");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    notificationCardTravelViewHolder.reject_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // api call to server to reject the request
                            try {
                                postDatatoServer("reject_request", notification.toJSON());
                                fetchDatafromServer("notifications");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 1:
                    NotificationCardLogisticsViewHolder notificationCardLogisticsViewHolder = (NotificationCardLogisticsViewHolder) holder;
                    notificationCardLogisticsViewHolder.title.setText(notification.title);
                    notificationCardLogisticsViewHolder.date.setText(notification.date);
                    notificationCardLogisticsViewHolder.description.setText(notification.description);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (notificationList.get(position).type == "Logistics Related")
                return 1;
            else
                return 0;
        }
    }
}
