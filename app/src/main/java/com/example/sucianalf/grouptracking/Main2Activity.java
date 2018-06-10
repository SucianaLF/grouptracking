package com.example.sucianalf.grouptracking;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sucianalf.grouptracking.Model.Object_Mhsw_Dosen;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import static java.security.AccessController.getContext;

public class Main2Activity extends AppCompatActivity {

    EditText namaGroup;
    RecyclerView rView;
    LinearLayoutManager  lLayout;
    View v;
    SearchView searchView;
    RelativeLayout mainLayout, wrapSearchDosen;
    private DatabaseReference mDatabase;
    private List<Task> allTask;
    List<Object_Mhsw_Dosen> allItems = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //turnOnGps();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        setupAdaptermhsw_dosen();

    }
//    @Nullable
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.activity_main2, container, false);
//        setupAdaptermhsw_dosen();
////        setSearch();
////        dialogOpsi();
////        changeColor();
//        return v;
//    }


//    public void dialogOpsi() {
//        ImageView more = v.findViewById(R.id.more);
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final LayoutInflater factory = LayoutInflater.from(getActivity());
//                final View exitDialogView = factory.inflate(R.layout.dialog_more_dsn_mhsw, null);
//                final AlertDialog exitDialog = new AlertDialog.Builder(getActivity()).create();
//
//                exitDialog.setView(exitDialogView);
//                exitDialogView.findViewById(R.id.filterKelas).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        exitDialog.dismiss();
//                        dialogFilter();
//                    }
//                });
//
//                exitDialogView.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        exitDialog.dismiss();
//                        Toast.makeText(getActivity(), "Unduh data sukses", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                exitDialog.show();
//            }
//        });
//    }

    public void addgroup(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_group);
        Button btCancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        Button btOk = (Button) dialog.findViewById(R.id.dialog_ok);
        namaGroup = dialog.findViewById(R.id.InputNamaGroup);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String namagroup = namaGroup.getText().toString().trim();
                com.example.sucianalf.grouptracking.Model.DataGroup dataGroup = new com.example.sucianalf.grouptracking.Model.DataGroup(namagroup, date, user.getUid());
                mDatabase.child("group").push().setValue(dataGroup);
                Toast.makeText(Main2Activity.this, "Grup Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }

//    public void turnOnGps(){
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(Main2Activity.this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this).build();
//        googleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5 * 1000);
//        locationRequest.setFastestInterval(2 * 1000);
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        //**************************
//        builder.setAlwaysShow(true); //this is the key ingredient
//        //**************************
//
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult result) {
//                final Status status = result.getStatus();
////                final LocationSettingsStates state = result.getLocationSettingsStates();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//
//
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(
//                                    Main2Activity.this, 1000);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        break;
//                }
//            }
//        });
//    }


    public void setupAdaptermhsw_dosen(){

        Query grup = mDatabase.child("group").orderByChild("nama_group");
        grup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                for (DataSnapshot tes : dataSnapshot.getChildren()) {
                    Log.d("Dites firebase", tes.child("nama_group").getValue().toString());
                    allItems.add(new Object_Mhsw_Dosen(tes.child("nama_group").getValue().toString(),R.drawable.user));
                }

                List<Object_Mhsw_Dosen> rowListItem = allItems;
                lLayout = new LinearLayoutManager(Main2Activity.this);

                rView = findViewById(R.id.rview);
                rView.setLayoutManager(lLayout);

                Main3Activity rcAdapter = new Main3Activity(Main2Activity.this, rowListItem);
                rView.setAdapter(rcAdapter);
                rView.setNestedScrollingEnabled(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
