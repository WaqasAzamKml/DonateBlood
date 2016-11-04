package net.itempire.donateblood;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class ProfileFragment extends Fragment {
    SessionManager sessionManager;
    TextView tvProfileName, tvThumbsCount, tvBloodGroup, tvContact, tvAddress;
    Button btnEditProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else {

            return inflater.inflate(R.layout.fragment_profile, container, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvProfileName = (TextView) getView().findViewById(R.id.tvProfileName);
        tvThumbsCount = (TextView) getView().findViewById(R.id.tvThumbsCount);
        tvBloodGroup = (TextView) getView().findViewById(R.id.tvBloodGroup);
        tvContact = (TextView) getView().findViewById(R.id.tvContact);
        tvAddress = (TextView) getView().findViewById(R.id.tvAddress);

        btnEditProfile = (Button) getView().findViewById(R.id.btnEditProfile);

        Typeface roboto_light = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf" );
        Typeface roboto_medium = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Medium.ttf" );

        tvProfileName.setTypeface(roboto_medium);
        tvThumbsCount.setTypeface(roboto_medium);
        tvBloodGroup.setTypeface(roboto_medium);
        tvContact.setTypeface(roboto_light);
        tvAddress.setTypeface(roboto_light);

        btnEditProfile.setTypeface(roboto_medium);

        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String name = userDetails.get(SessionManager.KEY_NAME);
        String thanks = userDetails.get(SessionManager.KEY_THANKS);
        String bloodGroup = userDetails.get(SessionManager.KEY_BLOOD_GROUP);
        String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);
        String city = userDetails.get(SessionManager.KEY_CITY);

        Log.d("Test onAttach",name);
        tvProfileName.setText(name);
        tvThumbsCount.setText(thanks);
        tvBloodGroup.setText(bloodGroup);
        tvContact.setText(phone);
        tvAddress.setText(city);
    }
}
