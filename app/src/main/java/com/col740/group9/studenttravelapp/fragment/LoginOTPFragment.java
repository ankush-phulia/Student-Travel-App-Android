package com.col740.group9.studenttravelapp.fragment;

import android.content.Context;
import android.net.Uri;
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
 * {@link LoginOTPFragment.OnLoginOTPFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginOTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginOTPFragment extends Fragment {

    private static final int OTP_LENGTH = 6;


    private OnLoginOTPFragmentInteractionListener mListener;
    private View LoginOTPFragmentView;

    public LoginOTPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginOTPFragment.
     */
    public static LoginOTPFragment newInstance() {
        LoginOTPFragment fragment = new LoginOTPFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LoginOTPFragmentView = inflater.inflate(R.layout.fragment_login_otp, container, false);

        Button button_go_otp = LoginOTPFragmentView.findViewById(R.id.button_go_otp);
        button_go_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTPDetails();
            }
        });

        return LoginOTPFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginOTPFragmentInteractionListener) {
            mListener = (OnLoginOTPFragmentInteractionListener) context;
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

    private void sendOTPDetails(){
        EditText otpEditText = (EditText) getActivity().findViewById(R.id.OTP_EditText);
        CharSequence otp = otpEditText.getText();
        if(TextUtils.isEmpty(otp) || otp.length() != OTP_LENGTH || !TextUtils.isDigitsOnly(otp)){
            Toast.makeText(getActivity(), "Enter a valid otp and then press Go", Toast.LENGTH_SHORT).show();
        }
        else if (mListener != null) {
            mListener.onLoginOTPFragmentInteraction(otp);
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
    public interface OnLoginOTPFragmentInteractionListener {

        void onLoginOTPFragmentInteraction(CharSequence otp);
    }
}
