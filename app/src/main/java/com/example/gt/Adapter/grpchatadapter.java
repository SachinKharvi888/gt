package com.example.gt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gt.R;
import com.example.gt.model.Chat;
import com.example.gt.model.User;
import com.example.gt.model.grpchatmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class grpchatadapter extends RecyclerView.Adapter<grpchatadapter.ViewHolder>{

    public  static  final int MSG_TYPE_LEFT = 0;
    public  static  final int MSG_TYPE_RIGHT = 1;
    private Context mcontext;
    private List<grpchatmodel> mChat;
    FirebaseUser fuser;
    DatabaseReference reference,msgDelete;
    DatabaseReference referenc;
    grpchatmodel chat ;

    public grpchatadapter(Context mcontext, List<grpchatmodel>mChat){
        this.mcontext=mcontext;
        this.mChat=mChat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right,parent,false);
            return new grpchatadapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,parent,false);
            return new grpchatadapter.ViewHolder(view);
        }    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        grpchatmodel m = mChat.get(position);

        holder.show_message.setText(m.getMessage());
       // holder.name.setText(m.getName());


         // holder.name.setText(m.getName());
        // holder.show_message11.setText(m.getMessage());
        ////holder.text_seen.setText(m.getName());

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            holder.text_seen.setText(m.getName());

        }else {
            holder.name.setText(m.getName());
            holder.profile_image.setVisibility(View.GONE);

        }
        Toast.makeText(mcontext, m.getName(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message,show_message11;
        public ImageView profile_image,likeImage;
        public  TextView text_seen,tex;
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
      show_message=itemView.findViewById(R.id.show_message);
          //  show_message11=itemView.findViewById(R.id.show_message11);

             profile_image=itemView.findViewById(R.id.profile_images);
           //
            //
             text_seen=itemView.findViewById(R.id.text_seen);
          //  likeImage=itemView.findViewById(R.id.likeimage);
          //  tex=itemView.findViewById(R.id.likeimage3);
            name=itemView.findViewById(R.id.grpusername);
        }
    }
    @Override
    public int getItemViewType(int position) {

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){

            return  MSG_TYPE_RIGHT;
        }else {
            return  MSG_TYPE_LEFT;
        }
    }
}
