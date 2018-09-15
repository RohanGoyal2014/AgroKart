package com.example.rohangoyal2014.agrokart;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.rohangoyal2014.agrokart.Utils.mAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    TextView loginBtn,newUserBtn;
    EditText phoneView,passwordView;

    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);

        final MainActivity mainActivity=(MainActivity)getActivity();

        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mainActivity.getSupportActionBar().setTitle(getString(R.string.login));

        loginBtn=view.findViewById(R.id.login_btn);
        phoneView=view.findViewById(R.id.phone);
        passwordView=view.findViewById(R.id.password);
        newUserBtn=view.findViewById(R.id.new_user);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phoneView.getText().toString().isEmpty() || passwordView.getText().toString().isEmpty()){

                    Toast.makeText(getContext(),"Fields can not be blank",Toast.LENGTH_SHORT).show();

                } else {

                    signIn(phoneView.getText().toString().trim(),passwordView.getText().toString().trim());

                }

            }
        });

        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainActivity.viewPager.setCurrentItem(1);

            }
        });

        return view;
    }

    public void signIn(final String phone, final String password){


        FirebaseDatabase.getInstance().getReference().child("users").child("farmers").orderByChild(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==1){
                    String email=dataSnapshot.child(phone).child("email").getValue().toString();
                    Log.e("LoginFragment",email);

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent((MainActivity)getActivity(),DashboardActivity.class));
                                getActivity().finish();
                            } else {
                                Log.e("LoginFragment",task.getException().toString());
                                Toast.makeText(getContext(),"लॉगिन में असफल रहा",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"लॉगिन में असफल रहा",Toast.LENGTH_SHORT).show();
            }
        });

    }



}
