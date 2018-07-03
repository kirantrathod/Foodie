package com.example.kiran.foodie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class show_menu_to_owner extends AppCompatActivity {
    private FirebaseAuth mauth;
    private RecyclerView mmenulistveg,mmenulistnonveg;
    private DatabaseReference mDatabase,mDatabase1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu_to_owner);
        mauth=FirebaseAuth.getInstance();
        String mcurrentuser=mauth.getCurrentUser().getUid();
        mmenulistveg=(RecyclerView) findViewById(R.id.recyclerviewmenu_veg);
        mmenulistveg.setHasFixedSize(true);
        mmenulistveg.setLayoutManager(new LinearLayoutManager(show_menu_to_owner.this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("menu").child(mcurrentuser).child("veg");
        //mDatabase1= FirebaseDatabase.getInstance().getReference().child("menu").child(mcurrentuser).child("veg");



        mmenulistnonveg=(RecyclerView) findViewById(R.id.recyclerviewmenu_nonveg);
        mmenulistnonveg.setHasFixedSize(true);
        mmenulistnonveg.setLayoutManager(new LinearLayoutManager(show_menu_to_owner.this));
       // mDatabase= FirebaseDatabase.getInstance().getReference().child("menu").child(mcurrentuser).child("veg");
        mDatabase1= FirebaseDatabase.getInstance().getReference().child("menu").child(mcurrentuser).child("non_veg");



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<menuclass,menuviewholder1> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<menuclass, menuviewholder1>(
                menuclass.class,
                R.layout.menu_layout_owner,
                menuviewholder1.class,
                mDatabase

        ){
            @Override
            protected void populateViewHolder(menuviewholder1 viewHolder, menuclass model, int position) {
                viewHolder.setName_of_menu(model.getName_of_menu());
                viewHolder.setPrice(model.getPrice());
            }

        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        mmenulistveg.setAdapter(firebaseRecyclerAdapter);
        //----------------------------------------------

        FirebaseRecyclerAdapter<menuclass,menuviewholder1> firebaseRecyclerAdapter1=new FirebaseRecyclerAdapter<menuclass, menuviewholder1>(
                menuclass.class,
                R.layout.menu_layout_owner,
                menuviewholder1.class,
                mDatabase1

        ){
            @Override
            protected void populateViewHolder(menuviewholder1 viewHolder, menuclass model, int position) {
                viewHolder.setName_of_menu(model.getName_of_menu());
                viewHolder.setPrice(model.getPrice());
            }

        };
        mmenulistnonveg.setAdapter(firebaseRecyclerAdapter1);
        //-----------------------------------------------------
    }
    public static class menuviewholder1 extends RecyclerView.ViewHolder {
        View mview;
        public menuviewholder1(View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setName_of_menu(String name_of_menu){
            TextView namemenu=(TextView)mview.findViewById(R.id.nameofmenu);
            namemenu.setText(name_of_menu);
        }
        public void setPrice(String price){
            TextView pricevalue=(TextView)mview.findViewById(R.id.pricetextview);
            pricevalue.setText(price);
        }



    }

}
