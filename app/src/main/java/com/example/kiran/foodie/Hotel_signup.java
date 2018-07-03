package com.example.kiran.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import butterknife.ButterKnife;

public class Hotel_signup extends AppCompatActivity {
    private AutoCompleteTextView textView;
    private Button btnSignUp;
    private FirebaseAuth mauth;
    private EditText email_input;
    private DatabaseReference mdevicetokendatabase;
    private TextInputLayout Name,inputPassword;
    private DatabaseReference mDatabase,mDatabase1;
    private ProgressDialog mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_signup);
        mauth = FirebaseAuth.getInstance();
        mprogress=new ProgressDialog(Hotel_signup.this);
        Name=(TextInputLayout) findViewById(R.id.name_inputlayout);
        email_input=(EditText) findViewById(R.id.email);
        inputPassword=(TextInputLayout)findViewById(R.id.passwordlayout);
        btnSignUp = (Button) findViewById(R.id.signuphotel);
        textView=(AutoCompleteTextView)findViewById(R.id.choosecategory);
        // Get the string array
        String[] categories= getResources().getStringArray(R.array.category_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(Hotel_signup.this, android.R.layout.simple_list_item_1, categories);
        textView.setAdapter(adapter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayname = Name.getEditText().getText().toString();
                String email = email_input.getText().toString();
                String password = inputPassword.getEditText().getText().toString();
                // String gender=genderEdittext.getText().toString().trim();
                String category=textView.getText().toString().trim();
                // reg_user(displayname,email,password);
                if (TextUtils.isEmpty(displayname)) {
                    Toast.makeText(getApplicationContext(), "Enter Name To Display Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mprogress.setTitle("Signing Up...");
                mprogress.setMessage("Please wait..You will be logged in after signing up");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();
                // progressBar.setVisibility(View.VISIBLE);
                reg_user(displayname, email, password,category);

               /* //-------------------device token-------------------------------------
                String currentuser_id=auth.getCurrentUser().getUid();
                String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                mdevicetokendatabase.child(currentuser_id).child("device_token").setValue(DeviceToken); */
            }

            private void reg_user(final String category, final String displayname, String email, String password) {
                mauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Hotel_signup.this, "Your account has created.", Toast.LENGTH_SHORT).show();
                                //progressBar.setVisibility(View.GONE);
                                mprogress.dismiss();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Hotel_signup.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    // String currentuser_id=auth.getCurrentUser().getUid();
                                   // String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                                    // mdevicetokendatabase.child(currentuser_id).child("device_token").setValue(DeviceToken);
                                    //putting uid and data in realtime database
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = currentUser.getUid();

                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("hotel_owners").child(uid);
                                    mDatabase1 = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                                    HashMap<String, String> usermap = new HashMap<>();
                                    usermap.put("name", displayname);
                                    usermap.put("category",category);
                                    mDatabase1.setValue(usermap);
                                    //usermap.put("device_token",DeviceToken);
                                   // usermap.put("image", "https://firebasestorage.googleapis.com/v0/b/beyou-a7151.appspot.com/o/profile_images%2FkTZxthCkQebTzdM7o5KC0miNxPw2.jpg?alt=media&token=8ba6019e-9f12-4714-bb4f-015e4b963b23");
                                    // usermap.put("thumb_image", "default");
                                   // db1=FirebaseDatabase.getInstance().getReference().child("pass");
                                    //db1.child(auth.getCurrentUser().getUid()).child("passwordds").setValue(password);
                                    // usermap.put("you","You");
                                    //blog creating
                                    // usermap.put("title","post!");
                                    // usermap.put("desc","Hey its post");
                                    //usermap.put("blog_image","https://firebasestorage.googleapis.com/v0/b/beyou-a7151.appspot.com/o/profile_images%2FZbqQPAbUPIeAKPkMEXaIA8WU7rn2.jpg?alt=media&token=1bcc9ef7-519c-4707-8341-b7fec546d159");
                                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(Hotel_signup.this, WelcomeHotelOwners.class));
                                                finish();
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
