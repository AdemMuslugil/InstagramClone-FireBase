package com.adem.instagramclone_firebase.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.adem.instagramclone_firebase.R;
import com.adem.instagramclone_firebase.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;

FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();

    }

    @Override//menu inflate
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.insta_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override//menu item select
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.upload_menu:
                NavDirections action= HomePageFragmentDirections.actionHomePageFragmentToUploadFragment();
                Navigation.findNavController(MainActivity.this,R.id.fragmentContainerView).navigate(action);
                break;

            case R.id.signOut_menu:
                auth.signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}