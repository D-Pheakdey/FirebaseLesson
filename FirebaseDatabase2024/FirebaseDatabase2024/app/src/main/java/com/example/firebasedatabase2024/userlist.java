package com.example.firebasedatabase2024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userlist extends AppCompatActivity {
    ArrayList<User> myArrayList ;
    MyAdapter myAdapter;;
    ListView Userlist;
    SearchView searchlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        // Initialize the ArrayList and Adapter
        myArrayList = new ArrayList<>();
        myAdapter = new MyAdapter(this, myArrayList);
        // Set up the ListView and SearchView
        searchlist = (SearchView) findViewById(R.id.schUser);
        Userlist = (ListView) findViewById(R.id.lstUsrList);
        Userlist.setAdapter(myAdapter);
        // Set up Firebase reference
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference ref = myDb.getReference("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myArrayList.clear();  // Clear the list before adding new data
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User myUser = new User(
                            ds.child("id").getValue(String.class),
                            ds.child("name").getValue(String.class),
                            ds.child("pass").getValue(String.class)
                    );
                    myArrayList.add(myUser);
                }
                myAdapter.notifyDataSetChanged();  // Notify the adapter about the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //Search List of User
        searchlist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<User> results = new ArrayList<>();
                for (User myUser : myArrayList) {
                    if (myUser.getName().toLowerCase().contains(newText.toLowerCase()) ||
                            String.valueOf(myUser.getId()).contains(newText)) {
                        results.add(myUser);
                    }
                }
                ((MyAdapter) Userlist.getAdapter()).textFilter(results);
                return false;
            }
        });
        //Click list of user to edit
        Userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User myUser=(User)Userlist.getItemAtPosition(position);
                Intent i=new Intent(userlist.this,MainActivity.class);
                i.putExtra("uid",myUser.getId());
                i.putExtra("uname",myUser.getName());
                i.putExtra("upass",myUser.getPass());
                startActivity(i);
            }
        });
    }
    public void Back(View view){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}