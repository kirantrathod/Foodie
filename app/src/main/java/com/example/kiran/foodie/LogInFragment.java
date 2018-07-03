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
import android.text.TextUtils;
import android.text.TextWatcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roger.catloadinglibrary.CatLoadingView;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionInflater;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public  class LogInFragment extends AuthFragment{
    private TextInputLayout inputemail,inputpasswd;
//   private TextInputEditText inputemail,inputpasswd;
    private Button btn_log_in;
    private TextView hotel_btnlogin;
    private FirebaseAuth auth;
    private ProgressDialog mprogressBar;
    private DatabaseReference mdevicetokendatabase;
    private CatLoadingView mView;

    @BindViews(value = {R.id.email_input_edit,R.id.password_input_edit})
    protected List<TextInputEditText> views;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
       // mprogressBar=new ProgressDialog(getContext());
        mView = new CatLoadingView();
        if(view!=null){
            caption.setText(getString(R.string.log_in_label));
            view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_log_in));

        inputemail=(TextInputLayout)ButterKnife.findById(view,R.id.email_input);
            inputpasswd=(TextInputLayout) ButterKnife.findById(view,R.id.password_input);
            btn_log_in=(Button)ButterKnife.findById(view,R.id.btn_login);
            hotel_btnlogin=(TextView) ButterKnife.findById(view,R.id.hotel_loginclick);

            mdevicetokendatabase= FirebaseDatabase.getInstance().getReference().child("users");
      /*   views.forEach(editText -> {
             if (editText.getId() == R.id.password_input_edit) {
                 final TextInputLayout inputLayout = ButterKnife.findById(view, R.id.password_input);
                 Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                 inputLayout.setTypeface(boldTypeface);
                 editText.addTextChangedListener(new TextWatcherAdapter() {
                     @Override
                     public void afterTextChanged(Editable editable) {
                         inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
                     }
                 });
             }
             editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View temp, boolean hasFocus) {
                     if (!hasFocus) {
                         boolean isEnabled = editText.getText().length() > 0;
                         editText.setSelected(isEnabled);
                     }
                 }
             });
         });*/
//////////////////log in action
            hotel_btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1=new Intent(getContext(),Hotel_login.class);
                    startActivity(intent1);
                }
            });

            btn_log_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {

                    String email = inputemail.getEditText().getText().toString();
                    String passswd = inputpasswd.getEditText().getText().toString();

                    int index=email.indexOf('@');
                    String username=email.substring(0,index);
                    //Toast.makeText(getContext(),"this is username:"+username,Toast.LENGTH_SHORT).show();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(passswd)) {
                        Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*mprogressBar.setTitle("Logging In...");
                    mprogressBar.setMessage("Please wait while loading!");
                    mprogressBar.setCanceledOnTouchOutside(false);
                    mprogressBar.show();*/

                    showDialog();

                    auth.signInWithEmailAndPassword(email,passswd).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // there was an error
                                //mprogressBar.dismiss();
                                mView.dismiss();

                                    Toast.makeText(getContext(),"Authentication failed", Toast.LENGTH_LONG).show();
                                }
                             else {

                                String currentuser_id=auth.getCurrentUser().getUid();
                                String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                                mdevicetokendatabase.child(currentuser_id).child("device_token").setValue(DeviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                          //  mprogressBar.dismiss();
                                            mView.dismiss();
                                            Intent intent4 = new Intent(getContext(), MainActivity.class);
                                            startActivity(intent4);
                                            getActivity().finish();


                                        }
                                        else{
                                            Toast.makeText(getContext(),"Can not establish Your currnt device token to database!",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });






                            }


                        }
                    });

                }

                private void showDialog() {
                    mView.show(getFragmentManager(), "");
                    mView.setCanceledOnTouchOutside(false);
                }
            });

            //////////////////////////////////////





        }
    }

  /*  @OnClick(R.id.caption)
    public void login(){
        Toast.makeText(getContext(),"this is trial of onclick",Toast.LENGTH_SHORT).show();
    }*/
    @Override
    public int authLayout() {
        return R.layout.login_fragment;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void fold() {
        lock=false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        caption.setVisibility(View.VISIBLE);
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
        final float padding=getResources().getDimension(R.dimen.folded_label_padding)/2;
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(-padding);
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        caption.post(new Runnable() {
            @Override
            public void run() {
                TransitionManager.beginDelayedTransition(parent, set);
                //   caption.setVisibility(View.VISIBLE);
                caption.setText(LogInFragment.this.getString(R.string.log_in_label));
                caption.setTextSize(TypedValue.COMPLEX_UNIT_PX, caption.getTextSize() / 2);
                caption.setTextColor(Color.WHITE);
                ConstraintLayout.LayoutParams params = LogInFragment.this.getParams();
                params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                params.verticalBias = 0.5f;
                caption.setLayoutParams(params);
                caption.setTranslationX(caption.getWidth() / 8 - padding);
            }
        });
    }

    @Override
    public void clearFocus() {

    }



}
