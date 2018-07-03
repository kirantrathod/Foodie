package com.example.kiran.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private  static final String TAG="MainActivity";
    private RecyclerView mhotelownerslist;
    private DatabaseReference mDatabase;
    private FirebaseUser mcurretuser;
    private DatabaseReference muserref;
    //private DatabaseReference muserref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        // this listener will be called when there is change in firebase user session
        authListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user==null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                }
        };
        mhotelownerslist=(RecyclerView) findViewById(R.id.hotel_list);
        mhotelownerslist.setHasFixedSize(true);
        mhotelownerslist.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("hotel_owners");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_signout){
            //////

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to Sign out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            ///////

        }
        return super.onOptionsItemSelected(item);
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        FirebaseRecyclerAdapter<hotels,MainActivity.hotelviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<hotels, MainActivity.hotelviewholder>(
                hotels.class,
                R.layout.hotel_details_layout,
                MainActivity.hotelviewholder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MainActivity.hotelviewholder viewHolder, hotels model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setCategory(model.getCategory());
                viewHolder.setAddress(model.getAddress());
                final String user_id=getRef(position).getKey();
                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent4=new Intent(MainActivity.this,Menu_or_Review.class);
                        intent4.putExtra("user_id",user_id);
                        startActivity(intent4);

                    }
                });

            }

            @Override
            public void onBindViewHolder(MainActivity.hotelviewholder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                if (position ==0) {
                    viewHolder.mview.setBackgroundResource(R.drawable.pexel);
                    viewHolder.mview.getBackground().setAlpha(190);
                } else if (position ==1 ) {
                    viewHolder.mview.setBackgroundResource(R.drawable.food);
                    viewHolder.mview.getBackground().setAlpha(190);
                } else if (position == 2) {
                    viewHolder.mview.setBackgroundResource(R.drawable.spice);
                    viewHolder.mview.getBackground().setAlpha(190);
                }
                else if(position==3){
                    viewHolder.mview.setBackgroundResource(R.drawable.dosa);
                    viewHolder.mview.getBackground().setAlpha(190);
                }
            }

        };

        mhotelownerslist.setAdapter(firebaseRecyclerAdapter);


    }
    public static class hotelviewholder extends RecyclerView.ViewHolder {
        View mview;
        public hotelviewholder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setName(String name){
            TextView namehotel=(TextView)mview.findViewById(R.id.hotel_name);
            namehotel.setText(name);
        }
        public void setCategory(String category){
            TextView category_=(TextView)mview.findViewById(R.id.category_layout);
            category_.setText(category);
        }
        public void setAddress(String address){
            TextView addresshotel=(TextView)mview.findViewById(R.id.address_layout);
            addresshotel.setText(address);
        }

    }



    @Override
    public void onStop() {
        super.onStop();

        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }

    }


}
