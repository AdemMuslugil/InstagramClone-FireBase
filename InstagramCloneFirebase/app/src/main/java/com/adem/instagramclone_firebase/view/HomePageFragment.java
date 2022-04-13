package com.adem.instagramclone_firebase.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.adem.instagramclone_firebase.adapter.PostAdapter;
import com.adem.instagramclone_firebase.databinding.FragmentHomePageBinding;
import com.adem.instagramclone_firebase.model.Posts;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomePageFragment extends Fragment {
private FragmentHomePageBinding binding;

FirebaseFirestore firebaseFirestore;
ArrayList<Posts> postsArrayList;
PostAdapter postAdapter;
ImageButton imageButton;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomePageBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();

        postsArrayList=new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postAdapter=new PostAdapter(postsArrayList);
        binding.recyclerView.setAdapter(postAdapter);

    }

    public void getData(){
        firebaseFirestore.collection("Instagram_Posts").orderBy("Date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Toast.makeText(requireContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if (value!=null){

                    postsArrayList.clear();

                    for (DocumentSnapshot document:value.getDocuments()){
                        Map<String,Object> data=document.getData();

                        String email=(String) data.get("Email");
                        String comment=(String) data.get("Comment");
                        String downloadUrl=(String) data.get("Download Url");


                        Posts posts=new Posts(email,comment,downloadUrl);
                        postsArrayList.add(posts);
                    }

                    postAdapter.notifyDataSetChanged();

                }
            }
        });
    }

}