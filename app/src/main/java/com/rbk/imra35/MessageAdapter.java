package com.rbk.imra35;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Linus on 07/07/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersDatabase;


    public MessageAdapter(List<Messages> userMessagesList) {

        this.userMessagesList = userMessagesList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout_user, parent, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        String message_sender_id = mAuth.getCurrentUser().getUid();


        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();

        String fromMessageType = messages.getType();

        UsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);

        UsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("fullname").getValue().toString();
                String userImage = dataSnapshot.child("profileimage").getValue().toString();

                Picasso.with(holder.userProfileImage.getContext()).load(userImage).placeholder(R.drawable.profile).into(holder.userProfileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (fromMessageType.equals("text"))
        {

            if (fromUserId.equals(message_sender_id))
            {

                //holder.messagePicture.setVisibility(View.INVISIBLE);

                holder.messageText.setBackgroundResource(R.drawable.messages_text_background_two);

                holder.messageText.setTextColor(Color.BLACK);

                holder.messageText.setGravity(Gravity.RIGHT);


            }
            else
            {

                holder.messageText.setBackgroundResource(R.drawable.message_text_background);

                holder.messageText.setTextColor(Color.WHITE);

                holder.messageText.setGravity(Gravity.LEFT);


            }


            holder.messageText.setText(messages.getMessage());


        }
        else
        {

            holder.messageText.setVisibility(View.GONE);
            holder.messageText.setPadding(0, 0, 0, 0);

            //holder.messagePicture.setForegroundGravity(22);
            holder.messagePicture.setVisibility(View.VISIBLE);


            Picasso.with(holder.userProfileImage.getContext()).load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.messagePicture);


        }


    }

    @Override
    public int getItemCount()
    {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {

        public TextView messageText;
        public CircleImageView userProfileImage;
        public ImageView messagePicture;

        public MessageViewHolder(View view)
        {

            super(view);

            messageText = view.findViewById(R.id.message_text);
            messagePicture = view.findViewById(R.id.message_image_view);
            userProfileImage = view.findViewById(R.id.messages_profile_images);
        }

    }

}



