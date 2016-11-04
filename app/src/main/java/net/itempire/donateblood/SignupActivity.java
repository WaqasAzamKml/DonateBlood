package net.itempire.donateblood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText etName, etAge, etEmail, etPhone, etPassword, etRepeatPassword;
    Spinner spnGender;
    Spinner spnBloodGroup;
    Spinner spnCity;
    Button btnLogin, btnSignup;

    String[] genderArray    = {"Male","Female"};
    String[] bGroupArray    = {"A+ve","A-ve","B+ve","B-ve","AB+ve","AB-ve","O+ve","O-ve"};
    String[] cityArray      = {"Karachi","Lahore","Faisalabad","Rawalpindi","Rahimyar Khan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar         = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer     = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        etName              = (EditText) findViewById(R.id.etName);
        etAge               = (EditText) findViewById(R.id.etAge);
        etEmail             = (EditText) findViewById(R.id.etEmail);
        etPhone             = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword          = (EditText) findViewById(R.id.etPassword);
        etRepeatPassword    = (EditText) findViewById(R.id.etRepeatPassword);

        spnGender           = (Spinner) findViewById(R.id.spnGender);
        spnBloodGroup       = (Spinner) findViewById(R.id.spnBloodGroup);
        //spnAge            = (Spinner) findViewById(R.id.spnAge);
        spnCity             = (Spinner) findViewById(R.id.spnCity);

        btnLogin            = (Button) findViewById(R.id.btnLogin);
        btnSignup           = (Button) findViewById(R.id.btnSignup);

        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,genderArray);
        spnGender.setAdapter(genderAdapter);

        ArrayAdapter<String> bGroupAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,bGroupArray);
        spnBloodGroup.setAdapter(bGroupAdapter);

        ArrayAdapter<String> cityAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cityArray);
        spnCity.setAdapter(cityAdapter);

        Typeface roboto_light = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf" );
        Typeface roboto_medium = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf" );

        etName.setTypeface(roboto_light);
        etAge.setTypeface(roboto_light);
        etEmail.setTypeface(roboto_light);
        etPhone.setTypeface(roboto_light);
        etPassword.setTypeface(roboto_light);
        etRepeatPassword.setTypeface(roboto_light);

        btnLogin.setTypeface(roboto_medium);
        btnSignup.setTypeface(roboto_medium);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String name             = etName.getText().toString();
//                String gender           = spnGender.getSelectedItem().toString();
//                String age              = etAge.getText().toString();
//                String bloodGroup       = spnBloodGroup.getSelectedItem().toString();
//                String phoneNumber      = etPhone.getText().toString();
//                String city             = spnCity.getSelectedItem().toString();
//                String email            = etEmail.getText().toString();
//                String password         = etPassword.getText().toString();
//                String repeatPassword   = etRepeatPassword.getText().toString();
//                String type             = "register";
//
//                if(!password.equals(repeatPassword)){
//                    Toast.makeText(SignupActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                BackgroundWorker backgroundWorker = new BackgroundWorker(SignupActivity.this);
//                backgroundWorker.execute(type, name, gender, age, bloodGroup, phoneNumber, city, email, password);
//                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_profile) {
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new ProfileFragment()).commit();
//        } else if (id == R.id.nav_request_donation) {
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new BloodGroupFragment()).commit();
//        } else if (id == R.id.nav_volunteers_list) {
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new VolunteersListFragment()).commit();
//        } else if (id == R.id.nav_view_donation_requests) {
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new DonationRequestsFragment()).commit();
//        } else if (id == R.id.nav_featured_donors) {
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new FeaturedDonorsFragment()).commit();
//        } else if (id == R.id.nav_blood_banks) {
//
//        } else if (id == R.id.nav_emergency_numbers) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*******
     * Inner BackgroundWorker Class
     *******/
    class BackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        BackgroundWorker(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            //String login_url = "http://192.168.8.105/android/";
            //String login_url = "http://172.18.15.82/android/";
            //String login_url = "http://waqasazam.com/android/";
            String login_url = "http://10.1.1.10/bloodapp/index.php?display=";

            if(type.equals("register")){
                String name             = params[1];
                String gender           = params[2];
                String age              = params[3];
                String bloodGroup       = params[4];
                String phoneNumber      = params[5];
                String city             = params[6];
                String email            = params[7];
                String password         = params[8];
                try {

                    URL url = new URL(login_url+"register.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                            URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"+
                            URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"+
                            URLEncoder.encode("bloodGroup","UTF-8")+"="+URLEncoder.encode(bloodGroup,"UTF-8")+"&"+
                            URLEncoder.encode("phoneNumber","UTF-8")+"="+URLEncoder.encode(phoneNumber,"UTF-8")+"&"+
                            URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    inputStream.close();
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//
//                String result = "";
//                String line = "";
//                while((line = bufferedReader.readLine())!=null){
//                    result += line;
//                }
//
//                bufferedReader.close();
//                httpURLConnection.disconnect();

                    return "Registration Successful";

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");
        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.setMessage(result);
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
