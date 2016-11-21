package net.itempire.donateblood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText etEmail, etPassword;
    TextView tvForgotPassword;
    Button btnSignup, btnLogin;
    SessionManager sessionManager;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        cm = (ConnectivityManager)LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        sessionManager = new SessionManager(getApplicationContext());

        Typeface roboto_light = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf" );
        Typeface roboto_medium = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf" );

        etEmail.setTypeface(roboto_light);
        etPassword.setTypeface(roboto_light);
        tvForgotPassword.setTypeface(roboto_light);
        btnLogin.setTypeface(roboto_medium);
        btnSignup.setTypeface(roboto_medium);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    String type = "login";

                    if (email.length() > 0 && password.length() > 0) {
                        BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                        backgroundWorker.execute(type, email, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "Please fill-in all fields!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Internet Connection Not Working", Toast.LENGTH_SHORT).show();
                }
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
        getMenuInflater().inflate(R.menu.signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signup) {
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
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
//            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
//            layoutContainer.removeAllViewsInLayout();
//            Fragment frag = new ProfileFragment();
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragmentContainer,frag); transaction.addToBackStack(null);
//            transaction.commit();
//        } else if (id == R.id.nav_request_donation) {
//            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
//            layoutContainer.removeAllViewsInLayout();
//            Fragment frag = new BloodGroupFragment();
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragmentContainer,frag); transaction.addToBackStack(null);
//            transaction.commit();
//        } else if (id == R.id.nav_volunteers_list) {
//            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
//            layoutContainer.removeAllViewsInLayout();
//            Fragment frag = new VolunteersListFragment();
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragmentContainer,frag); transaction.addToBackStack(null);
//            transaction.commit();
//        } else if (id == R.id.nav_view_donation_requests) {
//            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
//            layoutContainer.removeAllViewsInLayout();
//            Fragment frag = new DonationRequestsFragment();
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragmentContainer,frag); transaction.addToBackStack(null);
//            transaction.commit();
//        } else if (id == R.id.nav_featured_donors) {
//            RelativeLayout layoutContainer = (RelativeLayout) findViewById(R.id.fragmentContainer);
//            layoutContainer.removeAllViewsInLayout();
//            Fragment frag = new FeaturedDonorsFragment();
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragmentContainer,frag); transaction.addToBackStack(null);
//            transaction.commit();
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
            //String login_url = "http://10.1.1.10/bloodapp/index.php?display=";
            String login_url = "http://bloodapp.witorbit.net/index.php?display=";

            if(type.equals("login")){
                String email = params[1];
                String password = params[2];
                try {
                    URL url = new URL(login_url+"m_loginactivity");
                    //URL url = new URL(login_url+"login.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode(type,"UTF-8")+"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                            URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    String result = "";
                    String line = "";
                    while((line = bufferedReader.readLine())!=null){
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;

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
//            alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Login Status");
        }

        @Override
        protected void onPostExecute(String result) {
//            if(!result.equals("Error")){
//                String name, gender, age, bloodGroup, phoneNumber, city, email, password, thanks;
//                try {
//                    JSONObject userDetails = new JSONObject(result);
//                    Log.v("JSON Object",userDetails.getString("phoneNumber"));
//                    name = userDetails.getString("name");
//                    gender = userDetails.getString("gender");
//                    age = userDetails.getString("age");
//                    bloodGroup = userDetails.getString("bloodGroup");
//                    phoneNumber = userDetails.getString("phoneNumber");
//                    city = userDetails.getString("city");
//                    email = userDetails.getString("email");
//                    password = userDetails.getString("password");
//                    thanks = userDetails.getString("thanks");
//                    sessionManager.createLoginSession(name, gender, age, bloodGroup, phoneNumber, city, email, password, thanks);
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    LoginActivity.this.finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            if(!result.equals("null") && !result.equals("inactive") && !result.equals("Request Not found.")){
                String user_id, full_name, gender, age, blood_group, contact_no, city, email,
                        password, thanks, last_donation_data, reg_date;
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject userDetails = jsonArray.getJSONObject(0);

                    user_id = userDetails.getString("user_id");
                    full_name = userDetails.getString("full_name");
                    gender = userDetails.getString("gender");
                    age = userDetails.getString("age");
                    blood_group = userDetails.getString("blood_group");
                    contact_no = userDetails.getString("contact_no");
                    city = userDetails.getString("city");
                    email = userDetails.getString("email");
                    password = userDetails.getString("password");
                    thanks = userDetails.getString("thanks");
                    if(thanks.equals("")){
                        thanks = "0";
                    }
                    last_donation_data = userDetails.getString("last_donation_data");
                    reg_date = userDetails.getString("reg_date");
                    sessionManager.createLoginSession(user_id, full_name, gender, age, blood_group, contact_no, city, email, password, thanks, last_donation_data, reg_date);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    LoginActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "Error in Login! Try again.", Toast.LENGTH_SHORT).show();
            }
//            alertDialog.setMessage(result);
//            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
