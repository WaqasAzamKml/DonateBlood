package net.itempire.donateblood;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by MIRSAAB on 11/16/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("DonateBloodToken", "Refreshed token: " + refreshedToken);

        // TODO: Implement this String refreshedToken = Firemethod to send any registration to your app's servers.
        // sendRegistrationToServer(refreshedToken);
    }
}
