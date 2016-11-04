package net.itempire.donateblood;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class VolunteersListFragment extends Fragment {
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        return inflater.inflate(R.layout.fragment_volunteers_list, container, false);
    }
}
