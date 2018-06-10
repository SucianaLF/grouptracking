package com.example.sucianalf.grouptracking;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sucianalf.grouptracking.Adapter.MyRecyclerViewAdapter;
import com.example.sucianalf.grouptracking.Model.DataGroup;
import com.example.sucianalf.grouptracking.Model.DataObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class Group extends AppCompatActivity {

    EditText namaGroup;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private DatabaseReference mDatabase, mGroupReference;
    private DatabaseReference myref;
    private RecyclerView recyclerView;
    private ArrayList<DataGroup> grouplist;
    public ArrayList results = new ArrayList<DataGroup>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);

        mGroupReference = FirebaseDatabase.getInstance().getReference("group");
        //Ini adalah fungsi untuk mengambil data dari firebase
        mDatabase.child("group").orderByChild("nama_group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotl : dataSnapshot.getChildren()) {
                    DataGroup grup = dataSnapshotl.getValue(DataGroup.class);
                    DataGroup grp = new DataGroup(grup.getNama_group(),grup.getTanggal_dibuat(),grup.getDibuat_oleh());
                    results.add(grp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Sampe sini ya fungsi untuk mengambil data dari firebasenya

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        StringBuilder sb = new StringBuilder();
        for(Object s : results)
        {
            sb.append(s);
            sb.append("\t");
        }
//        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
        Log.d("TAG", sb.toString());
        mAdapter = new MyRecyclerViewAdapter(results);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                }
                });
    }

//    private void addDataGroup(String nama_group)
//    {
//        com.example.sucianalf.grouptracking.Model.DataGroup dataGroup = new com.example.sucianalf.grouptracking.Model.DataGroup(nama_group);
//        mDatabase.child("group").child("1").setValue(dataGroup);
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
                Toast.makeText(Group.this, "Grup Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
