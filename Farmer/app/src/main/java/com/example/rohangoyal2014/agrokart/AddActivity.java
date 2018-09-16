package com.example.rohangoyal2014.agrokart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.rohangoyal2014.agrokart.Utils.byIdName;
import static com.example.rohangoyal2014.agrokart.Utils.mAuth;
import static com.example.rohangoyal2014.agrokart.Utils.phoneNumber;

public class AddActivity extends AppCompatActivity {

    EditText qtyView,costView;
    Spinner itemSpinner,unitSpinner;

    ArrayList<String> itemNames=new ArrayList<String>();
    ArrayList<String> unitNames=new ArrayList<>();

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        itemNames.add("apple");
        itemNames.add("banana");
        itemNames.add("orange");
        itemNames.add("wheat");
        itemNames.add("rice");
        itemNames.add("dal");
        itemNames.add("potato");
        itemNames.add("onion");
        itemNames.add("cucumber");

        unitNames.add("dozen");
        unitNames.add("kg");
        unitNames.add("quintal");

        getSupportActionBar().setTitle("वस्तु डालें");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qtyView=findViewById(R.id.qty);
        costView=findViewById(R.id.cost);
        itemSpinner=findViewById(R.id.item);
        unitSpinner=findViewById(R.id.units);
        saveBtn=findViewById(R.id.save);

        ArrayList<String> unitList,itemList;

        unitList=new ArrayList<>();
        itemList=new ArrayList<>();

        unitList.add(getString(R.string.quintal));
        unitList.add(getString(R.string.kg));
        unitList.add(getString(R.string.dozen));

        itemList.add(getString(R.string.apple));
        itemList.add(getString(R.string.banana));
        itemList.add(getString(R.string.orange));
        itemList.add(getString(R.string.wheat));
        itemList.add(getString(R.string.rice));
        itemList.add(getString(R.string.dal));
        itemList.add(getString(R.string.potato));
        itemList.add(getString(R.string.cucumber));
        itemList.add(getString(R.string.onion));

        final ArrayAdapter<String> unitAdapter,itemAdapter;
        unitAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,unitList);
        itemAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,itemList);
        unitSpinner.setAdapter(unitAdapter);
        itemSpinner.setAdapter(itemAdapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.e("AddActivity",Utils.byIdName(AddActivity.this,"apple"));

                if(qtyView.getText().toString().trim().isEmpty() || costView.getText().toString().trim().isEmpty()){
                    Toast.makeText(AddActivity.this, "असफल रहा", Toast.LENGTH_SHORT).show();
                    return;
                }

                String item="",unit="";

                for(String s:itemNames){
                    if(byIdName(AddActivity.this,s).equals(itemSpinner.getSelectedItem().toString())){

                        item=s;
                        break;

                    }
                }

                for(String s:unitNames){

                    if(byIdName(AddActivity.this,s).equals(unitSpinner.getSelectedItem().toString())){

                        unit=s;
                        break;

                    }

                }

                final ItemModel itemModel=new ItemModel(item,mAuth.getCurrentUser().getEmail(),qtyView.getText().toString().trim(),costView.getText().toString().trim(),unit);

                boolean isNetworkavailable=Utils.isNetworkAvailable(AddActivity.this);

                if(isNetworkavailable){

                    FirebaseDatabase.getInstance().getReference().child("users").child("farmers").orderByChild("email").equalTo(itemModel.getFarmerEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String phone=dataSnapshot.getValue().toString().substring(1,11);

                            itemModel.setFarmerEmail(phone);

                            FirebaseDatabase.getInstance().getReference().child("products").child(itemModel.getName()).push().setValue(itemModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddActivity.this, "उत्पाद डाला गया है", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {

                    Set<String> itemSet=getSharedPreferences("textData",MODE_PRIVATE).getStringSet("items",new HashSet<String>());
                    itemSet.add(itemModel.getName());
                    SharedPreferences sharedPreferences=getSharedPreferences("tempData",MODE_PRIVATE);
                    SharedPreferences.Editor sharedPreferencesEditor=sharedPreferences.edit();
                    sharedPreferencesEditor.putStringSet("items",itemSet);
                    sharedPreferencesEditor.putString(itemModel.getName().concat("-cost"),itemModel.getCost());
                    sharedPreferencesEditor.putString(itemModel.getName().concat("-qty"),itemModel.getQty());
                    sharedPreferencesEditor.putString(itemModel.getName().concat("-unit"),itemModel.getUnit());
                    sharedPreferencesEditor.putString(itemModel.getName().concat("-seller"),itemModel.farmerEmail);
                    sharedPreferencesEditor.apply();
                    Toast.makeText(AddActivity.this, "उत्पाद ड्राफ्ट में सहेजा गया है", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
