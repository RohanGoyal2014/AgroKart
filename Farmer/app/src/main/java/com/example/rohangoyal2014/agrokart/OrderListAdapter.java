package com.example.rohangoyal2014.agrokart;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<OrderModel> {

    public OrderListAdapter(Context context,ArrayList<OrderModel> arrayList){
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView= convertView;
        if(listView==null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout_2, parent, false);
        }
        final OrderModel currModel=getItem(position);
        TextView whoView=listView.findViewById(R.id.buyerEmail);
        TextView qtyAmtView=listView.findViewById(R.id.qty);
        TextView whatView=listView.findViewById(R.id.what);

        whoView.setText(currModel.getPurchaser());
        qtyAmtView.setText(currModel.getQty()+" @ "+currModel.getAmt());
        whatView.setText(currModel.getItemName());

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        return listView;

    }
}
