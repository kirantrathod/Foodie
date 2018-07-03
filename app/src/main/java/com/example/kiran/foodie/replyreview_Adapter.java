package com.example.kiran.foodie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class replyreview_Adapter extends RecyclerView.Adapter<replyreview_Adapter.Feedbackviewholder> {

    private List<reviews> mReviewsList;
    private DatabaseReference mUserDatabase;



    public replyreview_Adapter(List<reviews> mReviewsList){

        this.mReviewsList = mReviewsList;

    }

    @Override
    public replyreview_Adapter.Feedbackviewholder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_single_layout, parent, false);


        return new replyreview_Adapter.Feedbackviewholder(v);

    }
    public class Feedbackviewholder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;

        public Feedbackviewholder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);

        }

    }

    @Override
    public void onBindViewHolder(final Feedbackviewholder holder, int position) {
        reviews c = mReviewsList.get(position);
        String from_user = c.getFrom();
        String to=c.getTo();
        String message_type=c.getType();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("hotelowners").child(to);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                //String image = dataSnapshot.child("thumb_image").getValue().toString();
                holder.displayName.setText(name);
                // Picasso.with(holder.profileImage.getContext()).load(image).placeholder(R.mipmap.male).into(holder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.messageText.setText(c.getFeedback());


    }




    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



}
