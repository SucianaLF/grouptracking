package com.example.sucianalf.grouptracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    EditText username, email, noTelp, location, password, confirmPassword ;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Query query, query2, query3;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String TAG = "PhoneAuthActivity";
    public String mVerificationId, uid, namaUser, emailUser, telpUser, lokasiUser, passwordUser ;
    public PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextView resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = findViewById(R.id.fullName);
        email = findViewById(R.id.userEmailId);
        noTelp = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        resultText = findViewById(R.id.edittext);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(SignUp.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(SignUp.this, "Too Many Requests", Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                // ...
                Toast.makeText(SignUp.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                // ...
            }
        };
    }

    public void register(View v)
    {
        namaUser = username.getText().toString().trim();
        emailUser = email.getText().toString().trim();
        telpUser = noTelp.getText().toString().trim();
//        int telpUser = Integer.parseInt(noTelp.getText().toString());
        lokasiUser = location.getText().toString().trim();
        passwordUser = password.getText().toString().trim();
//        if(passwordUser.length() >= 6)
//        {
//            query = mDatabase.child("user").orderByChild("noTelp").equalTo(telpUser);
//            query2 = mDatabase.child("user").orderByChild("username").equalTo(namaUser);
//            query3 = mDatabase.child("user").orderByChild("email").equalTo(emailUser);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.exists())
//                    {
//                        Toast.makeText(SignUp.this, "Nomor telepon sudah terdaftar", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.exists())
//                                {
//                                    Toast.makeText(SignUp.this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    query3.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            if(dataSnapshot.exists())
//                                            {
//                                                Toast.makeText(SignUp.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
//                                            }
//                                            else
//                                            {
                                                phone();
                                                mAuth.createUserWithEmailAndPassword(emailUser, passwordUser)
                                                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Sign in success, update UI with the signed-in user's information
                                                                    Log.d(TAG, "createUserWithEmail:success");
                                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                                } else {
                                                                    // If sign in fails, display a message to the user.
                                                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }

                                                                // ...
                                                            }
                                                        });

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                if(user != null){ uid = user.getUid();}
                                                showInputDialog(namaUser,emailUser,telpUser,lokasiUser,passwordUser);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//        else
//        {
//            Toast.makeText(this, "Password kurang dari 6 karakter.", Toast.LENGTH_SHORT).show();
//        }
//        if(query!=null && query2!=null) {
//            Toast.makeText(this, "Username atau nomor telepon sudah terdaftar", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            phone();
//            showInputDialog(namaUser,emailUser,telpUser,lokasiUser,passwordUser);
//        }
//        regisUser(namaUser,emailUser,telpUser,lokasiUser,passwordUser);
//        mDatabase.child("user").child("1").setValue("foto","test");


//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
    }

    protected
    void showInputDialog(final String nama, final String email, final String telp, final String lokasi, final String pass) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(SignUp.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_verification_code, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignUp.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        verifyPhoneNumberWithCode(mVerificationId, editText.getText().toString());

                        AuthCredential credentials = EmailAuthProvider.getCredential(emailUser, passwordUser);
                        mAuth.getCurrentUser().linkWithCredential(credentials)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "linkWithCredential:success");
                                            FirebaseUser user = task.getResult().getUser();

                                        } else {
                                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                                            Toast.makeText(SignUp.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        // ...
                                    }
                                });

                        com.example.sucianalf.grouptracking.Model.SignUp regis = new com.example.sucianalf.grouptracking.Model.SignUp(nama, email, telp, lokasi, pass);
                        mDatabase.child("user").child(uid).setValue(regis);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Toast.makeText(SignUp.this, user.toString(), Toast.LENGTH_SHORT).show();
                            // [START_EXCLUDE]
//                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    public void phone()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                noTelp.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

}
