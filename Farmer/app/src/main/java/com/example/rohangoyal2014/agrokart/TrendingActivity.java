package com.example.rohangoyal2014.agrokart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("शीर्ष 3 फसल");

        final TextView cp[]={findViewById(R.id.cp1),
            findViewById(R.id.cp2),
            findViewById(R.id.cp3)
        };

        final ArrayList<Pair<Integer,String>> parr=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("searches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Toast.makeText(TrendingActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();

                for(DataSnapshot ds:dataSnapshot.getChildren()){

//                    Toast.makeText(TrendingActivity.this,
//                            String.valueOf(
//                                    ds.getValue().toString().substring(
//                                            7,
//                                            ds.getValue().toString().indexOf('}')
//                                    )
//                            )
//                    , Toast.LENGTH_SHORT).show();

                    Pair<Integer,String> pr=new Pair<>(Integer.parseInt(String.valueOf(
                            ds.getValue().toString().substring(
                                    7,
                                    ds.getValue().toString().indexOf('}')
                            )
                    )),Utils.byIdName(TrendingActivity.this,ds.getKey().toString()));
                    Log.e("Trending",String.valueOf(pr.first));
                    parr.add(pr);


                }

                Log.e("TrendingActivity",String.valueOf(parr.size()));


                Collections.sort(parr, new Comparator<Pair<Integer, String>>() {
                    @Override
                    public int compare(final Pair<Integer, String> o1, final Pair<Integer, String> o2) {

                        if(o1.first<o2.first){
                            return 1;
                        } else {
                            return -1;
                        }

                    }
                });
                int j=0;
                for(Pair<Integer,String> p:parr){

                    if(j>=3){
                        break;
                    }
                    cp[j].setText(p.second);

                    Log.e("Trending",String.valueOf(p.first));
                    ++j;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TrendingActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
