package net.itempire.donateblood;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class FeaturedDonorsFragment extends Fragment {
    SessionManager sessionManager;
    TextView tvDrawerUsername, tvDrawerPhone;
    NavigationView navigationView;
    View headerView;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    ProgressDialog dialog;
    ListView featuredDonorsListView;
    FeaturedDonorsAdapter adapter;
    ArrayList<FeaturedDonors> featuredDonorsArrayList;



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
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String name = userDetails.get(SessionManager.KEY_NAME);
        String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);
        View v = inflater.inflate(R.layout.fragment_featured_donors, container, false);

        if(isConnected) {
            if (!sessionManager.isLoggedIn()) {
                name = getString(R.string.txt_username);
                phone = getString(R.string.txt_phone);
                tvDrawerPhone.setText(phone);
                tvDrawerUsername.setText(name);
                sessionManager.checkLogin();
                return inflater.inflate(R.layout.fragment_donation_requests, container, false);
            } else {

                tvDrawerUsername.setText(name);
                tvDrawerPhone.setText(phone);

                featuredDonorsArrayList = new ArrayList<FeaturedDonors>();
                new JSONAsyncTask().execute("http://bloodapp.witorbit.net/index.php?display=m_featured_donors");
                featuredDonorsListView = (ListView) v.findViewById(R.id.lvFeaturedDonors);
                adapter = new FeaturedDonorsAdapter(getActivity(), R.layout.featured_donor_row, featuredDonorsArrayList);
                featuredDonorsListView.setAdapter(adapter);
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
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

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
                //------------------>>
                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpGet);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    String donorName, donorThanks, donorCity;

                    JSONArray jsonArray = new JSONArray(data);
                    int arraySize = jsonArray.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        donorName = object.getString("full_name");
                        donorThanks = object.getString("thanks");
                        if(donorThanks == null || donorThanks.equals("")){
                            donorThanks = "0";
                        }
                        donorCity = object.getString("city");
                        if(donorCity == null || donorCity.equals("")){
                            donorCity = "N/A";
                        }
                        FeaturedDonors featuredDonor = new FeaturedDonors();
                        featuredDonor.setDonorName(donorName);
                        featuredDonor.setDonoroThanks(donorThanks);
                        featuredDonor.setDonorCity(donorCity);

                        featuredDonorsArrayList.add(featuredDonor);
                    }
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
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getActivity().getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}
