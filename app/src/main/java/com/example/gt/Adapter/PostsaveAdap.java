package com.example.gt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gt.R;
import com.example.gt.model.Post;

import java.util.List;

public class PostsaveAdap extends RecyclerView.Adapter<PostsaveAdap.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public PostsaveAdap(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public PostsaveAdap.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.postfoot, parent, false);
        return new PostsaveAdap.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsaveAdap.ImageViewHolder holder, final int position) {

        final Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPostimage()).into(holder.post_image);
        //holder.post_image.setImageResource(R.drawable.profileus);

        //    holder.post_image.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //    public void onClick(View view) {
        //  SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        //   editor.putString("postid", post.getPostid());
        //  editor.apply();

        // ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
        //     new PostDetailFragment()).commit();
        //     }
        // });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;


        public ImageViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_imagess);

        }
    }

}
