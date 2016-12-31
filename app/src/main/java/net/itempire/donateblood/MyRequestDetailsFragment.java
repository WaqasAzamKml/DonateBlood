package net.itempire.donateblood;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;

/**
 * Created by MIRSAAB on 12/31/2016.
 */

public class MyRequestDetailsFragment extends Fragment {
    String username, req_blood_group, user_contact, req_city, message;
    int request_id;
    TextView tvUsername, tvBloodGroup, tvContact, tvCity, tvMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_request_details, container, false);
        tvUsername = (TextView) v.findViewById(R.id.tvProfileName);
        tvBloodGroup =(TextView) v.findViewById(R.id.tvBloodGroup);
        tvContact =(TextView) v.findViewById(R.id.tvContact);
        tvCity =(TextView) v.findViewById(R.id.tvAddress);
        tvMessage =(TextView) v.findViewById(R.id.tvMessage);
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            request_id = bundle.getInt("request_id");
        }
        new JSONAsyncTask().execute("http://bloodapp.witorbit.net/index.php?display=m_request&request_details&request_id="+request_id);

        Log.d("RequestDetails","Before returning onCreateView");
        return v;
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
}
