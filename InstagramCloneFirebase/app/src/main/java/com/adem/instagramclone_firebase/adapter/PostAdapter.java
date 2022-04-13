package com.adem.instagramclone_firebase.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adem.instagramclone_firebase.R;
import com.adem.instagramclone_firebase.databinding.RecyclerRowBinding;
import com.adem.instagramclone_firebase.model.Posts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

   public ArrayList<Posts> postsArrayList;
    public PostAdapter(ArrayList<Posts> postsArrayList){
        this.postsArrayList=postsArrayList;
    }


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);



        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.recyclerRowBinding.homeEmailText.setText(postsArrayList.get(position).getEmail());
        holder.recyclerRowBinding.homeCommentText.setText(postsArrayList.get(position).getComment());
        Picasso.get().load(postsArrayList.get(position).getDownloadUrl()).into(holder.recyclerRowBinding.homeImageView);


    }

    @Override
    public int getItemCount() {

        return postsArrayList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;
        int like=0;


        public PostHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding=recyclerRowBinding;


            recyclerRowBinding.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (like==0) {
                        recyclerRowBinding.likeButton.setBackgroundResource(R.drawable.like);
                        like=1;
                    }
                    else if (like==1){
                        recyclerRowBinding.likeButton.setBackgroundResource(R.drawable.unlike);
                        like=0;
                    }
                }
            });

        }



    }


}
