package com.rbk.imra35;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity
{
    private RecyclerView FriendsList;
    private DatabaseReference FriendsRef,UsersRef;
    private FirebaseAuth mAuth;
    private String online_user_id;
    private FirebaseRecyclerAdapter<Friends,FriendsActivity.FriendsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mAuth=FirebaseAuth.getInstance();
        online_user_id=mAuth.getCurrentUser().getUid();

        FriendsRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        FriendsList=findViewById(R.id.friends_list);
        FriendsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        FriendsList.setLayoutManager(linearLayoutManager);


        FriendsList.post(new Runnable() {
            @Override
            public void run() {
                FriendsList.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());
            }
        });

        DisplayAllFriends();

    }

    private void DisplayAllFriends()
    {
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, FriendsActivity.FriendsViewHolder>
                (
                        Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsActivity.FriendsViewHolder.class,
                        FriendsRef

                )
        {
            @Override
            protected void populateViewHolder(final FriendsActivity.FriendsViewHolder viewHolder, Friends model, int position)
            {

                viewHolder.setDate(model.getDate());

                final String usersId=getRef(position).getKey();
                UsersRef.child(usersId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            final String userName=dataSnapshot.child("fullname").getValue().toString();
                            String profileImage=dataSnapshot.child("profileimage").getValue().toString();

                            viewHolder.setFullname(userName);
                            viewHolder.setProfileimage(getApplicationContext(),profileImage);

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(MessageActivity.this,ChatActivity.class).putExtra("visit_user_id",usersId).putExtra("userName",userName));
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
            }

        };

        FriendsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FriendsViewHolder(View itemView)
        {
            super(itemView);

            mView=itemView;

        }
        public void setFullname(String fullname){

            TextView userNameView=mView.findViewById(R.id.all_users_profile_full_name);
            userNameView.setText(fullname);

        }
        public void setDate(String date) {

            TextView userStatus=mView.findViewById(R.id.all_users_status);
            userStatus.setText("Friends Since: "+date);

        }
        public void setProfileimage(final Context ctx, final String profileimage) {
            final CircleImageView userThumbImage=mView.findViewById(R.id.all_users_profile_image);
            Picasso.with(ctx).load(profileimage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile).into(userThumbImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(userThumbImage);


                }
            });

        }
    }

}

