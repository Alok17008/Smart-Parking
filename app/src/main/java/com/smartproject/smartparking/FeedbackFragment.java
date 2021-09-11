package com.smartproject.smartparking;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackFragment extends Fragment implements View.OnClickListener{

    RadioGroup radioButtonGroup;
    RadioButton resultRadioButton;
    Button send;
    EditText feedback;
    int selectedID;
    String message, username;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feedback, container, false);

        feedback = v.findViewById(R.id.feedbackID);
        radioButtonGroup = v.findViewById(R.id.radiogroupID);
        send = v.findViewById(R.id.sendID);
        send.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Feedback" );

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sendID){
            String comment = feedback.getText().toString().trim();

            if(comment.isEmpty()) {
                feedback.setError("Fill this field");
            }

            else {
                selectedID = radioButtonGroup.getCheckedRadioButtonId();
                resultRadioButton = (RadioButton) radioButtonGroup.findViewById(selectedID);
                message = resultRadioButton.getText().toString().trim();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                username = user.getUid();
                String Key_User_Info = username;
                StoreReviewData storeReviewData = new StoreReviewData(message, comment);
                databaseReference.child(Key_User_Info).setValue(storeReviewData);

                feedback.setText(null);
            }
        }
    }
}
