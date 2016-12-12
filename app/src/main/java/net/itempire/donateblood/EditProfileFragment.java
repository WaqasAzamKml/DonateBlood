package net.itempire.donateblood;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by MIRSAB on 12/12/2016
 */
public class EditProfileFragment extends Fragment {
    SessionManager sessionManager;
    NavigationView navigationView;
    View headerView;
    TextView tvDrawerUsername, tvDrawerPhone;
    EditText etName, etAge, etPhone, etEmail, etPassword;
    Spinner spnGender, spnBloodGroup, spnCity;
    Button btnUpdateProfile;
    String[] genderArray    = {"Male","Female"};
    String[] bGroupArray    = {"A+ve","A-ve","B+ve","B-ve","AB+ve","AB-ve","O+ve","O-ve"};
    String[] cityArray      = {"Karachi","Lahore","Faisalabad","Rawalpindi","Rahimyar Khan"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvDrawerPhone = (TextView) headerView.findViewById(R.id.tvDrawerPhone);
        tvDrawerUsername = (TextView) headerView.findViewById(R.id.tvDrawerUsername);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!sessionManager.isLoggedIn()){
            String name = getString(R.string.txt_username);
            String phone = getString(R.string.txt_phone);
            tvDrawerPhone.setText(phone);
            tvDrawerUsername.setText(name);
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else{
            View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

            etName = (EditText) v.findViewById(R.id.etName);
            etAge = (EditText) v.findViewById(R.id.etAge);
            etPhone = (EditText) v.findViewById(R.id.etPhoneNumber);
            etEmail = (EditText) v.findViewById(R.id.etEmail);
            etPassword = (EditText) v.findViewById(R.id.etPassword);

            spnBloodGroup = (Spinner) v.findViewById(R.id.spnBloodGroup);
            spnCity = (Spinner) v.findViewById(R.id.spnCity);
            spnGender = (Spinner) v.findViewById(R.id.spnGender);

            btnUpdateProfile = (Button) v.findViewById(R.id.btnUpdateProfile);

            ArrayAdapter<String> genderAdapter =
                    new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,genderArray);
            spnGender.setAdapter(genderAdapter);

            ArrayAdapter<String> bGroupAdapter =
                    new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,bGroupArray);
            spnBloodGroup.setAdapter(bGroupAdapter);

            ArrayAdapter<String> cityAdapter =
                    new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,cityArray);
            spnCity.setAdapter(cityAdapter);

            Typeface roboto_light = Typeface.createFromAsset(getResources().getAssets(),"fonts/Roboto-Light.ttf" );
            Typeface roboto_medium = Typeface.createFromAsset(getResources().getAssets(),"fonts/Roboto-Medium.ttf" );

            etName.setTypeface(roboto_light);
            etAge.setTypeface(roboto_light);
            etEmail.setTypeface(roboto_light);
            etPhone.setTypeface(roboto_light);
            etPassword.setTypeface(roboto_light);

            HashMap<String, String> userDetails = sessionManager.getUserDetails();
            String name = userDetails.get(SessionManager.KEY_NAME);
            String age = userDetails.get(SessionManager.KEY_AGE);
            String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);
            String email = userDetails.get(SessionManager.KEY_EMAIL);
            String password = userDetails.get(SessionManager.KEY_PASSWORD);
            String bloodGroup = userDetails.get(SessionManager.KEY_BLOOD_GROUP);
            String gender = userDetails.get(SessionManager.KEY_GENDER);
            String city = userDetails.get(SessionManager.KEY_CITY);

            etName.setHint("Name: "+name);
            etAge.setHint("Age: "+age);
            etPhone.setHint("Phone: "+phone);
            etEmail.setHint("Email: "+email);

            return v;
        }
    }
}
