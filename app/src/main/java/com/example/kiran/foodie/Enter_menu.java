package com.example.kiran.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Enter_menu extends AppCompatActivity {
     Button Submit;
    CheckBox kaju_panir,panir,daal_tadka,veg_biryani;
    CheckBox butter_chicken,mutton_handi,Biryani,kabab;
    EditText kaju_panir_price,panir_price,daal_tadka_price,veg_biryani_price,
            butter_chicken_price,mutton_handi_price,Biryani_price,kabab_price;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabasereference;
    String mCurrentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_menu);
        mAuth=FirebaseAuth.getInstance();
        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick() {
        kaju_panir=(CheckBox)findViewById(R.id.checkbox1);
        panir=(CheckBox)findViewById(R.id.checkbox2);
        daal_tadka=(CheckBox)findViewById(R.id.checkbox3);
        veg_biryani=(CheckBox)findViewById(R.id.checkbox4);
        butter_chicken=(CheckBox)findViewById(R.id.checkbox2_1);
        mutton_handi=(CheckBox)findViewById(R.id.checkbox2_3);
        Biryani=(CheckBox)findViewById(R.id.checkbox2_2);
        kabab=(CheckBox)findViewById(R.id.checkbox2_4);
        Submit=(Button)findViewById(R.id.submitmenu);

        kaju_panir_price=(EditText)findViewById(R.id.price1);
        panir_price=(EditText)findViewById(R.id.price2);
        daal_tadka_price=(EditText)findViewById(R.id.price3);
        veg_biryani_price=(EditText)findViewById(R.id.price4);
        butter_chicken_price=(EditText)findViewById(R.id.price2_1);
        mutton_handi_price=(EditText)findViewById(R.id.price2_3);
        Biryani_price=(EditText)findViewById(R.id.price2_2);
        kabab_price=(EditText)findViewById(R.id.price2_4);




        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentuser=mAuth.getCurrentUser().getUid();
                HashMap<String, String> messagemap = new HashMap<>();
                DatabaseReference mupdate=FirebaseDatabase.getInstance().getReference().child("menu").child(mCurrentuser);


                String kajupanir_price=kaju_panir_price.getText().toString().trim();
                String panirprice=panir_price.getText().toString().trim();
                String daaltadkaprice=daal_tadka_price.getText().toString().trim();
                String vegbiryaniprice=veg_biryani_price.getText().toString().trim();
                String butterchickenprice=butter_chicken_price.getText().toString().trim();
                String muttonhandiprice=mutton_handi_price.getText().toString().trim();
                String Biryaniprice=Biryani_price.getText().toString().trim();
                String kababprice=kabab_price.getText().toString().trim();

                if(kaju_panir.isChecked()){
                    DatabaseReference mpush=mupdate.child("veg").push();
                    messagemap.put("name_of_menu","Kaju Panir");
                    messagemap.put("price",kajupanir_price);
                     mpush.setValue(messagemap);
                }
                if(panir.isChecked()){
                    DatabaseReference mpush=mupdate.child("veg").push();
                    messagemap.put("name_of_menu","Panir");
                    messagemap.put("price",panirprice);
                    mpush.setValue(messagemap);
                }
                if(daal_tadka.isChecked()){
                    DatabaseReference mpush=mupdate.child("veg").push();
                    messagemap.put("name_of_menu","Daaal Tadka");
                    messagemap.put("price",daaltadkaprice);
                    mpush.setValue(messagemap);
                }
                if(veg_biryani.isChecked()){
                    DatabaseReference mpush=mupdate.child("veg").push();
                    messagemap.put("name_of_menu","Veg-Birayani");
                    messagemap.put("price",vegbiryaniprice);
                    mpush.setValue(messagemap);
                }
                if(butter_chicken.isChecked()){
                    DatabaseReference mpush1=mupdate.child("non_veg").push();
                    messagemap.put("name_of_menu","Butter Chicken");
                    messagemap.put("price",butterchickenprice);
                    mpush1.setValue(messagemap);
                }
                if(mutton_handi.isChecked()){
                    DatabaseReference mpush1=mupdate.child("non_veg").push();
                    messagemap.put("name_of_menu","Mutton Handi");
                    messagemap.put("price",muttonhandiprice);
                    mpush1.setValue(messagemap);
                }
                if(Biryani.isChecked()){
                    DatabaseReference mpush1=mupdate.child("non_veg").push();
                    messagemap.put("name_of_menu","Biryani");
                    messagemap.put("price",Biryaniprice);
                    mpush1.setValue(messagemap);
                }
                if(kabab.isChecked()){
                    DatabaseReference mpush1=mupdate.child("non_veg").push();
                    messagemap.put("name_of_menu","Kabab");
                    messagemap.put("price",kababprice);
                    mpush1.setValue(messagemap);
                }
                Intent menuintent=new Intent(Enter_menu.this,show_menu_to_owner.class);
                startActivity(menuintent);


            }
        });

    }

}
