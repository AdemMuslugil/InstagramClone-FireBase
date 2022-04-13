package com.adem.instagramclone_firebase.view;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adem.instagramclone_firebase.databinding.FragmentUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;


public class UploadFragment extends Fragment {
private FragmentUploadBinding binding;

StorageReference storageReference;
FirebaseStorage firebaseStorage;
FirebaseFirestore firebaseFirestore;
FirebaseAuth auth;
FirebaseUser user;
Uri imageUri;

ActivityResultLauncher<String> permissionLauncher;
ActivityResultLauncher<Intent> activityResultLauncher;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerLauncher();

        auth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        user=auth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentUploadBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewClick(view);
            }
        });


        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload(view);
            }
        });
    }

    public void imageViewClick(View view){

        //check permission for gallery
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                Snackbar.make(view,"Permission needed for gallery", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();

            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }


        }else{
            //gallery
            Intent intentToGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    public void upload(View view){

        if (imageUri!=null) {
            UUID uuid = UUID.randomUUID();
            String imageName = "Instagram_images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference=firebaseStorage.getReference(imageName);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl=uri.toString();
                            String comment=binding.commentEditText.getText().toString();
                            String email=user.getEmail();

                            HashMap<String,Object> postHashMap=new HashMap<>();
                            postHashMap.put("Email",email);
                            postHashMap.put("Comment",comment);
                            postHashMap.put("Download Url",downloadUrl);
                            postHashMap.put("Date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Instagram_Posts").add(postHashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    NavDirections action=UploadFragmentDirections.actionUploadFragmentToHomePageFragment();
                                    Navigation.findNavController(view).navigate(action);
                                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "onFailure: ", e);
                }
            });
        }
    }


    public void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK){

                    Intent intentFromResult=result.getData();
                    if (intentFromResult!= null){
                        imageUri=intentFromResult.getData();
                        binding.imageView.setImageURI(imageUri);
                    }

                }
            }
        });

        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){

                    Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }
                else{
                    Toast.makeText(requireActivity(), "Permission Needed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}