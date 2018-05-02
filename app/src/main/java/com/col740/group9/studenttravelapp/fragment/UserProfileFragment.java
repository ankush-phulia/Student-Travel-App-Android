package com.col740.group9.studenttravelapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.*;
import static com.col740.group9.studenttravelapp.classes.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnUserProfileFragmentInteractionListener} interface
 * to handle interaction events.
 */

public class UserProfileFragment extends Fragment
        implements Response.Listener, Response.ErrorListener {

    private OnUserProfileFragmentInteractionListener mListener;

    private boolean image_changed = false;

    private User user; // To be used between server and app
    private View UserProfileFragmentView;
    private TextView user_profile_name;
    private EditText user_profile_bio, user_profile_first_name, user_profile_last_name, user_profile_sex, user_profile_phone, user_profile_facebook_link;
    private CircleImageView user_profile_image;

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
        user_profile_bio = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_bio);
        user_profile_first_name = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_first_name);
        user_profile_last_name = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_last_name);
        user_profile_sex = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_sex);
        user_profile_phone = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_phone);
        user_profile_facebook_link = (EditText) UserProfileFragmentView.findViewById(R.id.user_profile_facebook_link);

        user_profile_image = (CircleImageView) UserProfileFragmentView.findViewById(R.id.user_profile_image);
        user_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MY_BUILD_VERSION > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (!checkIfAlreadyhavePermission()) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, REQUEST_LOAD_IMAGE);
                    }
                }

            }
        });

        FloatingActionButton fab_save = (FloatingActionButton) UserProfileFragmentView.findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.bio = user_profile_bio.getText().toString();
                user.first_name = user_profile_first_name.getText().toString();
                user.last_name = user_profile_last_name.getText().toString();
                user.sex = user_profile_sex.getText().toString();
                user.phone = user_profile_phone.getText().toString();
                user.facebook_link = user_profile_facebook_link.getText().toString();

                try {
                    JSONObject userJSON = user.toJSON();
                    // upload image from user_profile_image if image_changed == true
                    if (image_changed) {
                        user_profile_image.buildDrawingCache();
                        Bitmap bmp = user_profile_image.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        userJSON.put("photo", encodedImage);
                        Log.w("Profile", "Image");
                    }
                    postDatatoServer("update_user_info", userJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                image_changed = false;
            }
        });

        return UserProfileFragmentView;
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

    public void postDatatoServer(String type, JSONObject data) {
        // assumes that the base activity is home
        final Home baseHomeActivity = (Home) getActivity();

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest
                (Request.Method.POST,
                        serverURL + "/" + type + "/",
                        data,
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
    public void onResponse(Object response) {
        try {
            if (response.getClass() == JSONArray.class) {
                if (((JSONArray) response).length() > 0) {
                    JSONObject firstElement = (JSONObject) ((JSONArray) response).get(0);
                    Log.w("User", response.toString());
                    user = new User(firstElement);

                    user_profile_name.setText(user.username);
                    user_profile_bio.setText(user.bio);
                    user_profile_first_name.setText(user.first_name);
                    user_profile_last_name.setText(user.last_name);
                    user_profile_sex.setText(user.sex);
                    user_profile_phone.setText(user.phone);
                    user_profile_facebook_link.setText(user.facebook_link);

                    // load image from server and load it to bitmap
                    byte[] decodedString = Base64.decode(user.photo, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    if (bitmap != null)
                        user_profile_image.setImageBitmap(bitmap);
                }
            }
            Log.w("User Info", response.toString());
        } catch (JSONException e) {
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

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_LOAD_IMAGE);
                } else {
                    Toast.makeText(getActivity(), "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
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
