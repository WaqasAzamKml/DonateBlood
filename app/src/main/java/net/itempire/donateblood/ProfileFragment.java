package net.itempire.donateblood;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
    TextView tvProfileName, tvThumbsCount, tvBloodGroup, tvContact, tvAddress, tvDrawerUsername, tvDrawerPhone;
    Button btnEditProfile;
    NavigationView navigationView;
    View headerView;
    FragmentManager fragmentManager;
    //String name, thanks, bloodGroup, phone, city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvDrawerPhone = (TextView) headerView.findViewById(R.id.tvDrawerPhone);
        tvDrawerUsername = (TextView) headerView.findViewById(R.id.tvDrawerUsername);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();

        if(!sessionManager.isLoggedIn()){
            String name = getString(R.string.txt_username);
            String phone = getString(R.string.txt_phone);
            tvDrawerPhone.setText(phone);
            tvDrawerUsername.setText(name);
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else {
            View v = inflater.inflate(R.layout.fragment_profile, container, false);
            tvProfileName = (TextView) v.findViewById(R.id.tvProfileName);
            tvThumbsCount = (TextView) v.findViewById(R.id.tvThumbsCount);
            tvBloodGroup = (TextView) v.findViewById(R.id.tvBloodGroup);
            tvContact = (TextView) v.findViewById(R.id.tvContact);
            tvAddress = (TextView) v.findViewById(R.id.tvAddress);


            btnEditProfile = (Button) v.findViewById(R.id.btnEditProfile);

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
            tvDrawerUsername.setText(name);
            tvDrawerPhone.setText(phone);

            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new EditProfileFragment()).addToBackStack(null).commit();
                }
            });

            return v;
        }
    }
}
