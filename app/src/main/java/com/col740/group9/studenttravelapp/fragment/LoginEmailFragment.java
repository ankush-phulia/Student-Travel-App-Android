package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.col740.group9.studenttravelapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginEmailFragment.OnLoginEmailFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginEmailFragment extends Fragment {

    private OnLoginEmailFragmentInteractionListener mListener;

    public LoginEmailFragment() {
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
        View LoginEmailFragmentView = inflater.inflate(R.layout.fragment_login_email, container, false);

        Button button_go_email = LoginEmailFragmentView.findViewById(R.id.button_go_email);
        button_go_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailDetails();
            }
        });

        return LoginEmailFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginEmailFragmentInteractionListener) {
            mListener = (OnLoginEmailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void sendEmailDetails() {
        EditText emailEditText = (EditText) getActivity().findViewById(R.id.Email_EditText);
        CharSequence emailID = emailEditText.getText();
        if (TextUtils.isEmpty(emailID) || !Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
            Toast.makeText(getActivity(), "Enter a valid email address and then press Go", Toast.LENGTH_SHORT).show();
        } else if (mListener != null) {
            mListener.onLoginEmailFragmentInteraction(emailID);
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
    public interface OnLoginEmailFragmentInteractionListener {

        void onLoginEmailFragmentInteraction(CharSequence emailID);
    }
}
