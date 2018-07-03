package com.example.kiran.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roger.catloadinglibrary.CatLoadingView;
import com.victor.loading.newton.NewtonCradleLoading;

public class Menu_or_Review extends AppCompatActivity {
    private ImageView Menu,Feedback,background;
    private CatLoadingView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_or__review);
        final String user_id=getIntent().getStringExtra("user_id");
        Menu=(ImageView)findViewById(R.id.menu_imageview);
        Feedback=(ImageView)findViewById(R.id.feeback_imageview);
       // background=(ImageView)findViewById(R.id.background_imageview);
       // background.setBackgroundResource(R.drawable.buffet);
        //background.getBackground().setAlpha(190);
        mView = new CatLoadingView();
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                mView.dismiss();
                Intent menu_intent=new Intent(Menu_or_Review.this, com.example.kiran.foodie.Menu.class);
                menu_intent.putExtra("user_id",user_id);
                startActivity(menu_intent);


            }

            private void showDialog() {
                mView.show(getSupportFragmentManager(), "Loading");
            }
        });
        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackintent=new Intent(Menu_or_Review.this, com.example.kiran.foodie.Feedback.class);
                feedbackintent.putExtra("user_id",user_id);
                startActivity(feedbackintent);
                finish();
            }
        });

    }
}
