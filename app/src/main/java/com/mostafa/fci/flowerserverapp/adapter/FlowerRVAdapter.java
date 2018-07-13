package com.mostafa.fci.flowerserverapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mostafa.fci.flowerserverapp.Classes.Flower;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.Utillities.Dialog;
import com.mostafa.fci.flowerserverapp.Utillities.Network;
import com.mostafa.fci.flowerserverapp.interfaces.OnItemClickListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class FlowerRVAdapter extends RecyclerView.Adapter<FlowerRVAdapter.RVHolder> {


    Context context;
    ArrayList<Flower> arrayList;
    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            showDialog(arrayList.get(position));
        }
    };

    private StorageReference storageReference;

    public FlowerRVAdapter(Context context, ArrayList<Flower> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();
    }



    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_child_recyclerview, parent, false);
        holder = new RVHolder(view , onItemClickListener);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        Flower flower;
        flower = arrayList.get(position);
        holder.flowerName.setText(flower.getName());
        holder.flowerPrice.setText(flower.getPrice() + "$");
        holder.flowerCategory.setText(flower.getCategory());

        if(Network.isOnLine(context)) {
            storageReference.child( "flowers/"+flower.getPhoto() )
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.loading)
                            .into(holder.flowerImage);
                }
            });

        }else{
            Dialog.show(context);
        }

    }

    private void showDialog(final Flower flower){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("DELETE FLOWER")
                .setMessage("Do you really want to delete Flower")
                .setIcon(R.drawable.ic_delete_red_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItemPosition(flower);
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }

    private void removeItemPosition(Flower flower){
        int index = arrayList.indexOf(flower);
        arrayList.remove(index);
        notifyItemRemoved(index);

        new DBServices().removeOrder(flower);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     * RecyclerView.ViewHolder
     * **/

    public static class RVHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener

    {

        int type;
        ImageView flowerImage;
        TextView flowerName, flowerPrice, flowerCategory;
        OnItemClickListener listener;
        public RVHolder(View itemView,OnItemClickListener clickListener){
            super(itemView);
            this.listener = clickListener;
            itemView.setOnLongClickListener(this);
            flowerImage = itemView.findViewById(R.id.imageChildFlower);
            flowerName = itemView.findViewById(R.id.flowerNameChildItem);
            flowerPrice = itemView.findViewById(R.id.flowerPriceChildItem);
            flowerCategory = itemView.findViewById(R.id.flowerCategoryChildItem);
        }


        @Override
        public boolean onLongClick(View view) {
            if (this.listener != null)
                this.listener.onItemClick(view,this.getLayoutPosition());
            return true;
        }
    }

}
