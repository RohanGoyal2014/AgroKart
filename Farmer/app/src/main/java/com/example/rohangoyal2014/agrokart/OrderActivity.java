package com.example.rohangoyal2014.agrokart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.rohangoyal2014.agrokart.Utils.mAuth;

public class OrderActivity extends AppCompatActivity {


    OrderListAdapter orderListAdapter;
    ArrayList<OrderModel> orderList=new ArrayList<>();

    ListView orderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderListAdapter=new OrderListAdapter(this,orderList);
        orderView=findViewById(R.id.orders);
        orderView.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();

        getSupportActionBar().setTitle("ग्राहक का ऑर्डर");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase.getInstance().getReference().child("users").child("farmers").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String phone= dataSnapshot.getValue().toString().trim().substring(1,11);
                FirebaseDatabase.getInstance().getReference().child("transfers").orderByChild("sellerEmail").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


//                        Log.e("OrderActivity",dataSnapshot.toString());

                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            final String qty=ds.child("quantity").getValue().toString();
                            final String item=ds.child("item").getValue().toString();
                            final String sellerPhone=ds.child("sellerEmail").getValue().toString();
                            final String purchaser=ds.child("email").getValue().toString();

                            FirebaseDatabase.getInstance().getReference().child("products").child(item).orderByChild("farmerEmail").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String amt="0";
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        amt=ds.child("cost").getValue().toString();
//                                        Log.e("ORders",amt);
                                        break;
                                    }
                                    String totalAmt=String.valueOf(Integer.parseInt(qty)*Integer.parseInt(amt));
                                    orderList.add(new OrderModel(Utils.byIdName(OrderActivity.this,item),qty,totalAmt,purchaser));
                                    orderListAdapter=new OrderListAdapter(OrderActivity.this,orderList);
                                    Log.e("Inside","here");
                                    orderView.setAdapter(orderListAdapter);
                                    orderListAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(OrderActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(OrderActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrderActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
