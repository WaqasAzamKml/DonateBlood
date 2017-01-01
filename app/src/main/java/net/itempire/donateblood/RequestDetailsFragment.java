package net.itempire.donateblood;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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
import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class RequestDetailsFragment extends Fragment {
    String username, req_blood_group, user_contact, req_city, message, user_id;
    int request_id;
    TextView tvUsername, tvBloodGroup, tvContact, tvCity, tvMessage;
    ProgressDialog dialog;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    SessionManager sessionManager;
    Button btnDonate;
    FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request_details, container, false);
        tvUsername = (TextView) v.findViewById(R.id.tvProfileName);
        tvBloodGroup =(TextView) v.findViewById(R.id.tvBloodGroup);
        tvContact =(TextView) v.findViewById(R.id.tvContact);
        tvCity =(TextView) v.findViewById(R.id.tvAddress);
        tvMessage =(TextView) v.findViewById(R.id.tvMessage);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        user_id = userDetails.get(SessionManager.KEY_USER_ID);
        fragmentManager = getFragmentManager();
        btnDonate = (Button) v.findViewById(R.id.btnDonate);
        if(isConnected) {
            btnDonate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle("Sure to Donate?");
                    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
                            backgroundWorker.execute(String.valueOf(request_id), user_id);
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
            });
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                request_id = bundle.getInt("request_id");
            }
            new JSONAsyncTask().execute("http://bloodapp.witorbit.net/index.php?display=m_request&request_details&request_id=" + request_id);
        }else{
            Toast.makeText(getActivity(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
        }

        Log.d("RequestDetails","Before returning onCreateView");
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

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

    /*******
     * Inner BackgroundWorker Class
     *******/
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                String request_id, user_id, blood_group, request_status, detail, date_time, city;
                //------------------>>
                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpGet);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject object = new JSONObject(data);
                    JSONObject request = object.getJSONObject("request");
                    JSONObject user = object.getJSONObject("user");
                    username = user.getString("full_name");
                    req_blood_group = request.getString("blood_group");
                    user_contact = user.getString("contact_no");
                    req_city = request.getString("city");
                    message = request.getString("detail");


                    Log.d("Result","True");
                    return true;
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
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            if(result) {
                try {
                    tvUsername.setText(username);
                    tvBloodGroup.setText(req_blood_group);
                    tvContact.setText(user_contact);
                    tvCity.setText(req_city);
                    tvMessage.setText(message);
                } catch (Exception e) {
                    Log.d("RequestDetails", "Error setting values");
                }
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*******
     * Inner BackgroundWorker Class fired when user clicks on Donate button
     *******/
    class BackgroundWorker extends AsyncTask<String,Void,String> {
        Context context;
        AlertDialog alertDialog;
        BackgroundWorker(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = "http://bloodapp.witorbit.net/index.php?display=m_request&add_donner";
            String request_id       = params[0];
            String user_id          = params[1];
            try {
                URL url = new URL(login_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("request_id","UTF-8")+"="+URLEncoder.encode(request_id,"UTF-8")+"&"+
                        URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                String result = "";

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

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

            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            if(result.equals("done")){
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).addToBackStack(null).commit();
            }
            else{
                Toast.makeText(getActivity(), "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment()).addToBackStack(null).commit();
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
