package com.smartproject.smartparking;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView profile, about, feedback, btn1;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btn1 = (TextView) v.findViewById(R.id.findLotID);
        profile = (TextView) v.findViewById(R.id.profileID);
        about = (TextView) v.findViewById(R.id.aboutID);
        feedback = (TextView) v.findViewById(R.id.feedbackID);
        btn1.setOnClickListener(this);
        profile.setOnClickListener(this);
        about.setOnClickListener(this);
        feedback.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        Fragment fragment;
        if (v.getId() == R.id.findLotID) {
            fragment = new MapsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentID, fragment).commit();
        } else if (v.getId() == R.id.profileID) {
            fragment = new ProfileFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentID, fragment).commit();
        } else if (v.getId() == R.id.feedbackID) {
            fragment = new FeedbackFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentID, fragment).commit();
        } else if (v.getId() == R.id.aboutID) {
            fragment = new AboutFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentID, fragment).commit();
        }
    }

    public void shutDownFunction() {

    }
}
