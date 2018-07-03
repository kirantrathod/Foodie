package com.example.kiran.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class WelcomeHotelOwners extends AppCompatActivity {
    private EditText name,address;
    private AutoCompleteTextView category;
    private Button submit;
    private FirebaseAuth auth;
    private TextView entermenu,replyreview,menutrial;
    private DatabaseReference mDatabase;
    private ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_hotel_owners);
        name=(EditText)findViewById(R.id.hotelname);
        submit=(Button)findViewById(R.id.submit);
        address=(EditText)findViewById(R.id.address);
        category=(AutoCompleteTextView)findViewById(R.id.autocomplete_category);
        auth = FirebaseAuth.getInstance();
        mprogress=new ProgressDialog(WelcomeHotelOwners.this);
        entermenu=(TextView)findViewById(R.id.enter);
        replyreview=(TextView)findViewById(R.id.gotoreply);
        menutrial=(TextView)findViewById(R.id.menutrial);
        menutrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent90=new Intent(WelcomeHotelOwners.this,show_menu_to_owner.class);
                startActivity(intent90);
            }
        });
        replyreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent999=new Intent(WelcomeHotelOwners.this,Reply_reviews.class);
                startActivity(intent999);
            }
        });
        entermenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentmenu=new Intent(WelcomeHotelOwners.this,Enter_menu.class);
                startActivity(intentmenu);

            }
        });


        String[] categories= getResources().getStringArray(R.array.category_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(WelcomeHotelOwners.this, android.R.layout.simple_list_item_1, categories);
        category.setAdapter(adapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mprogress.setTitle("Updating Information...");
                mprogress.setMessage("Please wait..while we are loading!!");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();
                String name_hotel=name.getText().toString().trim();
                String address_hotel=address.getText().toString().trim();
                String categories=category.getText().toString().trim();
                String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = currentUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("hotel_owners").child(uid);



                HashMap<String, String> usermap = new HashMap<>();
                usermap.put("name",name_hotel);
                usermap.put("address",address_hotel);
                usermap.put("device_token",DeviceToken);
                usermap.put("category",categories);
                mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mprogress.dismiss();
                            Intent intent;
                            intent = new Intent(WelcomeHotelOwners.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });





            }
        });


    }
}
