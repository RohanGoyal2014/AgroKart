package com.example.rohangoyal2014.agrokart;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.rohangoyal2014.agrokart.Utils.mAuth;

public class ViewActivity extends AppCompatActivity {


    ListAdapter listAdapter;

    LinearLayout onlineLayout,offlineLayout;

    ListView onlineList,offlineList;

    ArrayList<String> itemNames=new ArrayList<>();

    ArrayList<ItemModel> onlineArrayList=new ArrayList<>();
    ArrayList<ItemModel> offlineArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        listAdapter=new ListAdapter(ViewActivity.this,onlineArrayList);


        itemNames.add("apple");
        itemNames.add("banana");
        itemNames.add("orange");
        itemNames.add("wheat");
        itemNames.add("rice");
        itemNames.add("dal");
        itemNames.add("potato");
        itemNames.add("onion");
        itemNames.add("cucumber");

        getSupportActionBar().setTitle("उत्पाद देखें");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onlineLayout=findViewById(R.id.online);
        onlineList=findViewById(R.id.online_list);

        offlineLayout=findViewById(R.id.offline);
        offlineList=findViewById(R.id.offline_list);

        onlineList.setAdapter(listAdapter);

        if(Utils.isNetworkAvailable(this)){

            queryRecOnline(0);

        } else {
            onlineLayout.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences=getSharedPreferences("tempData",MODE_PRIVATE);

        Set<String> st=sharedPreferences.getStringSet("items",new HashSet<String>());

        if(st.isEmpty()){
//            Toast.makeText(this, getString(R.string.no_results), Toast.LENGTH_SHORT).show();
        } else {
            for(String s:st){
                offlineArrayList.add(new ItemModel(s,
                        sharedPreferences.getString(s+"-seller",""),
                        sharedPreferences.getString(s+"-qty",""),
                        sharedPreferences.getString(s+"-cost",""),
                        sharedPreferences.getString(s+"-unit","")
                ));
            }
            ListAdapter listAdapter1=new ListAdapter(this,offlineArrayList);
            offlineList.setAdapter(listAdapter1);
            listAdapter1.notifyDataSetChanged();
        }

    }

    public void queryRecOnline(final int pos){
        FirebaseDatabase.getInstance().getReference().child("products").child(itemNames.get(pos)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        onlineArrayList.add(new ItemModel(ds.child("name").getValue().toString(),
                                mAuth.getCurrentUser().getEmail(),
                                ds.child("qty").getValue().toString(),
                                ds.child("cost").getValue().toString(),
                                ds.child("unit").getValue().toString()
                        ));
                    }
                } catch (Exception e){
                    Log.e("ViewActivity",e.toString());
                }
                listAdapter.notifyDataSetChanged();
                if(pos<itemNames.size()-1){
                    queryRecOnline(pos+1);
                    if(onlineArrayList.isEmpty()){
//                        Toast.makeText(ViewActivity.this, getString(R.string.no_results), Toast.LENGTH_SHORT).show();
                    } else {

                        listAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(pos<itemNames.size()-1){
                    queryRecOnline(pos+1);
                }
            }
        });
    }
}
