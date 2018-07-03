package com.example.kiran.foodie;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roger.catloadinglibrary.CatLoadingView;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import android.support.annotation.Nullable;
import android.annotation.TargetApi;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindViews;
import butterknife.ButterKnife;


public  class SignUpFragment extends AuthFragment{
    private TextInputLayout email,password,re_password;
    private Button btn_sign_up;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ProgressDialog mprogress;
    @BindViews(value = {R.id.email_input_edit,
            R.id.password_input_edit})
    protected List<TextInputEditText> views;
    private CatLoadingView mView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mprogress=new ProgressDialog(getContext());
        mView = new CatLoadingView();
        if(view!=null){
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_sign_up));
            caption.setText(getString(R.string.sign_up_label));
            email=(TextInputLayout) ButterKnife.findById(view,R.id.email_input);
            password=(TextInputLayout)ButterKnife.findById(view,R.id.password_input);
            btn_sign_up=(Button)ButterKnife.findById(view,R.id.btn_signup);


             final AutoCompleteTextView textView = (AutoCompleteTextView) ButterKnife.findById(view,R.id.autocomplete_gender);
            // Get the string array
            String[] categories= getResources().getStringArray(R.array.category_array);
            // Create the adapter and set it to the AutoCompleteTextView
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categories);
            textView.setAdapter(adapter);
           /* views.forEach(editText->{
                if(editText.getId()==R.id.password_input_edit){
                    final TextInputLayout inputLayout= ButterKnife.findById(view,R.id.password_input);
                    final TextInputLayout confirmLayout=ButterKnife.findById(view,R.id.confirm_password);
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    inputLayout.setTypeface(boldTypeface);
                    confirmLayout.setTypeface(boldTypeface);
                    editText.addTextChangedListener(new TextWatcherAdapter(){
                        @Override
                        public void afterTextChanged(Editable editable) {
                            inputLayout.setPasswordVisibilityToggleEnabled(editable.length()>0);
                        }
                    });
                }
                editText.setOnFocusChangeListener((temp,hasFocus)->{
                    if(!hasFocus){
                        boolean isEnabled=editText.getText().length()>0;
                        editText.setSelected(isEnabled);
                    }
                });
            });*/
         ////////////////////////////////////
            btn_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {

                    String email_input =email.getEditText().getText().toString().trim();
                    String passswd_input = password.getEditText().getText().toString().trim();
                    String category=textView.getText().toString().trim();
                    int index=email_input.indexOf('@');
                    String username=email_input.substring(0,index);
                   // Toast.makeText(getContext(),"this is username:"+username,Toast.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(email_input)) {
                        Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(passswd_input)) {
                        Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                   /* if(!(confirm_pswd_input ==passswd_input)){
                        Toast.makeText(getContext(),"Password and cofirm password are not matching!",Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                  /*  mprogress.setTitle("Signing Up...");
                    mprogress.setMessage("Please wait..You will be logged in after signing up");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();*/
                    showDialog();
                    register_user(email_input,passswd_input,username,category);

                }

                private void showDialog() {
                    mView.show(getFragmentManager(), "");
                    mView.setCanceledOnTouchOutside(false);
                }

                private void register_user(String email_input, String passswd_input, final String username, final String category) {
                    auth.createUserWithEmailAndPassword(email_input,passswd_input).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // String currentuser_id=auth.getCurrentUser().getUid();
                                String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                                // mdevicetokendatabase.child(currentuser_id).child("device_token").setValue(DeviceToken);
                                //putting uid and data in realtime database
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = currentUser.getUid();

                                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                                HashMap<String, String> usermap = new HashMap<>();
                                usermap.put("name",username);
                                usermap.put("device_token",DeviceToken);
                                usermap.put("category",category);
                              //  usermap.put("image","https://firebasestorage.googleapis.com/v0/b/foodie-d7b9e.appspot.com/o/profile_image%2Fmale.png?alt=media&token=9b59316b-52b3-4063-873c-a6b7cc949944");
                                mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mView.dismiss();
                                            Intent intent;
                                            intent = new Intent(getContext(),MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                        }
                                    }
                                });


                            }




                        }
                    });
                }
            });
           ///////////////////////////////////////////
            caption.setVerticalText(true);
            foldStuff();
        }
    }

    @Override
    public int authLayout() {
        return R.layout.sign_up_fragment;
    }



    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fold() {
        lock=false;
        caption.requestLayout();
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        final TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        final float padding=getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        caption.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(parent, set);
                SignUpFragment.this.foldStuff();
                caption.setTranslationX(-caption.getWidth() / 8 + padding);
            }
        });
    }

    @Override
    public void clearFocus() {

    }

    private void foldStuff(){
        //caption.setEnabled(true);
       caption.setVisibility(View.VISIBLE);
        caption.setText(getString(R.string.New_User));
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight=ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }
}
