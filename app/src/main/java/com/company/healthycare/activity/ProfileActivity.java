package com.company.healthycare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.company.healthycare.R;
import com.company.healthycare.model.UsersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity
implements View.OnClickListener {
    private static final int CHOOSE_IMAGE = 101;
    LinearLayout changePictureProfile,deletePictureProfile;
    BottomSheetDialog bottomSheetDialog;
    Uri uriProfileImage;
    ImageView profileImageView;
    ProgressBar progressBar;
    String profileImageUrl;
    Button btnSave,btnCancel;
    EditText txtFullName,txtAge,txtEmail;
    Spinner spinGender;
    FirebaseAuth mAuth;
    TextView errorView,errorViewVerified;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        txtAge = (EditText) findViewById(R.id.txt_age);
        txtFullName = (EditText) findViewById(R.id.txt_full_name);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        spinGender = (Spinner) findViewById(R.id.spin_gender);
        errorView = (TextView) findViewById(R.id.error_view);
        errorViewVerified = (TextView) findViewById(R.id.error_view_verification);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        fab.setOnClickListener(this);

        createBottomSheetDialog();
    }

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            if(user.isEmailVerified()){
                errorViewVerified.setVisibility(View.GONE);
            }else{
                errorViewVerified.setVisibility(View.VISIBLE);
                errorViewVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                        progressDialog.setMessage("Loading..");
                        progressDialog.show();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Toast.makeText(getBaseContext(),"Email verification sent.",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
            if(user.getDisplayName().isEmpty()){
                errorView.setVisibility(View.VISIBLE);
            }else{
                errorView.setVisibility(View.GONE);
            }
            if(user.getPhotoUrl() != null){
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImageView);
            }
            if(user.getDisplayName() != null){
                setTitle(user.getDisplayName());
                txtFullName.setText(user.getDisplayName());
            }
            if(user.getEmail() != null){
                txtEmail.setText(user.getEmail());
            }
            mDatabase.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null){
                        UsersModel users = dataSnapshot.getValue(UsersModel.class);
                        txtAge.setText((users.getAge() == null) ? "" : users.getAge());
                        if(users.getGender() != null && users.getGender().equals("Male")){
                            spinGender.setSelection(0);
                        }else{
                            spinGender.setSelection(1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            Intent it = new Intent(this,LoginActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }

        loadUserInformation();
    }

    private void createBottomSheetDialog(){
        if(bottomSheetDialog == null){
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet,null);
            changePictureProfile = view.findViewById(R.id.change_picture_profile);
            deletePictureProfile = view.findViewById(R.id.delete_picture_profile);
            setTitle("Nama Lengkap");

            changePictureProfile.setOnClickListener(this);
            deletePictureProfile.setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }
    private void showImageChooser(){
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it,"Pilih Foto Profile"),CHOOSE_IMAGE);
    }
    private void uploadImageToFirebaseStorage(){
        final StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    profileImageUrl = downloadUri.toString();
                                    Glide.with(ProfileActivity.this)
                                            .load(profileImageUrl)
                                            .into(profileImageView);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage= data.getData();
            uploadImageToFirebaseStorage();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab:
                bottomSheetDialog.show();
                break;
            case R.id.change_picture_profile:
                showImageChooser();
                bottomSheetDialog.dismiss();
                break;
            case R.id.delete_picture_profile:
                bottomSheetDialog.dismiss();
                break;
            case R.id.btn_save:
                saveUserInformation();
                break;
            case R.id.btn_cancel:
                finish();
                Intent it = new Intent(this,MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                break;
        }
    }

    private void saveUserInformation() {
        String displayFullName = txtFullName.getText().toString();
        String age = txtAge.getText().toString();
        String gender = spinGender.getSelectedItem().toString();

        if(displayFullName.isEmpty()){
            txtFullName.setError("Full name is required.");
            txtFullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            txtAge.setError("Age is required.");
            txtAge.requestFocus();
            return;
        }
        if(Integer.parseInt(age) <= 0){
            txtAge.setError("Age must more than 0.");
            txtAge.requestFocus();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            if(profileImageUrl == null && user.getPhotoUrl() == null){
                Toast.makeText(getBaseContext(),"Please change your profile pircture first.",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
            if(user.getPhotoUrl() != null){
                profileImageUrl = user.getPhotoUrl().toString();
            }
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayFullName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profileChangeRequest);
            UsersModel users = new UsersModel();
            users.setAge(age);
            users.setEmail(user.getEmail());
            users.setFullName(displayFullName);
            users.setGender(gender);
            mRef.child(user.getUid())
                    .setValue(users)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(getBaseContext(),"Profile updated",Toast.LENGTH_LONG).show();
                                finish();
                                Intent it = new Intent(ProfileActivity.this,MainActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(it);
                            }
                        }
                    });
        }
    }
}
