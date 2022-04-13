package com.adem.instagramclone_firebase.view;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adem.instagramclone_firebase.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;

String email;
String password;
FirebaseAuth auth;
FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();

        user=auth.getCurrentUser();
        if (user!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void signUp(View view) {
        email = binding.emailEditText.getText().toString();
        password = binding.passwordEditText.getText().toString();

        if (!email.trim().isEmpty() && !password.trim().isEmpty()) {

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    user = auth.getCurrentUser();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this,e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    System.out.println("Failed");
                    Log.w(TAG, "fail: ",e );
                }
            });

        }else {
            Toast.makeText(LoginActivity.this, "Enter Email && Password", Toast.LENGTH_SHORT).show();
        }
    }


    public void signIn(View view) {
        email=binding.emailEditText.getText().toString();
        password=binding.passwordEditText.getText().toString();

        if (!email.trim().isEmpty() && !password.trim().isEmpty()) {

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(view.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    user = auth.getCurrentUser();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.w(TAG, "fail: ",e );
                }
            });
        }else{
            Toast.makeText(view.getContext(), "Enter Email & Password", Toast.LENGTH_SHORT).show();
        }

    }
}