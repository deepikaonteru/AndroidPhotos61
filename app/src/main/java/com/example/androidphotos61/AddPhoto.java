package com.example.androidphotos61;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import android.widget.*;

import DialogFragments.AddEditDialogFragment;
import objects.*;

public class AddPhoto extends AppCompatActivity {

    String albumName;
    private EditText photoNameField;
    ArrayList<Photo> photos;
    ArrayList<Album> albums;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Photo");
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString("albumTitle");
        albums = new ArrayList<Album>();
        photos = new ArrayList<Photo>();

        try{
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
            position = bundle.getInt("position");
            photos = albums.get(position).photos;
        }catch(Exception e){}


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addPhoto(View view){
        Bundle bundle = new Bundle();
        boolean photoExists = false;

        photoNameField = findViewById(R.id.tagValueTextField);
        if(photoNameField.getText().toString().trim().isEmpty()) {
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Photo Field Is Empty");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        int id = AddPhoto.this.getResources().getIdentifier(photoNameField.getText().toString().trim(), "drawable", AddPhoto.this.getPackageName());
        for(int i = 0; i < photos.size(); i++){
            if(photos.get(i).getPhotoID() == id){
                photoExists = true;
                break;
            }
        }
        if(id == 0) {
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Photo Does Not Exist. Add A Different Photo.");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }else if(photoExists){
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Photo Already Exists. Add A Different Photo.");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }else{
            bundle.putInt("photoID", AddPhoto.this.getResources().getIdentifier(photoNameField.getText().toString().trim(), "drawable", AddPhoto.this.getPackageName()));
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), PhotosList.class);
        bundle.putInt("position", position);
        bundle.putString("albumTitle", albumName);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        return true;
    }
}
