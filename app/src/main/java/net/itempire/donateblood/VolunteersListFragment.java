package net.itempire.donateblood;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class VolunteersListFragment extends Fragment {
    SessionManager sessionManager;
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
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            String name = getString(R.string.txt_username);
            String phone = getString(R.string.txt_phone);
            tvDrawerPhone.setText(phone);
            tvDrawerUsername.setText(name);
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else {
            HashMap<String, String> userDetails = sessionManager.getUserDetails();
            String name = userDetails.get(SessionManager.KEY_NAME);
            String phone = userDetails.get(SessionManager.KEY_CONTACT_NUMBER);

            tvDrawerUsername.setText(name);
            tvDrawerPhone.setText(phone);
            return inflater.inflate(R.layout.fragment_volunteers_list, container, false);
        }

    }
}
