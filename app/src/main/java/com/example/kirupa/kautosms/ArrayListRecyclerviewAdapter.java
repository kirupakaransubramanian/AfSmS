package com.example.kirupa.kautosms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ArrayListRecyclerviewAdapter extends RecyclerView.Adapter<ArrayListRecyclerviewAdapter.UsersViewHolder>{
    Context context;
    ArrayList<String> phoneNumber;

    public ArrayListRecyclerviewAdapter(Context context, ArrayList<String> dataArray) {
        this.phoneNumber = dataArray;
        this.context = context;
    }
    @Override
    public ArrayListRecyclerviewAdapter.UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recent_number, null);
        ArrayListRecyclerviewAdapter.UsersViewHolder usersViewHolder = new ArrayListRecyclerviewAdapter.UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, final int position) {

        holder.number.setText(phoneNumber.get(position));


        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.phoneNumbers.remove(position);
                MainActivity.refresh();
            }
        });
    }

    @Override
    public int getItemCount() {
       return phoneNumber.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageview;
        public TextView number;

        public UsersViewHolder(View itemView) {
            super(itemView);

            imageview = (ImageView) itemView.findViewById(R.id.img_delete);
            number = (TextView) itemView.findViewById(R.id.txt_title);


        }
    }

}
