package com.smartproject.smartparking;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmAddress extends DialogFragment implements
        android.view.View.OnClickListener, OnMapReadyCallback {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    private GoogleMap mMap;
    MapView mapView;
    Double Lat;
    Double Long;
    String Address,Name,Email,park_name;
    String bikelot;
    String carlot,uniqueid;
    TextView myAddress;
    EditText cars,bike,parking_name;
    Button SelectBtn;
    Button ChangeBtn;
    DatabaseReference mdatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Lat = getArguments().getDouble( "lat" );
        Long = getArguments().getDouble( "long" );
        Address = getArguments().getString( "address" );
        mdatabase = FirebaseDatabase.getInstance().getReference().child( "Parking Location" );



    }

    MapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.custom_confirm_address, container, false );
        myAddress = (TextView) v.findViewById( R.id.myAddress );
        SelectBtn = (Button) v.findViewById( R.id.Select );
        ChangeBtn = (Button) v.findViewById( R.id.Change );
        cars =(EditText)v.findViewById( R.id.cars_lot );
        bike =(EditText)v.findViewById( R.id.bikes_lot );
        parking_name=(EditText)v.findViewById( R.id.parkingname );


        mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.mapp );
        mapFragment.getMapAsync( this );
        // Toast.makeText(getActivity(),mNum,Toast.LENGTH_LONG).show();

        SelectBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikelot = bike.getText().toString().trim();
                carlot = cars.getText().toString().trim();
                park_name=parking_name.getText().toString().trim();

                if(TextUtils.isEmpty(bikelot)) {
                    Toast.makeText( getActivity(),"Please fill No. of Bikes lot "+bikelot,Toast.LENGTH_SHORT ).show();
                }if(TextUtils.isEmpty(carlot)) {
                    Toast.makeText( getActivity(),"Please fill No. of Cars lot "+carlot,Toast.LENGTH_SHORT ).show();
                }else {
                    Toast.makeText( getActivity(), myAddress.getText().toString(), Toast.LENGTH_LONG ).show();
                    getFragmentManager().beginTransaction().remove( mapFragment ).commit();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    setlocation( uid );
                    dismiss();
                    startActivity( new Intent(getActivity(),MainActivity2.class) );
                }
            }
        } );
        ChangeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove( mapFragment ).commit();
                dismiss();
            }
        } );
        getDialog().setCanceledOnTouchOutside( true );
        return v;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel( dialog );
        getFragmentManager().beginTransaction().remove( mapFragment ).commit();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss( dialog );
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myAddress.setText( Address );
        mMap.getUiSettings().setMyLocationButtonEnabled( false );
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position( new LatLng( Lat, Long ) );

        markerOptions.title( Address );
        mMap.clear();
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                new LatLng( Lat, Long ), 16f );
        mMap.animateCamera( location );
        mMap.addMarker( markerOptions );
        Log.d( "status", "success" );
    }

    private void setlocation(String uid) {
        uniqueid=uid;
        Location location = BuildNewuser();
        mdatabase.child( uid ).setValue( location );
    }


    private Location BuildNewuser() {
        return new Location(
                getLati(),
                getLongi(),
                getbikeslot(),
                getcarslot(),
                getparkingname(),
                getuniqueid()

        );
    }



    public String getLati() {
        return String.valueOf( Lat );
    }

    public String getLongi() {
        return String.valueOf( Long );
    }

    private String getbikeslot() {
        return bikelot;
    }

    private String getcarslot() {
        return carlot;    }

        private String getparkingname() {
        return park_name;
    }
    private String getuniqueid(){
        return uniqueid;
    }
}