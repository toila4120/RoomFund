package com.example.roomfund.fragment;

import static com.example.roomfund.MainActivity2.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.roomfund.MainActivity2;
import com.example.roomfund.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class myProfile extends Fragment {
    private View mView;
    private ImageView imgAvatar;
    private EditText edtFullName, edtEmail;
    private Button btnUpdateProfile,btnUpdateEmail;
    private Uri mUri;
    private MainActivity2 mAainActivity;
    private ProgressDialog mProgressDialog;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private FirebaseDatabase database;
    private final StorageReference storageRef=storage.getReference();
    private Uri dowload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_my_profile, container, false);

        initUi();
        mAainActivity=(MainActivity2) getActivity();
        mProgressDialog = new ProgressDialog(getActivity());
        setUserInformation();
        initlistener();

        return mView;
    }

    private void initUi(){
        storage=FirebaseStorage.getInstance();
        imgAvatar=mView.findViewById(R.id.img_avatar);
        edtFullName=mView.findViewById(R.id.edt_full_name);
        edtEmail=mView.findViewById(R.id.edt_email);
        btnUpdateProfile=mView.findViewById(R.id.btn_update_profile);
        btnUpdateEmail=mView.findViewById(R.id.btn_update_email);
    }

    private void setUserInformation() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            return;
        }
        edtFullName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.avatardefault).into(imgAvatar);
    }

    private void initlistener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestpermisson();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onClickUpdateProfile();
                upDateAvatar();
//                getAvatar();
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickRequestpermisson() {
        if (mAainActivity==null) {
            return;
        }
        mAainActivity.openGallery();
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            mAainActivity.openGallery();
        } else {
            String[] permisstion = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permisstion, MY_REQUEST_CODE);
        }
    }

    //
    public void setBitmapImgView(Bitmap bitmapImgView) {
        imgAvatar.setImageBitmap(bitmapImgView);
    }

    //
    public void setUri(Uri uri) {
        this.mUri = uri;
    }


    //update profile
    private void onClickUpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) {
            return;
        }
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        String strFullName = edtFullName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(dowload)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Update profile success", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }

    //Update Avatar
    private void upDateAvatar(){
        imgAvatar.setDrawingCacheEnabled(true);
        imgAvatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://roomfund-b14d5.appspot.com");
        StorageReference storageRef=storage.getReference("imgAvatar/");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mountainsRef=storageRef.child(user.getUid()+".png");
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                dowload=mUri;
                onClickUpdateProfile();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                getAvatar();
            }
        });
    }

    private void getAvatar(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://roomfund-b14d5.appspot.com");
        StorageReference storageRef=storage.getReference("imgAvatar/");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        StorageReference mountainsRef=storageRef.child(user.getUid()+".png");
        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                dowload=uri;
                onClickUpdateProfile();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                dowload=mUri;
                onClickUpdateProfile();
            }
        });
    }


    //Update email
    private void onClickUpdateEmail() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String strEmail = edtEmail.getText().toString().trim();
        if (user==null) {
            return;
        }
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        user.updateEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Update email success", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }
}
