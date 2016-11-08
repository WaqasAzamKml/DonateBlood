package net.itempire.donateblood;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by MIRSAAB on 11/1/2016.
 */

public class BloodGroupFragment extends Fragment {
    SessionManager sessionManager;
    Button btnAPostivie, btnANegative, btnBPostivie, btnBNegative,btnOPostivie, btnONegative,btnABPostivie, btnABNegative;
    android.app.FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            return inflater.inflate(R.layout.fragment_donation_requests, container, false);
        }
        else {
            View v = inflater.inflate(R.layout.fragment_blood_group, container, false);

            fragmentManager = getFragmentManager();

            btnAPostivie = (Button) v.findViewById(R.id.btnAPositive);
            btnANegative = (Button) v.findViewById(R.id.btnANegative);
            btnBPostivie = (Button) v.findViewById(R.id.btnBPositive);
            btnBNegative = (Button) v.findViewById(R.id.btnBNegative);
            btnOPostivie = (Button) v.findViewById(R.id.btnOPositive);
            btnONegative = (Button) v.findViewById(R.id.btnONegative);
            btnABPostivie = (Button) v.findViewById(R.id.btnABPositive);
            btnABNegative = (Button) v.findViewById(R.id.btnABNegative);

            btnAPostivie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnANegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnBPostivie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnBNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnOPostivie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnONegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnABPostivie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });
            btnABNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageDialog();
                }
            });

            return v;
        }
    }

    public void showMessageDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.etMessage);

        dialogBuilder.setTitle("Message");
        dialogBuilder.setMessage("Enter message below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), "Donation Request Sent", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,new ProfileFragment()).addToBackStack(null).commit();
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
}
