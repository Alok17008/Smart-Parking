package com.smartproject.smartparking;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReserveListFragment extends Fragment {

    TextView textView, reservationText1, reservationText2;
    ImageView bkash, dbbl;
    DatabaseReference databaseReference,paymentdata,datareference,datareferen;
    String username, markertitle,  saveCurrentDate, saveCurrentTime;
    Button payment,endparking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_reserve_list, container, false);



        databaseReference =FirebaseDatabase.getInstance().getReference().child( "User History" );
        datareference =FirebaseDatabase.getInstance().getReference().child( "Admin Payment History" );
        datareferen =FirebaseDatabase.getInstance().getReference().child( "Users Payment History" );
        paymentdata =FirebaseDatabase.getInstance().getReference().child( "Reserved List" );

        textView = v.findViewById(R.id.parking_lot_ID1);
        reservationText1 = v.findViewById(R.id.reservationTimeID1);
        reservationText2 = v.findViewById(R.id.reservationTimeID2);
        payment=(Button)v.findViewById( R.id.makepayment );

        return v;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.getUid()!=null) {
                username = user.getUid();

                DatabaseReference userRef1 = databaseReference.child(username);
                try {
                    userRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot datas: dataSnapshot.getChildren()){

                                String mob= String.valueOf( dataSnapshot.child( "parking_name" ).getValue() );
                                String Dname=String.valueOf( dataSnapshot.child( "saveCurrentDate" ).getValue() );
                                String Dna=String.valueOf( dataSnapshot.child( "saveCurrentTime" ).getValue() );
                                textView.setText(mob);
                                reservationText1.setText( Dname );
                                reservationText2.setText( Dna );


                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        View view = getLayoutInflater().inflate( R.layout.fragment_paymentbottomsheet,null );
        BottomSheetDialog dialog = new BottomSheetDialog( getActivity());
        payment.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView( view );
                dialog.show();
                DatabaseReference getpaymentdata = databaseReference.child(username);
                getpaymentdata.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Dname = String.valueOf( snapshot.child( "saveCurrentDate" ).getValue() );
                            String Dna = String.valueOf( snapshot.child( "saveCurrentTime" ).getValue() );
                            paymentcalculate( Dname, Dna );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
            }



            @RequiresApi(api = Build.VERSION_CODES.O)
            private void paymentcalculate(String dname, String dna) {
                final String saveCurrentD, saveCurrentT;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM-dd-yyyy");
                saveCurrentD = currentDate.format(c.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentT = currentTime.format(c.getTime());
                SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                int i;
                try {
                    Date dateobj1 =sdf.parse( saveCurrentD+" "+saveCurrentT );
                    Date dateobj2 =sdf.parse( dname+" "+dna );
                    DecimalFormat formate=new DecimalFormat("###,###");
                    long diff=dateobj1.getTime()-dateobj2.getTime();
                    int diffhours= (int)(diff/(60*60*1000));
                    if (diffhours<=2){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            showprice(2);
                        }
                    }else{showprice( diffhours );}
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e( "tag",e.toString() );
                }



            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            private void showprice(int i) {

                String total = String.valueOf( 50*i );
                TextView showamount = (TextView) view.findViewById( R.id.totalAmount );
                showamount.setText(total);

            }
        } );
        Button button =(Button)view.findViewById( R.id.paymnetdone );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog1 = ProgressDialog.show( getActivity(),"","Payment Processing, Please wait....",true );
                Handler handler=new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        dialog1.dismiss();
                        dialog.dismiss();
                        Toast.makeText( getActivity()," Payment sucessfull.....",Toast.LENGTH_SHORT).show();

                    }
                },3000 );
                paymentdata.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap1:snapshot.getChildren()) {
                            String str=snap1.getKey();
                            if(snap1.hasChild( username )){
                                //paymentdata(str,username);
                                paymentdata.child( str ).child( username ).removeValue();
                                databaseReference.child( username ).removeValue();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );



            }

            private void paymentdata(String str, String username) {


            }
        } );

        super.onStart();
    }

}
