package com.mostafa.fci.flowerserverapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.interfaces.OnItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DerviryRVAdapter extends RecyclerView.Adapter<DerviryRVAdapter.RVHolder> {


    Context context;
    ArrayList<Order> arrayList;
    public DerviryRVAdapter(Context context, ArrayList<Order> arrayList ) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public DerviryRVAdapter.RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        DerviryRVAdapter.RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.derviry_child_recyclerview, parent, false);
        holder = new DerviryRVAdapter.RVHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(DerviryRVAdapter.RVHolder holder, final int position) {
        final Order order;
        order = arrayList.get(position);
        holder.userNameTextView.setText(order.getUser().getName());
        holder.phoneTextView.setText(order.getUser().getPhone());
        holder.addressTextView.setText(order.getUser().getAddress());

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double orderPrice = Double.valueOf(twoDForm.format( order.getTotalPrice()));

        holder.orderPriceTextView.setText( orderPrice  + "$");
        holder.orderPaymentTextView.setText(order.getPayment());
        holder.orderIdTextView.setText(order.getId());
        holder.orderQuantityTextView.setText( "Quantity : "+ order.getQuantity() );

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public static class RVHolder extends RecyclerView.ViewHolder {

        TextView orderIdTextView , userNameTextView, orderPriceTextView
                , orderQuantityTextView , orderPaymentTextView
                , phoneTextView , addressTextView;
        public RVHolder(View itemView){
            super(itemView);

            orderIdTextView      = itemView.findViewById(R.id.orderidDerviryChildItem);
            userNameTextView     = itemView.findViewById(R.id.userNameDerviryChildItem);
            orderPriceTextView   = itemView.findViewById(R.id.priceDerviryChildItem);
            orderQuantityTextView = itemView.findViewById(R.id.quantityDerviryChildItem);
            orderPaymentTextView    = itemView.findViewById(R.id.paymentDerviryChildItem);
            phoneTextView   = itemView.findViewById(R.id.priceDerviryChildItem);
            addressTextView = itemView.findViewById(R.id.addressDerviryChildItem);
        }

    }
}


