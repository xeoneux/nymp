package com.bethelper.nymp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView check;
    final String TAG = "Something Something";
    DatabaseReference ref;
    private String sharedKey;
    String stat;
    private String status;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        check = (TextView) findViewById(R.id.check);
        status = "Not-Connected";

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public void connect(View view) {
    try {
        check();
        //wait(10000);
    }catch(Exception e) {
        Log.d(TAG,"Exception: "+e.getMessage());
    }

        //  Log.i(TAG, "KEy: " + key);
        if(status.equals("Not-Connected")) {
            String key = mDatabase.child("connections").push().getKey();
            Log.d(TAG, "Pushed a New Connection");
            query.removeEventListener(childEventListener);
            sharedKey = key;
            Connection con = new Connection("Not-Connected");
            mDatabase.child("connections").child(sharedKey).setValue(con);
            mDatabase.child("connections").child(sharedKey).child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        if(dataSnapshot.getValue().equals("Connected")) {
                            status = "Connected";
                            check.setText(status);
                            mDatabase.child("connections").child(sharedKey).child("status").setValue("Connected");
                            Log.d(TAG, "Connected");
                        }
                        if(dataSnapshot.getValue().equals("Not-Connected")) {
                            status = "Not-Connected";
                            check.setText(status);
                            Log.d(TAG, "DisConnected");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }



    private void check() {

        ref = mDatabase.child("connections");
        query = ref.orderByChild("status").equalTo("Not-Connected");
        query.addChildEventListener(childEventListener);

    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            if(dataSnapshot.exists()) {
                if(status.equals("Not-Connected")) {
                    Log.d(TAG, "Result: " + dataSnapshot.getKey());
                    sharedKey = dataSnapshot.getKey();
                    mDatabase.child("connections").child(sharedKey).child("status").setValue("Connected");
                    status = "Connected";
                    check.setText(status);
                }
                Log.d(TAG, "Runs Once");

            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };




    }








