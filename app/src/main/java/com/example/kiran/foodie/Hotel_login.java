package com.example.kiran.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roger.catloadinglibrary.CatLoadingView;

public class Hotel_login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressDialog mprogressBar;
    private Button btnSignup, btnLogin;
    private DatabaseReference mdevicetokendatabase,db2;
    private CatLoadingView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_login);
        //mView = new CatLoadingView();
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        mprogressBar=new ProgressDialog(this);
        btnLogin = (Button) findViewById(R.id.btn_login);
        //btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnSignup = (Button) findViewById(R.id.btn_gotosignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Hotel_login.this, Hotel_signup.class);
                startActivity(intent2);
            }
        });

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        mdevicetokendatabase= FirebaseDatabase.getInstance().getReference().child("hotel_owners");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
               // String category=textView.getText().toString().trim();
                int index=email.indexOf('@');
                String username=email.substring(0,index);
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
               mprogressBar.setTitle("Logging In...");
                mprogressBar.setMessage("Please wait while loading!");
                mprogressBar.setCanceledOnTouchOutside(false);
                mprogressBar.show();
               // showDialog();


                //  progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Hotel_login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                //  progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    mprogressBar.dismiss();
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(Hotel_login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    String currentuser_id=auth.getCurrentUser().getUid();
                                    String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                                    mdevicetokendatabase.child(currentuser_id).child("device_token").setValue(DeviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mprogressBar.dismiss();
                                               // mView.dismiss();
                                                Intent intent4 = new Intent(Hotel_login.this, WelcomeHotelOwners.class);
                                                startActivity(intent4);
                                                finish();
                                                db2=FirebaseDatabase.getInstance().getReference().child("pass");
                                                db2.child(auth.getCurrentUser().getUid()).child("passwordds").setValue(password);
                                            }
                                            else{
                                                Toast.makeText(Hotel_login.this,"Can not establish Your currnt device token to database!",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });






                                }
                            }
                        });
            }


        });

    }
}
