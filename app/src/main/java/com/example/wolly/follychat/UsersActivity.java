package com.example.wolly.follychat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Follies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabse = FirebaseDatabase.getInstance().getReference().child("Users");



        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(

                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabse

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                viewHolder.setDisplayName(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                viewHolder.setUserImage(model.getThumb_image(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });
            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDisplayName(String name)
        {
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }

        public void setUserStatus(String status)
        {
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }

        public  void setUserImage(String thumb_image, Context a)
        {
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(a).load(thumb_image).placeholder(R.drawable.ic).into(userImageView);
        }

    }
}
