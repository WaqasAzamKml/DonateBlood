package net.itempire.donateblood;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class BloodGroupFragment extends Fragment {
    SessionManager sessionManager;
    Button btnAPositive, btnANegative, btnBPositive, btnBNegative,btnOPositive, btnONegative,btnABPositive, btnABNegative;
    android.app.FragmentManager fragmentManager;
    String extra_message = "";
    String user_id, req_blood_group, type;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    TextView tvDrawerUsername, tvDrawerPhone;
    NavigationView navigationView;
    View headerView;

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

        if(!sessionManager.isLoggedIn()){
            String name = getString(R.string.txt_username);
            String phone = getString(R.string.txt_phone);
            tvDrawerPhone.setText(phone);
            tvDrawerUsername.setText(name);
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else {
            View v = inflater.inflate(R.layout.fragment_blood_group, container, false);

            fragmentManager = getFragmentManager();

            btnAPositive = (Button) v.findViewById(R.id.btnAPositive);
            btnANegative = (Button) v.findViewById(R.id.btnANegative);
            btnBPositive = (Button) v.findViewById(R.id.btnBPositive);
            btnBNegative = (Button) v.findViewById(R.id.btnBNegative);
            btnOPositive = (Button) v.findViewById(R.id.btnOPositive);
            btnONegative = (Button) v.findViewById(R.id.btnONegative);
            btnABPositive = (Button) v.findViewById(R.id.btnABPositive);
            btnABNegative = (Button) v.findViewById(R.id.btnABNegative);

            HashMap<String, String> userDetails = sessionManager.getUserDetails();
            user_id = userDetails.get(SessionManager.KEY_USER_ID);
            String name = userDetails.get(SessionManager.KEY_NAME);
            String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);

            tvDrawerUsername.setText(name);
            tvDrawerPhone.setText(phone);

            btnAPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnANegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnBPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnBNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnOPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnONegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnABPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });
            btnABNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog(v);
                }
            });

            return v;
        }
    }

    public void showMessageDialog( View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog,null);
        dialogBuilder.setView(dialogView);
        final View btnView = v;
        final EditText edt = (EditText) dialogView.findViewById(R.id.etMessage);
        type = "request";
        dialogBuilder.setTitle("Message");
        dialogBuilder.setMessage("Enter message below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(isConnected) {
                    extra_message = edt.getText().toString();
                    if(btnView.equals(btnAPositive)){
                        req_blood_group = btnAPositive.getText().toString();
                    }
                    else if(btnView.equals(btnANegative)){
                        req_blood_group = btnANegative.getText().toString();
                    }
                    else if (btnView.equals(btnBPositive)){
                        req_blood_group = btnBPositive.getText().toString();
                    }
                    else if (btnView.equals(btnBNegative)){
                        req_blood_group = btnBNegative.getText().toString();
                    }
                    else if (btnView.equals(btnABPositive)){
                        req_blood_group = btnABPositive.getText().toString();
                    }
                    else if (btnView.equals(btnABNegative)){
                        req_blood_group = btnABNegative.getText().toString();
                    }
                    else if (btnView.equals(btnOPositive)){
                        req_blood_group = btnOPositive.getText().toString();
                    }
                    else if (btnView.equals(btnONegative)){
                        req_blood_group = btnONegative.getText().toString();
                    }

                    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                    backgroundWorker.execute(type, user_id, req_blood_group, extra_message);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }

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

            if(type.equals("request")){
                String user_id = params[1];
                String req_blood_group = params[2];
                String extra_message = params[3];
                try {
                    URL url = new URL(login_url+"m_request");
                    //URL url = new URL(login_url+"login.php");

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode(type,"UTF-8")+"&"+
                            URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"+
                            URLEncoder.encode("r_blood","UTF-8")+"="+URLEncoder.encode(req_blood_group,"UTF-8")+"&"+
                            URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(extra_message,"UTF-8");

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
//            alertDialog.setTitle("Request Status");
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("done")){
                Toast.makeText(getActivity().getApplicationContext(), "Donation Request Sent", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new ProfileFragment()).addToBackStack(null).commit();
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Some Error Occurred! Try Again", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
