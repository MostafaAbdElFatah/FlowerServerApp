package com.mostafa.fci.flowerserverapp.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mostafa.fci.flowerserverapp.Classes.Flower;
import com.mostafa.fci.flowerserverapp.R;
import com.mostafa.fci.flowerserverapp.Services.DBServices;
import com.mostafa.fci.flowerserverapp.Utillities.Dialog;
import com.mostafa.fci.flowerserverapp.Utillities.Network;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddFlowerActivity extends AppCompatActivity {

    ImageButton galleryBtn;
    int RESULT_LOAD_IMG = 911;
    Uri filePathUri = null;
    ProgressDialog progressDialog ;
    EditText flowerNameEditText , flowerCategoryEditText
            , flowerInstructionEditText , flowerPriceEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flower);
        flowerNameEditText        = findViewById(R.id.flowerName);
        flowerCategoryEditText    = findViewById(R.id.flowerCategory);
        flowerPriceEditText       = findViewById(R.id.flowerPrice);
        flowerInstructionEditText = findViewById(R.id.flowerInstructions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Button addBtn = findViewById(R.id.addBtn);
        galleryBtn = findViewById(R.id.galleryBtn);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(AddFlowerActivity.this);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        addBtn.setTypeface(typeface);

    }

    public void getGalleryPhotos(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public void addFlowerToFirebase(View view) {
        if (filePathUri == null){
            Toast.makeText(AddFlowerActivity.this, "Select Flower Photo"
                    , Toast.LENGTH_LONG).show();
            return;
        }
        if(Network.isOnLine(AddFlowerActivity.this)) {
            progressDialog.setTitle("Uploading Data...");
            progressDialog.show();
            uploadImage();
            progressDialog.dismiss();
        }else{
            Dialog.show(AddFlowerActivity.this);
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK && reqCode == RESULT_LOAD_IMG) {
            try {
                filePathUri = data.getData();
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),selectedImage);
                galleryBtn.setBackgroundDrawable(bdrawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddFlowerActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void uploadImage(){

        String flowerName = flowerNameEditText.getText().toString();
        flowerName = flowerName.replaceAll("[()?:!.,;{}]+-/\\ %$#~^&*?\"\' ", "_");
        flowerName += "." + getFileExtension(filePathUri);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child( "flowers/"+ flowerName)
                .putFile(filePathUri);
        Flower flower = new Flower();
        flower.setPhoto(flowerName);
        flower.setName(flowerNameEditText.getText().toString());
        flower.setCategory(flowerCategoryEditText.getText().toString());
        flower.setInstructions(flowerInstructionEditText.getText().toString());
        flower.setPrice(Double.parseDouble(flowerPriceEditText.getText().toString()));
        DBServices dbServices = new DBServices();
        dbServices.pushFlower(flower);
        flowerNameEditText.setText("");
        flowerCategoryEditText.setText("");
        flowerPriceEditText.setText("");
        flowerInstructionEditText.setText("");
        galleryBtn.setBackgroundResource(R.drawable.ic_add_to_photos_white_24dp);
        Toast.makeText(AddFlowerActivity.this ,"The Flower Addded"
                ,Toast.LENGTH_SHORT).show();

    }

    public String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

}
