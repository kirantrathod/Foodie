package com.example.kiran.foodie;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    private String mchatuser;
    private DatabaseReference mRootref;
    private FirebaseAuth mAuth;
    private String mCurrentuserid;
    private EditText mchat_messageview;
    private ImageButton msend_message_btn;
    private ImageButton mAdd_image_btn;
    private RecyclerView mMessagelist;
    private SwipeRefreshLayout mRefreshlayout;
    private final List<reviews> feedbacklist =new ArrayList<reviews>();
    private LinearLayoutManager mlinearlayout;
    private FeedbackAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mAuth=FirebaseAuth.getInstance();

        mRootref= FirebaseDatabase.getInstance().getReference();

        mCurrentuserid=mAuth.getCurrentUser().getUid();
        mchatuser=getIntent().getStringExtra("user_id");
        mchat_messageview=(EditText)findViewById(R.id.write_message);
        msend_message_btn=(ImageButton)findViewById(R.id.send_message);
        mAdd_image_btn=(ImageButton)findViewById(R.id.addimage);
        mAdapter=new FeedbackAdapter(feedbacklist);
        mMessagelist=(RecyclerView)findViewById(R.id.messageslist);
        mRefreshlayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh_layout);
        mlinearlayout=new LinearLayoutManager(this);
        mMessagelist.setHasFixedSize(true);
        mMessagelist.setLayoutManager(mlinearlayout);
        mMessagelist.setAdapter(mAdapter);
        loadMessages();

        msend_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });
    }
    private void loadMessages() {
        DatabaseReference messageRef=mRootref.child("feedbacks").child(mchatuser);
        Query messageQuery=messageRef.limitToLast(1000);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                reviews message1=dataSnapshot.getValue(reviews.class);
                feedbacklist.add(message1);
                mAdapter.notifyDataSetChanged();
                mMessagelist.scrollToPosition(feedbacklist.size() -1);
                mRefreshlayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    private void sendMessage() {
        String feedback=mchat_messageview.getText().toString();
        if (!TextUtils.isEmpty(feedback)){
            String current_user_ref="feedbacks/" + mCurrentuserid + "/" +mchatuser;
            String chat_user_ref="feedbacks/" + mchatuser;

            DatabaseReference user_message_push=mRootref.child("feedbacks").child(mCurrentuserid).child(mchatuser)
                    .push();
            String push_id=user_message_push.getKey();
//=============================================================================================================================
            Map messagemap=new HashMap();
            messagemap.put("feedback",feedback);
            messagemap.put("seen",false);
            messagemap.put("type","text");
            messagemap.put("time", ServerValue.TIMESTAMP);
            messagemap.put("to",mchatuser);
            messagemap.put("from",mCurrentuserid);
            Map message_user_map=new HashMap();
            message_user_map.put(current_user_ref +"/" + push_id,messagemap);
            message_user_map.put(chat_user_ref + "/ " + push_id,messagemap);
            mchat_messageview.setText(" ");

            DatabaseReference newNotificationref=mRootref.child("feedback_notification").child(mchatuser).push();
            String newNotificationid=newNotificationref.getKey();

            HashMap<String,String> notificationData=new HashMap<String, String>();
            notificationData.put("from",mCurrentuserid);
            notificationData.put("seen","false");
            notificationData.put("feedback",feedback);
            Map requestmap=new HashMap();
            requestmap.put("feedback_notification/" +mchatuser +"/" +newNotificationid,notificationData);

            mRootref.updateChildren(requestmap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                }
            });

            try {

                mMessagelist.smoothScrollToPosition(feedbacklist.size() - 1);
            } catch (Exception e){
                e.printStackTrace();
            }
            mRootref.updateChildren(message_user_map, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError!=null){
                        Log.d("CHAT_LOG",databaseError.getMessage().toString());
                    }
                }
            });



        }
    }
}
