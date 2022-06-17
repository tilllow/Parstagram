package com.example.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.Viewholder> {
    Context context;
    ArrayList<Post> posts;

    FeedAdapter(Context context, ArrayList<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView ivProfilePhoto;
        ImageView ivPostPhoto;
        TextView tvUserName;
        TextView tvDescription;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePicture);
            ivPostPhoto = itemView.findViewById(R.id.ivPostPhoto);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Post post = posts.get(pos);
                    Intent i = new Intent(context,PostDetailsActivity.class);
                    i.putExtra("EXTRA_POST",Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }

        private void bind(Post post) {
            tvUserName.setText(post.getUser().toString());
            tvDescription.setText(post.getDescription());
            ParseFile image = post.getImage();
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(ivPostPhoto);
            }

            ivProfilePhoto.setImageResource(R.drawable.mickeymouse);
        }
    }

    public void clear(){
        posts.clear();
        this.notifyDataSetChanged();
    }

    public void AddAll(List<Post> list){
        posts.addAll(list);
    }


}
