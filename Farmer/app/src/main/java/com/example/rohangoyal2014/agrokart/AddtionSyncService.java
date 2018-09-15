package com.example.rohangoyal2014.agrokart;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public class AddtionSyncService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        onTaskRemoved(intent);

//        MediaPlayer mp=MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
//        mp.setLooping(true);
//        mp.start();

//        Toast.makeText(this, "Hey I am Running Man!", Toast.LENGTH_SHORT).show();

        if(Utils.isNetworkAvailable(this)) {



            final SharedPreferences sharedPreferences = getSharedPreferences("tempData", MODE_PRIVATE);
            Set<String> st = sharedPreferences.getStringSet("items", new HashSet<String>());
            final Set<String> unsynced=new HashSet<>();

            final SharedPreferences.Editor tempDataEditor=sharedPreferences.edit();

            if (st.isEmpty()) {


                //No Need to run service if there are no unsaved drafts

                stopService(intent);
                SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("serviceState", MODE_PRIVATE).edit();
                sharedPreferencesEditor.putBoolean("state", false);
                sharedPreferencesEditor.apply();
            } else {

                for(final String s:st){

                    ItemModel im=new ItemModel(s,
                            sharedPreferences.getString(s+"-seller",""),
                            sharedPreferences.getString(s+"-qty",""),
                            sharedPreferences.getString(s+"-cost",""),
                            sharedPreferences.getString(s+"-unit","")
                    );

                    FirebaseDatabase.getInstance().getReference().child("products").child(im.getName()).push().setValue(im).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(AddtionSyncService.this, "Done", Toast.LENGTH_LONG).show();

                                tempDataEditor.remove(s+"-qty");
                                tempDataEditor.remove(s+"-seller");
                                tempDataEditor.remove(s+"-cost");
                                tempDataEditor.remove(s+"-unit");
                                tempDataEditor.apply();
                            } else {
                                unsynced.add(s);
                            }
                        }
                    });

                }

                tempDataEditor.putStringSet("items",unsynced);
                tempDataEditor.apply();


            }
        }

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent=new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);

        super.onTaskRemoved(rootIntent);
    }
}
