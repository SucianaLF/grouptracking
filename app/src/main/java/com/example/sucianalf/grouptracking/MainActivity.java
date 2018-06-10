package com.example.sucianalf.grouptracking;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Boolean teslogin = false;
    private DatabaseReference mDatabase;
    private static final String TAG = "MyActivity";
    private ArrayList<String> Tes;
    FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final int PERMISSIONS_REQUEST = 1;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.login_emailid);
        password = findViewById(R.id.login_password);

        // Check GPS is enabled
//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
//            //finish();
//        }
//
//        // Check location permission is granted - if it is, start
//        // the service, otherwise request the permission
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            startTrackerService();
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST);
//        }

    }

//    private void loginToFirebase() {
//        // Authenticate with Firebase, and request location updates
//        String email = getString(R.string.firebase_email);
//        String password = getString(R.string.firebase_password);
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(
//                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
//            @Override
//            public void onComplete(Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "firebase auth success");
//                    requestLocationUpdates();
//                } else {
//                    Log.d(TAG, "firebase auth failed");
//                }
//            }
//        });
//    }

//    private void startTrackerService() {
//        startService(new Intent(this, TrackerService.class));
//        //finish();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
//            grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // Start the service when the permission is granted
//            startTrackerService();
//        } else {
//            finish();
//        }
//    }

//    private void requestLocationUpdates() {
//        // Functionality coming next step
//        LocationRequest request = new LocationRequest();
//        request.setInterval(10000);
//        request.setFastestInterval(5000);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
//
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
////        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
//        final String path = "location" + getString(R.string.firebase_path);
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            // Request location updates and when an update is
//            // received, store the location in Firebase
//            client.requestLocationUpdates(request, new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//                    Location location = locationResult.getLastLocation();
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    if (location != null) {
//                        Log.d(TAG, "location update " + location);
//                        ref.child("user").child (user.getUid()).child("location").setValue(location);
//                    }
//                }
//            }, null);
//        }
//    }
    public void buttonClickFunction(View v)
    {
        login();
//        Toast.makeText(this, username.getText().toString(), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, username.getText().toString());
    }

    public void SignUp(View v)
    {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void login(){
        if (validate() == false){
            onLoginFailed();
            return;
        }
        requestLogin();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        buttonClickFunction(setEnabled(true));
    }


    public void requestLogin()
    {
        final String uname = username.getText().toString();
        final String pass = password.getText().toString();

//        Query loginUsername = mDatabase.child("user").orderByChild("username").equalTo(uname);
//        Query loginPassword = mDatabase.child("user").orderByChild("password").equalTo(pass);
        Query login = mDatabase.child("user").orderByChild("username");
        mAuth = FirebaseAuth.getInstance();
        login.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tes : dataSnapshot.getChildren()) {
                if((tes.child("username").getValue().toString().equals(uname) || tes.child("noTelp").getValue().toString().equals(uname)) && tes.child("password").getValue().toString().equals(pass)) {
                    teslogin = true;


                    mAuth.signInWithEmailAndPassword(tes.child("email").getValue().toString(), pass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        requestLocationUpdates();
                                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed. " + task.getException(),
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });

                }
                else{
                    teslogin = false;
                }
            }
              if(teslogin)
              {
                  Toast.makeText(MainActivity.this, "Username atau password tidak tepat", Toast.LENGTH_SHORT).show();

              }
              else
                {
                    Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database tidak dapat diakses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String uname = username.getText().toString();
        String pass = password.getText().toString();

        if (uname.isEmpty()) {
            username.setError("Username is empty");
            valid = false;
        } else {
            username.setError(null);
        }

        if (pass.isEmpty()) {
            password.setError("Password is empty");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }



}
