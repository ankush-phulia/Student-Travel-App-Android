package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.activity.Home;
import com.col740.group9.studenttravelapp.classes.User;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.col740.group9.studenttravelapp.classes.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnUserProfileFragmentInteractionListener} interface
 * to handle interaction events.
 */

public class UserProfileFragment extends Fragment
        implements Response.Listener, Response.ErrorListener{

    private OnUserProfileFragmentInteractionListener mListener;

    private static int RESULT_LOAD_IMAGE = 1;
    private boolean image_changed = false;

    private User user; // To be used between server and app
    private View UserProfileFragmentView;
    private TextView user_profile_name,user_profile_email;
    private EditText user_profile_first_name,user_profile_last_name,user_profile_sex,user_profile_facebook_link;
    private ImageView user_profile_image;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        image_changed = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UserProfileFragmentView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        user_profile_name = (TextView) UserProfileFragmentView.findViewById(R.id.user_profile_name);
        user_profile_email = (TextView) UserProfileFragmentView.findViewById(R.id.user_profile_email);
        user_profile_first_name = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_first_name);
        user_profile_last_name = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_last_name);
        user_profile_sex = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_sex);
        user_profile_facebook_link = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_facebook_link);

        user_profile_image = (ImageView) UserProfileFragmentView.findViewById(R.id.user_profile_image);
        user_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        FloatingActionButton fab_save = (FloatingActionButton) UserProfileFragmentView.findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.first_name = user_profile_first_name.getText().toString();
                user.last_name = user_profile_last_name.getText().toString();
                user.sex = user_profile_sex.getText().toString();
                user.facebook_link = user_profile_facebook_link.getText().toString();

                // TODO upload image from user_profile_image if image_changed == true
                try {
                    postDatatoServer("/user_info_update/", user.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                image_changed = false;
            }
        });

        return UserProfileFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            user_profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            image_changed = true;
        }
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
                String type = "";
                if (((JSONArray)response).length() > 0) {
                    JSONObject firstElement = (JSONObject) ((JSONArray)response).get(0);
                    if (firstElement.has("username")) {
                        type = "user_info";
                    }
                    Log.w("User", response.toString());
                    user = new User(firstElement);

                    user_profile_name.setText(user.username);
                    user_profile_email.setText(user.email);
                    user_profile_first_name.setText(user.first_name);
                    user_profile_last_name.setText(user.last_name);
                    user_profile_sex.setText(user.sex);
                    user_profile_facebook_link.setText(user.facebook_link);

                    // TODO load image from server and load it to bitmap
                    Bitmap bitmap = null;

                    if(bitmap != null)
                        user_profile_image.setImageBitmap(bitmap);
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
