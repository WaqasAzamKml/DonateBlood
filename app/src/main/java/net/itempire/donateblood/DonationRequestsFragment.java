package net.itempire.donateblood;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class DonationRequestsFragment extends Fragment {
    SessionManager sessionManager;
    TextView tvProfileName, tvThumbsCount, tvBloodGroup, tvContact, tvAddress, tvDrawerUsername, tvDrawerPhone;
    Button btnEditProfile;
    NavigationView navigationView;
    View headerView;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String user_id = userDetails.get(SessionManager.KEY_USER_ID);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String name = userDetails.get(SessionManager.KEY_NAME);
        String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);

        if(isConnected){
            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
            backgroundWorker.execute(user_id,email);
        }
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

            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
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
            //String type = params[0];
            //String login_url = "http://192.168.8.105/android/";
            //String login_url = "http://172.18.15.82/android/";
            //String login_url = "http://waqasazam.com/android/";
            //String login_url = "http://10.1.1.10/bloodapp/index.php?display=";
            String login_url = "http://bloodapp.witorbit.net/index.php?display=";
            String user_id = params[0];
            String email = params[1];
            if(user_id != null){
                try {
                    URL url = new URL(login_url+"m_request&check_user");
                    //URL url = new URL(login_url+"login.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"+
                            URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");

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
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Request Status");
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
