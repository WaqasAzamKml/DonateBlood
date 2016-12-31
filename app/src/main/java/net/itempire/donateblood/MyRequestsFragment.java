package net.itempire.donateblood;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.AdapterView;
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
 * Created by MIRSAB on 12/28/2016
 */

public class MyRequestsFragment extends Fragment {
    SessionManager sessionManager;
    TextView tvDrawerUsername, tvDrawerPhone;
    NavigationView navigationView;
    View headerView;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;
    RequestSharedPref requestSharedPref;
    ListView requestsListView;
    DonationRequestsAdapter adapter;
    ArrayList<DonationRequests> donationRequestsList;
    FragmentManager fragmentManager;
    int[] request_ids = new int[15];
    ProgressDialog dialog;
    String my_user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        requestSharedPref = new RequestSharedPref(getActivity().getApplicationContext());

        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        tvDrawerPhone = (TextView) headerView.findViewById(R.id.tvDrawerPhone);
        tvDrawerUsername = (TextView) headerView.findViewById(R.id.tvDrawerUsername);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cm = (ConnectivityManager)getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return inflater.inflate(R.layout.fragment_my_requests, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentManager = getFragmentManager();
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String user_id = userDetails.get(SessionManager.KEY_USER_ID);
        my_user_id = userDetails.get(SessionManager.KEY_USER_ID);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String name = userDetails.get(SessionManager.KEY_NAME);
        String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);
        if(isConnected){
            if(!sessionManager.isLoggedIn()){
                name = getString(R.string.txt_username);
                phone = getString(R.string.txt_phone);
                tvDrawerPhone.setText(phone);
                tvDrawerUsername.setText(name);

            }
            else {
                tvDrawerUsername.setText(name);
                tvDrawerPhone.setText(phone);
                donationRequestsList = new ArrayList<DonationRequests>();
                new JSONAsyncTask().execute("http://bloodapp.witorbit.net/index.php?display=m_request&check_user&user_id="+user_id+"&email="+email);
                requestsListView = (ListView) getView().findViewById(R.id.lvRequestsList);
                adapter = new DonationRequestsAdapter(getActivity(),R.layout.request_row,donationRequestsList);
                requestsListView.setAdapter(adapter);
                requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MyRequestDetailsFragment fragment = new MyRequestDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("request_id",request_ids[position]);
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
                    }
                });
            }
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
            if(!sessionManager.isLoggedIn()){
                name = getString(R.string.txt_username);
                phone = getString(R.string.txt_phone);
                tvDrawerPhone.setText(phone);
                tvDrawerUsername.setText(name);
            }
            else {
                tvDrawerUsername.setText(name);
                tvDrawerPhone.setText(phone);
            }
        }
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

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    int indexer = 0;
                    JSONArray jsonArray = new JSONArray(data);
                    int arraySize = jsonArray.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if(!my_user_id.equals(object.getString("fk_user_id")))
                            continue;
                        else{
                            request_ids[indexer] = Integer.parseInt(object.getString("request_id"));
                            indexer++;
                        }

                        DonationRequests request = new DonationRequests();

                        request.setRequestBloodGroup(object.getString("blood_group"));
                        request.setRequestDate(object.getString("date_time"));
                        request.setRequestCity(object.getString("city"));

                        Log.d("LoopNumber",""+i);
                        donationRequestsList.add(request);
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
