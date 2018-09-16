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

public class ListAdapter extends ArrayAdapter<ItemModel> {

    public ListAdapter(Context context,ArrayList<ItemModel> arrayList){
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView= convertView;
        if(listView==null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }
        final ItemModel currModel=getItem(position);
        TextView nameView=listView.findViewById(R.id.itemName);
        TextView qtyRateView=listView.findViewById(R.id.qty);
        ImageView imageView=listView.findViewById(R.id.itemImage);

        nameView.setText(Utils.byIdName(getContext(),currModel.getName()));
        qtyRateView.setText(currModel.getQty()+Utils.byIdName(getContext(),currModel.getUnit())+" @ "+currModel.getCost());
        switch (currModel.getName()){
            case "apple":
                imageView.setImageResource(R.drawable.apple);
                break;
            case "banana":
                imageView.setImageResource(R.drawable.banana);
                break;
            case "orange":
                imageView.setImageResource(R.drawable.orange);
                break;
            case "wheat":
                imageView.setImageResource(R.drawable.wheat);
                break;
            case "rice":
                imageView.setImageResource(R.drawable.rice);
                break;
            case "dal":
                imageView.setImageResource(R.drawable.dal);
                break;
            case "onion":
                imageView.setImageResource(R.drawable.onion);
                break;
            case "cucumber":
                imageView.setImageResource(R.drawable.cucumber);
                break;
            default:
                imageView.setImageResource(R.drawable.potato);

        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        return listView;

    }
}
