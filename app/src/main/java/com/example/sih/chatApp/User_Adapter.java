package com.example.sih.chatApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.cloud.translate.Translate;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<usercardview> details;
    ArrayList<usercardview> fullDetails;
    Translate translate;
    String check;


    public User_Adapter(ArrayList<usercardview> d, Context c) {
        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.Username.setText(details.get(position).getUsername());
            holder.Name.setText(details.get(position).getName());
            holder.Number.setText(details.get(position).getPhone());
            Thread thread = new Thread() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getImage(details.get(position).getPhone(), holder);
                            } catch(Exception e){

                            }

                        }
                    });
                }
            };
            thread.start();

//            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    Intent intent = new Intent(context, Chat.class);
                    String phone = details.get(position).getPhone();
                    String username = details.get(position).getUsername();
                    intent.putExtra("Phone", phone);
                    intent.putExtra("Username", username);
                    view.getContext().startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details ==null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Username, Name, Number;
        ImageView Profile_Picture;
        String M, J;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            Username = itemView.findViewById(R.id.username);
            Name = itemView.findViewById(R.id.person_name);
            Number = itemView.findViewById(R.id.number);
            Profile_Picture = itemView.findViewById(R.id.profile_picture);
            check = preferences.getString("Lang","Eng");
        }
    }

    @Override
    public Filter getFilter() {
        return detailsFilter;
    }

    private Filter detailsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<usercardview> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (usercardview item : fullDetails){
                    try {
                        if (item.getUsername().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getUsername().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getPhone().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            details.clear();
            details.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void getImage(String user, final MyViewHolder holder){
        final Bitmap[] bm = new Bitmap[1];
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(user + "/Profile Picture");
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bm[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.Profile_Picture.setImageBitmap(bm[0]);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

}
