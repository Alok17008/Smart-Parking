package com.smartproject.smartparking;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    LatLng DevicelatLng;
    TextView t1,b1,c1;
    EditText nameedit,phoneedit,vehical_no;
    Button submit_button;
    RadioGroup vehicaltype;
    RadioButton radioButton;

    DatabaseReference databaseReference,test,user_get,user_history;
    AutoCompleteTextView inputSearch;
    ImageView search;
    float zoomLevel = 16f;
    String username,buttonselected;
    int j = 0, i = 0;
    private GoogleMap mGoogleMap;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    MapFragment supportMapFragment;
    private final static int LOCATION_REQUEST_CODE = 23;
    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_maps, container, false);
        supportMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.mapID);
        if (supportMapFragment!= null) {
            supportMapFragment.getMapAsync(this);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }


        inputSearch = v.findViewById(R.id.searchMapID);
        search=v.findViewById( R.id.magnifyMapID);
        search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchLocation( v );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } );
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reserved List");
        test = FirebaseDatabase.getInstance().getReference().child( "Parking Location" );
        user_get = FirebaseDatabase.getInstance().getReference().child( "Users" );
        user_history=FirebaseDatabase.getInstance().getReference().child( "User History" );

        test.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap:snapshot.getChildren()) {
                    String loaction1=(String)snap.child( "lati" ).getValue();
                    String location2=(String)snap.child( "longi" ).getValue();
                    String uniqueid=(String)snap.child( "uniqueid" ).getValue();
                    addmarker(loaction1,location2,uniqueid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.getUid()!=null) {
                username = user.getUid();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            init();
        }

        return v;
    }

    public void searchLocation(View view) {
        try {
            String location = inputSearch.getText().toString();
            List<Address> addressList = null;

            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(getActivity());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addmarker(String L1, String L2, String name) {
        double l1 =Double.parseDouble(L1);
        double l2 =Double.parseDouble(L2);
        LatLng latLng = new LatLng(l1,l2);
        String parking_name = name;
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(parking_name).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.lot_marker)));
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();

                String markertitle = marker.getTitle();
                BookparkingArea(markertitle);

                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void BookparkingArea(String markertitle) {
        View view = getLayoutInflater().inflate( R.layout.fragment_bottomsheet,null );
        BottomSheetDialog dialog = new BottomSheetDialog( getActivity(),R.style.AppBottomSheetDialogTheme);
        dialog.setContentView( view );
        dialog.show();

        t1=view.findViewById( R.id.parking_name );
        b1=view.findViewById( R.id.bike_lot );
        c1=view.findViewById( R.id.car_lots );
        nameedit=view.findViewById( R.id.nameforbooking );
        phoneedit=view.findViewById( R.id.mobileforbookig );
        submit_button=view.findViewById( R.id.park_button );
        vehicaltype = (RadioGroup) view.findViewById(R.id.vehicalselected);
        vehical_no=view.findViewById( R.id.vehicalno );

        DatabaseReference userRef1 = test.child(markertitle);
        userRef1.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String bike_parking=(String)snapshot.child( "bikes" ).getValue();
                String car_parking=(String)snapshot.child( "cars" ).getValue();
                String parking_name=(String)snapshot.child( "park" ).getValue();

                dataupdate(parking_name,bike_parking,car_parking);

            }

            public void dataupdate(String parking_name, String bike_parking, String car_parking){

                t1.setText( parking_name );
                b1.setText( bike_parking );
                c1.setText( car_parking );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String u1 =user.getUid();
        DatabaseReference userRef2 = user_get.child(u1);
        userRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    String phone=datas.getKey();
                    if(phone.equals( "mobile" )){
                        String mob= String.valueOf( dataSnapshot.child( "mobile" ).getValue() );
                        String Dname=String.valueOf( dataSnapshot.child( "displayname" ).getValue() );
                        phoneedit.setText(" "+mob);
                        nameedit.setText(Dname);
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        submit_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String Name = nameedit.getText().toString().trim();
                    String mobile = phoneedit.getText().toString().trim();
                    radioButton =view.findViewById( vehicaltype.getCheckedRadioButtonId() );
                    buttonselected = radioButton.getText().toString().trim();
                    String Vehicalno=vehical_no.getText().toString().trim();
                    String parking_name=t1.getText().toString().trim();
                    final String saveCurrentDate, saveCurrentTime;
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
                    saveCurrentDate = currentDate.format(c.getTime());
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    saveCurrentTime = currentTime.format(c.getTime());

                    if (TextUtils.isEmpty( Name )) {
                        Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT ).show();
                        return;
                    }else if (mobile.length() == 11) {
                        Toast.makeText(getActivity(), "Mobile No. must be of 10 digit", Toast.LENGTH_SHORT ).show();
                        return;
                    } else if (TextUtils.isEmpty( mobile )) {
                        Toast.makeText(getActivity(), "Enter Mobile No. ", Toast.LENGTH_SHORT ).show();
                        return;
                    }else if (TextUtils.isEmpty( buttonselected )) {
                        Toast.makeText(getActivity(), "Please select Type of Vehical", Toast.LENGTH_SHORT ).show();
                        return;
                    }else if (TextUtils.isEmpty( Vehicalno )) {
                        Toast.makeText(getActivity(), "Enter your Vehical No.", Toast.LENGTH_SHORT ).show();
                        return;
                    }else{
                        StoreReservedListData(buttonselected,markertitle,Name,mobile,Vehicalno,parking_name,saveCurrentDate,saveCurrentTime);
                        Toast.makeText( getActivity(),"Parking Booked, Now you Park your vehical",Toast.LENGTH_SHORT ).show();
                        dialog.dismiss();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } );


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style);
        mGoogleMap.setMapStyle(mapStyleOptions);

        // Check permission
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                withListener( new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                        mGoogleMap.setOnMyLocationChangeListener( new GoogleMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                LatLng ltlng = new LatLng( location.getLatitude(), location.getLongitude() );
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                        ltlng,12 );
                                mGoogleMap.animateCamera( cameraUpdate );
                            }
                        });
                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mGoogleMap.setOnMyLocationButtonClickListener( new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                mGoogleMap.setOnMyLocationChangeListener( new GoogleMap.OnMyLocationChangeListener() {
                                    @Override
                                    public void onMyLocationChange(Location location) {
                                        LatLng ltlng = new LatLng( location.getLatitude(), location.getLongitude() );
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                                ltlng,zoomLevel );
                                        mGoogleMap.animateCamera( cameraUpdate );
                                    }
                                } );
                                return false;
                            }
                        } );


                        // Set device location button layout right bottom
                        View locationButton = ((View)supportMapFragment.getView().findViewById(Integer.parseInt("1"))
                                .getParent()).findViewById(Integer.parseInt("2"));
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        params.setMargins(0, 0, 0, 400);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int VectorID) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, VectorID);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void StoreReservedListData(String buttonselected,String markertitle, String name, String mobile, String vehicalno, String parking_name, String saveCurrentDate, String saveCurrentTime){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.getUid()!=null) {
                username = user.getUid();
            }
        }

        String Key_User_Info = username;
        StoreReservedData storeReservedData;
        storeReservedData = new StoreReservedData(buttonselected,parking_name,name,mobile,vehicalno, saveCurrentDate, saveCurrentTime);
        databaseReference.child(markertitle).child( Key_User_Info ).setValue(storeReservedData);
        user_history.child( Key_User_Info ).setValue( storeReservedData );
    }
}
