package com.col740.group9.studenttravelapp.activity;

import static com.col740.group9.studenttravelapp.classes.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.col740.group9.studenttravelapp.R;
import com.col740.group9.studenttravelapp.classes.GlobalRequestQueue;
import com.col740.group9.studenttravelapp.fragment.LoginEmailFragment;
import com.col740.group9.studenttravelapp.fragment.LoginOTPFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class Login extends AppCompatActivity
        implements View.OnClickListener,
                LoginEmailFragment.OnLoginEmailFragmentInteractionListener,
                LoginOTPFragment.OnLoginOTPFragmentInteractionListener,
                Response.Listener<JSONObject>,
                Response.ErrorListener {

    String mUser;
    String mPass;

    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 9001;
    public static final int EMAIL = 0;
    public static final int OTP = 1;
    public int screen_state = EMAIL;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]

        screen_state = EMAIL;
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) loginViaGoogle(account);
        // [END on_start_sign_in]
    }

    @Override
    protected void onResume() {
        Fragment LoginFragment = null;
        if (screen_state == EMAIL) 
            LoginFragment = new LoginEmailFragment();
        else 
            LoginFragment = new LoginOTPFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_relativeLayout, LoginFragment)
                .addToBackStack(null)
                .commit();

        super.onResume();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            loginViaGoogle(account);
        } 
        catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code =" + e.getStatusCode());
            loginViaGoogle(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    private void loginViaGoogle(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            // Take the Google account details and call login
            mUser = account.getEmail();
            mPass = mUser.charAt(0) + "000000";
            login(mUser, mPass);
        } 
        else {
            Toast.makeText(
                            this,
                            "Unable to login to Google Account. Try Again !",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                try {
                    dummySignIn();
                } 
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void dummySignIn() throws JSONException {

        RequestQueue queue =
                GlobalRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", "ankush@gmail.com").put("password", "a1234567");

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.POST, serverURL + "/api-token-auth/", jsonBody, this, this);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } 
        else {
            // super.onBackPressed();
        }
    }

    public void onLoginEmailFragmentInteraction(CharSequence emailID) {
        // TODO : send a six digit otp to the Email ID
        mUser = emailID.toString();

        Fragment loginOTPFragment = new LoginOTPFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.login_relativeLayout, loginOTPFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        screen_state = OTP;
    }

    public void onLoginOTPFragmentInteraction(CharSequence otp) {
        // Take the authentication detail and call login
        mPass = mUser.charAt(0) + otp.toString();
        login(mUser, mPass);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            if (response.has("token")) {
                String token = (String) response.get("token");

                Intent gotoHome = new Intent(Login.this, Home.class);
                gotoHome.putExtra("token", token);
                startActivity(gotoHome);
                finish();
            }
        } 
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void login(String email, String pass) {
        // Create new user account or open Dashboard if account present already
        try {
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", email).put("password", pass);

            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(
                            Request.Method.POST,
                            serverURL + "/api-token-auth/",
                            jsonBody,
                            this,
                            this);
            GlobalRequestQueue.getInstance(this.getApplicationContext())
                    .getRequestQueue()
                    .add(jsonObjectRequest);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
