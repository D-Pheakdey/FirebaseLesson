package com.example.firebasedatabase2024;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText id, name, pass;
    DatabaseReference databaseUsers;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = (EditText) findViewById(R.id.userid);
        name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.userpass);
        databaseUsers=FirebaseDatabase.getInstance().getReference();

        Intent recive_i=getIntent();
        id.setText(recive_i.getStringExtra("uid"));
        name.setText(recive_i.getStringExtra("uname"));
        pass.setText(recive_i.getStringExtra("upass"));
    }

    public void PUSHDATA(View view) {
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = myDb.getReference("User");
        User user = new User(id.getText().toString(), name.getText().toString(), pass.getText().toString());
        myRef.push().setValue(user);
    }

    public void SEARCH(View view) {
        final int[] found = new int[1];
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference ref = myDb.getReference("User");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
//                    User user = new User(
//                            ds.child("id").getValue(String.class),
//                            ds.child("name").getValue(String.class),
//                            ds.child("pass").getValue(String.class)
//                    );
//                    Userlist.add(user);

                    if (ds.child("id").getValue(String.class).equals(id.getText().toString())) {
                        name.setText(ds.child("name").getValue(String.class));
                        pass.setText(ds.child("pass").getValue(String.class));
                        found[0] =1;
                    }
                }
                if (found[0]==0){
                    Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                    name.setText("");
                    pass.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EDIT(View view) {
        // FirebaseDatabase fb= FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.orderByChild("id").equalTo(id.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // snapshot.getRef().child("name").setValue(name.getText().toString());
                //    snapshot.getRef().child("pass").setValue(pass.getText().toString());

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("name").setValue(name.getText().toString());
                    ds.getRef().child("pass").setValue(pass.getText().toString());
                    Toast.makeText(MainActivity.this, "Record has been updated!", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void DELETE(View view) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.orderByChild("id").equalTo(id.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        ds.getRef().removeValue();
                        id.setText("");
                        name.setText("");
                        pass.setText("");
                    }
                    Toast.makeText(MainActivity.this, "Record has benn deleted!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                    id.setText("");
                    name.setText("");
                    pass.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void SHOWLIST(View view){
        Intent intent=new Intent(this, userlist.class);
        startActivity(intent);
    }
}
