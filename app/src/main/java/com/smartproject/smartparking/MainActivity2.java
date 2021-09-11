package com.smartproject.smartparking;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ListView listView;
    DatabaseReference databaseReference,data;
    String Username, Email;
    List<Bookinguser> storeDataList;
    CustomAdapter customAdapter;
    Button placepicker;
    TextView Parking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.getuserdata );
        placepicker=findViewById( R.id.placepickerbutton );
        Parking=findViewById( R.id.parkingnameid );

        toolbar = findViewById(R.id.toolBarID);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, null,
                toolbar, R.string.drawerOpen, R.string.drawerClose);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        databaseReference = FirebaseDatabase.getInstance().getReference().child( "Reserved List" );
        data=FirebaseDatabase.getInstance().getReference().child( "Parking Location" );
        placepicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity2.this,Placepicker.class) );
            }
        } );

        storeDataList = new ArrayList<>();
        customAdapter = new CustomAdapter(this, storeDataList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.settings_menu_bar, menu);
        return super.onCreateOptionsMenu( menu );
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutID){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent it = new Intent(MainActivity2.this, AdminloginActivity.class);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.getUid()!=null) {
                Username = user.getUid();
            }

            if(user.getEmail()!=null){
                Email = user.getEmail();
            }
            String uid = user.getUid();
            DatabaseReference parking_name_database =data.child( uid );
            parking_name_database.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot datas: snapshot.getChildren()){
                        String phone=datas.getKey();
                        if(phone.equals( "park" )) {
                            String Parkingname = String.valueOf( snapshot.child( "park" ).getValue() );
                            Parking.setText( Parkingname );
                            if (Parkingname!=null) {
                                placepicker.setVisibility( View.GONE );
                            } else {
                                placepicker.setVisibility( View.VISIBLE );
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            } );
            DatabaseReference userRef1 = databaseReference.child(Username);
            userRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storeDataList.clear();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        Bookinguser storeData = dataSnapshot1.getValue(Bookinguser.class);
                        storeDataList.add(storeData);
                    }
                    listView.setAdapter(customAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;

        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Are you sure to leave ?\n you will be logged out");
        alertDialogBuilder.setIcon(R.drawable.exit);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().signOut();
        super.onStop();
    }
}
