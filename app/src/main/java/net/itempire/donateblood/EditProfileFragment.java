package net.itempire.donateblood;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by MIRSAB on 12/12/2016
 */
public class EditProfileFragment extends Fragment {
    FragmentManager fragmentManager;
    SessionManager sessionManager;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    ProgressDialog dialog;
    NavigationView navigationView;
    View headerView;
    TextView tvDrawerUsername, tvDrawerPhone;
    EditText etName, etAge, etPhone, etEmail, etPassword;
    Spinner spnGender, spnBloodGroup, spnCity;
    Button btnUpdateProfile, btnEditName, btnEditGender, btnEditAge, btnEditBloodGroup, btnEditPhone, btnEditCity, btnEditEmail;
    String[] genderArray    = {"Male","Female"};
    String[] bGroupArray    = {"A+ve","A-ve","B+ve","B-ve","AB+ve","AB-ve","O+ve","O-ve"};
    String[] cityArray      = {"Karachi","Lahore","Faisalabad","Rawalpindi","Rahimyar Khan"};
    String user_id, name, gender, age, bloodGroup, phone, city, email, password, thanks, last_donation_data, reg_date;
    String n_gender, n_bloodGroup, n_city;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        tvDrawerPhone = (TextView) headerView.findViewById(R.id.tvDrawerPhone);
        tvDrawerUsername = (TextView) headerView.findViewById(R.id.tvDrawerUsername);

        fragmentManager = getFragmentManager();

        cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        user_id = userDetails.get(SessionManager.KEY_USER_ID);
        name = userDetails.get(SessionManager.KEY_NAME);
        phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);

        if(isConnected) {
            if (!sessionManager.isLoggedIn()) {
                name = getString(R.string.txt_username);
                phone = getString(R.string.txt_phone);
                tvDrawerPhone.setText(phone);
                tvDrawerUsername.setText(name);
                sessionManager.checkLogin();
                return inflater.inflate(R.layout.fragment_donation_requests, container, false);
            } else {


                etName = (EditText) v.findViewById(R.id.etName);
                etAge = (EditText) v.findViewById(R.id.etAge);
                etPhone = (EditText) v.findViewById(R.id.etPhoneNumber);
                etEmail = (EditText) v.findViewById(R.id.etEmail);
                etPassword = (EditText) v.findViewById(R.id.etPassword);

                spnBloodGroup = (Spinner) v.findViewById(R.id.spnBloodGroup);
                spnCity = (Spinner) v.findViewById(R.id.spnCity);
                spnGender = (Spinner) v.findViewById(R.id.spnGender);

                btnUpdateProfile = (Button) v.findViewById(R.id.btnUpdateProfile);
                btnEditName = (Button) v.findViewById(R.id.btnEditName);
                btnEditGender = (Button) v.findViewById(R.id.btnEditGender);
                btnEditAge = (Button) v.findViewById(R.id.btnEditAge);
                btnEditBloodGroup = (Button) v.findViewById(R.id.btnEditBloodGroup);
                btnEditPhone = (Button) v.findViewById(R.id.btnEditPhoneNumber);
                btnEditCity = (Button) v.findViewById(R.id.btnEditCity);
                btnEditEmail = (Button) v.findViewById(R.id.btnEditEmail);

                btnEditName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etName.setEnabled(true);
                    }
                });
                btnEditGender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spnGender.setEnabled(true);
                    }
                });
                btnEditAge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etAge.setEnabled(true);
                    }
                });
                btnEditBloodGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spnBloodGroup.setEnabled(true);
                    }
                });
                btnEditPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etPhone.setEnabled(true);
                    }
                });
                btnEditCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spnCity.setEnabled(true);
                    }
                });
                btnEditEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etEmail.setEnabled(true);
                    }
                });

                spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        n_gender = spnGender.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spnBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        n_bloodGroup = spnBloodGroup.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        n_city = spnCity.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                etName.setEnabled(false);
                etAge.setEnabled(false);
                etPhone.setEnabled(false);
                etEmail.setEnabled(false);

                ArrayAdapter<String> genderAdapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, genderArray);
                spnGender.setEnabled(false);
                spnGender.setAdapter(genderAdapter);

                ArrayAdapter<String> bGroupAdapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bGroupArray);
                spnBloodGroup.setEnabled(false);
                spnBloodGroup.setAdapter(bGroupAdapter);

                ArrayAdapter<String> cityAdapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityArray);
                spnCity.setEnabled(false);
                spnCity.setAdapter(cityAdapter);

                Typeface roboto_light = Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto-Light.ttf");
                Typeface roboto_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Roboto-Medium.ttf");

                etName.setTypeface(roboto_light);
                etAge.setTypeface(roboto_light);
                etEmail.setTypeface(roboto_light);
                etPhone.setTypeface(roboto_light);
                etPassword.setTypeface(roboto_light);

                btnUpdateProfile.setTypeface(roboto_medium);

                age = userDetails.get(SessionManager.KEY_AGE);
                email = userDetails.get(SessionManager.KEY_EMAIL);
                password = userDetails.get(SessionManager.KEY_PASSWORD);
                bloodGroup = userDetails.get(SessionManager.KEY_BLOOD_GROUP);
                gender = userDetails.get(SessionManager.KEY_GENDER);
                city = userDetails.get(SessionManager.KEY_CITY);

                etName.setHint("Name: " + name);
                int genderIndex = 0;
                for (int i = 0; i < genderArray.length; i++) {
                    if (genderArray[i].equals(gender)) {
                        genderIndex = i;
                        break;
                    }
                }
                spnGender.setSelection(genderIndex);
                etAge.setHint("Age: " + age);
                int bloodIndex = 0;
                for (int i = 0; i < bGroupArray.length; i++) {
                    if (bGroupArray[i].equals(bloodGroup)) {
                        bloodIndex = i;
                        break;
                    }
                }
                spnBloodGroup.setSelection(bloodIndex);
                etPhone.setHint("Phone: " + phone);
                int cityIndex = 0;
                for (int i = 0; i < cityArray.length; i++) {
                    if (cityArray[i].equals(city)) {
                        cityIndex = i;
                        break;
                    }
                }
                spnCity.setSelection(cityIndex, true);
                etEmail.setHint("Email: " + email);

                btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String pwd = etPassword.getText().toString();
                        if(!pwd.equals("")){
                            if(etName.isEnabled() && etName.getText().toString()!=null && !etName.getText().toString().equals("")){
                                name = etName.getText().toString();
                            }
                            else if(spnGender.isEnabled() && !n_gender.equals(gender)){
                                gender = spnGender.getSelectedItem().toString();
                            }
                            else if(etAge.isEnabled() && etAge.getText().toString()!=null && !etAge.getText().toString().equals("")){
                                age = etAge.getText().toString();
                            }
                            else if(spnBloodGroup.isEnabled()){
                                bloodGroup = spnBloodGroup.getSelectedItem().toString();
                            }
                            else if(etPhone.isEnabled() && etPhone.getText().toString()!=null && !etPhone.getText().toString().equals("")){
                                phone = etPhone.getText().toString();
                            }
                            else if(spnCity.isEnabled()){
                                city = spnCity.getSelectedItem().toString();
                            }
                            else if(etEmail.isEnabled() && etEmail.getText().toString()!=null && !etEmail.getText().toString().equals("")){
                                email = etEmail.getText().toString();
                            }
                            else{
                                Toast.makeText(getActivity(), "You didn't make any changes.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                            dialogBuilder.setTitle("Sure to Update Profile?");
                            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new JSONAsyncTask().execute("http://bloodapp.witorbit.net/index.php?display=m_update_profile&update&user_id="
                                            +user_id+"&full_name="+name+"&gender="+gender+"&age="
                                            +age+"&blood_group="+bloodGroup+"&contact_no="+phone
                                            +"&city="+city+"&email="+email+"&password="+pwd);
                                }
                            });
                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //pass
                                }
                            });
                            AlertDialog b = dialogBuilder.create();
                            b.show();

                        }
                        else{
                            Toast.makeText(getActivity(), "Please enter your password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return v;
            }
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
            if(!sessionManager.isLoggedIn()){
                name = getString(R.string.txt_username);
                phone = getString(R.string.txt_phone);
                tvDrawerPhone.setText(phone);
                tvDrawerUsername.setText(name);
                return inflater.inflate(R.layout.fragment_donation_requests, container, false);
            }
            else {
                tvDrawerUsername.setText(name);
                tvDrawerPhone.setText(phone);
                return v;
            }
        }
    }

    /*******
     * Inner BackgroundWorker Class
     *******/
    class JSONAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                //------------------>>
                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpGet);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    if(data.equals("null")){
                        return "null";
                    }
                    if(data.equals("request error!")){
                        return "request error!";
                    }
                    if(data.equals("password not match")){
                        return "password not match";
                    }
                    JSONArray jsonArray = new JSONArray(data);
                    int arraySize = jsonArray.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        user_id = object.getString("user_id");
                        name = object.getString("full_name");
                        gender = object.getString("gender");
                        age = object.getString("age");
                        bloodGroup = object.getString("blood_group");
                        phone = object.getString("contact_no");
                        city = object.getString("city");
                        email = object.getString("email");
                        password = object.getString("password");
                        thanks = object.getString("thanks");
                        if(thanks.equals("")){
                            thanks = "0";
                        }
                        last_donation_data = object.getString("last_donation_data");
                        reg_date = object.getString("reg_date");
                        sessionManager.createLoginSession(user_id, name, gender, age, bloodGroup, phone, city, email, password, thanks, last_donation_data, reg_date);
                    }
                    Log.d("Result","True");
                    return "success";
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Result","False");
            return "request error!";
        }

        protected void onPostExecute(String result) {
            dialog.cancel();
            if(result.equals("success")) {
                Toast.makeText(getActivity(), "Updated profile successfully.", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).addToBackStack(null).commit();
            }
            else if(result.equals("null")){
                Toast.makeText(getActivity(), "Error updating. Try again!", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("password not match")){
                Toast.makeText(getActivity(), "Password Error! Try again.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }

        }
    } // End of inner BackgroundWorker class

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
