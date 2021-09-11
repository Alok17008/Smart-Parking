   package com.smartproject.smartparking;

   import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
   import com.google.firebase.database.DataSnapshot;
   import com.google.firebase.database.DatabaseError;
   import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
   import com.google.firebase.database.ValueEventListener;

   public class AdminloginActivity extends AppCompatActivity  implements View.OnClickListener {
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent(this,LoginActivity.class) );
        //if (doubleBackToExitPressedOnce) {
        //    super.onBackPressed();
        //    return;
        //}

        //this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        //mHandler.postDelayed(mRunnable, 2000);
    }

    EditText Email, Password;
    Button LogInButton;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    ProgressDialog dialog;
    TextView textView,resetpassword;
    DatabaseReference mdatabase;
    public static final String userEmail="";

    public static final String TAG="LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_adminlogin );
        LogInButton = (Button) findViewById(R.id.loginbutton);

        textView = (TextView) findViewById( R.id.textView4 );
        resetpassword =(TextView) findViewById( R.id.textView8 );

        Email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        Password = (EditText) findViewById(R.id.editTextTextPassword4);
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference().child( "Admins" );
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(AdminloginActivity.this, MainActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Log.d(TAG,"AuthStateChanged:Logout");
                }

            }
        };
        // LogInButton.setOnClickListener((View.OnClickListener) this);
        //RegisterButton.setOnClickListener((View.OnClickListener) this);
        //Adding click listener to log in button.
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                userSign();


            }
        });

        // Adding click listener to register button.
        textView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminregistrationActivity.class ) );
            }
        } );

        resetpassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpass();
            }
        } );

    }
    private void resetpass() {
        email = Email.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(AdminloginActivity.this,"Enter the correct Email", Toast.LENGTH_SHORT).show();
        }else{
            auth.sendPasswordResetEmail( String.valueOf( email ) )
                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( AdminloginActivity.this,"Password reset Link is successfully Sent to your Email address",Toast.LENGTH_SHORT ).show();
                            }else{
                                Toast.makeText(AdminloginActivity.this,"User Not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    // @Override
    /**public void onBackPressed() {
     LoginActivity.super.finish();
     }**/



    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(AdminloginActivity.this,"Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(AdminloginActivity.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();

                    Toast.makeText(AdminloginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    mdatabase.child( uid ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String As = snapshot.child( "as" ).getValue(String.class);
                                if(As.equalsIgnoreCase("Admin")){
                                    checkIfEmailVerified();
                                }
                                else{
                                    Toast.makeText(AdminloginActivity.this,"You are not Admin, please Login into User section..",Toast.LENGTH_LONG ).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    } );


                    //

                }
            }
        });

    }
    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified(){
        FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified=users.isEmailVerified();
        if(!emailVerified){
            Toast.makeText(this,"Verify the Email Id",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        }
        else {
            Email.getText().clear();

            Password.getText().clear();
            Intent intent = new Intent(AdminloginActivity.this, MainActivity2.class);

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail,email);

            startActivity(intent);

        }
    }

    @Override
    public void onClick(View v) {

    }
}