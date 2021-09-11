package com.smartproject.smartparking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminregistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, password, moblie_no;
    Button mRegisterbtn;
    TextView mLoginPageBack;
    TextView admin;
    FirebaseAuth mAuth;
    DatabaseReference mdatabase;
    String Name, Email, Password, Mobile, Gender,as="Admin";
    ProgressDialog mDialog;
    RadioGroup gender;
    RadioButton selectedgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminregistration);
        name = (EditText) findViewById( R.id.PersonName );
        moblie_no = (EditText) findViewById( R.id.editTextPhone );
        email = (EditText) findViewById( R.id.editTextTextEmailAddress2 );
        password = (EditText) findViewById( R.id.editTextTextPassword );
        gender = (RadioGroup) findViewById( R.id.radioGroup );
        mRegisterbtn = (Button) findViewById( R.id.rbutton );
        mLoginPageBack = (TextView) findViewById( R.id.textView2 );
        gender = findViewById( R.id.radioGroup );
        admin = (TextView) findViewById( R.id.textView2 );
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        mRegisterbtn.setOnClickListener( this );
        mLoginPageBack.setOnClickListener( this );
        mDialog = new ProgressDialog( this );
        mdatabase = FirebaseDatabase.getInstance().getReference().child( "Admins" );

        admin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminloginActivity.class ) );
            }
        } );

    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent(this,AdminloginActivity.class) );
    }

    public void onClick(View v) {
        if (v == mRegisterbtn) {
            UserRegister();
        } else if (v == mLoginPageBack) {
            Intent intent = new Intent( AdminregistrationActivity.this, AdminloginActivity.class );
            startActivity( intent );
        }
    }
    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();
        Mobile = moblie_no.getText().toString().trim();
        selectedgender = (RadioButton) findViewById( gender.getCheckedRadioButtonId() );
        Gender = selectedgender.getText().toString();


        if (TextUtils.isEmpty( Name )) {
            Toast.makeText( AdminregistrationActivity.this, "Enter Name", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Email )) {
            Toast.makeText( AdminregistrationActivity.this, "Enter Email", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Password )) {
            Toast.makeText( AdminregistrationActivity.this, "Enter Password", Toast.LENGTH_SHORT ).show();
            return;
        } else if (Password.length() < 6) {
            Toast.makeText( AdminregistrationActivity.this, "Passwor must be greater then 6 digit", Toast.LENGTH_SHORT ).show();
            return;
        } else if (Mobile.length() < 10) {
            Toast.makeText( AdminregistrationActivity.this, "Mobile No. must be of 10 digit", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Mobile )) {
            Toast.makeText( AdminregistrationActivity.this, "Enter Mobile No. ", Toast.LENGTH_SHORT ).show();
            return;
        } else if (TextUtils.isEmpty( Gender )) {
            Toast.makeText( AdminregistrationActivity.this, "Please select your gender", Toast.LENGTH_SHORT ).show();
            return;
        }
        mDialog.setMessage( "Creating User please wait..." );
        mDialog.setCanceledOnTouchOutside( false );
        mDialog.show();
        mAuth.createUserWithEmailAndPassword( Email, Password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth( task.getResult().getUser() );
                    mAuth.signOut();
                } else {
                    Toast.makeText( AdminregistrationActivity.this, "error on creating user", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText( AdminregistrationActivity.this, "Check your Email for verification", Toast.LENGTH_SHORT ).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            } );
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser( user.getUid() );
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child( uid ).setValue( user );
    }


    private User BuildNewuser() {
        return new User(
                getDisplayName(),
                getUserEmail(),
                getMobile(),
                getgender(),
                getas()
        );
    }

    private String getas() {
        return as;
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

    public String getMobile() {
        return Mobile;
    }

    private String getgender() {
        return Gender;
    }


}