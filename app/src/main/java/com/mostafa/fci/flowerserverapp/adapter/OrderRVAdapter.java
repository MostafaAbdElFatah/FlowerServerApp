package com.mostafa.fci.flowerserverapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;


import com.mostafa.fci.flowerserverapp.Classes.Order;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.interfaces.NotifyDataChanged;
import com.mostafa.fci.flowerserverapp.interfaces.OnItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderRVAdapter extends RecyclerView.Adapter<OrderRVAdapter.RVHolder> {


    Context context;
    ArrayList<Order> arrayList;
    OnItemClickListener listener = null;
    public OrderRVAdapter(Context context, ArrayList<Order> arrayList ) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_child_recyclerview, parent, false);
        holder = new RVHolder(view,this.listener);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RVHolder holder, final int position) {
        final Order order;
        order = arrayList.get(position);
        holder.userNameTextView.setText(order.getUser().getName());
        holder.phoneTextView.setText(order.getUser().getPhone());
        holder.addressTextView.setText(order.getUser().getAddress());

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double orderPrice = Double.valueOf(twoDForm.format( order.getTotalPrice()));

        holder.orderPriceTextView.setText( orderPrice  + "$");
        holder.orderPaymentTextView.setText(order.getPayment());
        holder.flowerNameTextView.setText(order.getFlowerName());
        holder.orderQuantityTextView.setText( "Quantity : "+ order.getQuantity() );


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shoke);
                holder.imageButton.startAnimation(animShake);
                showDialog(order);
            }
        });
    }

    private void removeItemPosition(Order order){
        int index = arrayList.indexOf(order);
        arrayList.remove(index);
        notifyItemRemoved(index);
        new DBServices().acceptOrder(order);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private void showDialog(final Order order){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Accept Order")
                .setMessage("Do you really want to Accept Order")
                .setIcon(R.drawable.accept);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItemPosition(order);
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }
    /**
     * RecyclerView.ViewHolder
     **/

    public static class RVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageButton imageButton;
        TextView userNameTextView, orderPriceTextView , flowerNameTextView
                , orderQuantityTextView , orderPaymentTextView
                , phoneTextView , addressTextView;
        OnItemClickListener listener;
        public RVHolder(View itemView,OnItemClickListener clickListener){
            super(itemView);
            this.listener = clickListener;
            imageButton   = itemView.findViewById(R.id.acceptBtn);
            imageButton.setOnClickListener(this);

            userNameTextView      = itemView.findViewById(R.id.userNameOrderChildItem);
            flowerNameTextView    = itemView.findViewById(R.id.flowerNameOrderChildItem);
            orderPriceTextView    = itemView.findViewById(R.id.priceOrderChildItem);
            orderQuantityTextView = itemView.findViewById(R.id.quantityOrderChildItem);
            orderPaymentTextView  = itemView.findViewById(R.id.paymentOrderChildItem);
            phoneTextView         = itemView.findViewById(R.id.priceOrderChildItem);
            addressTextView       = itemView.findViewById(R.id.addressOrderChildItem);
        }

        @Override
        public void onClick(View view) {
            if (this.listener != null)
                this.listener.onItemClick(view,this.getLayoutPosition());
        }
    }
}


