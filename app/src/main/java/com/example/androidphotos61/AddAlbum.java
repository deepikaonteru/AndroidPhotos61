package com.example.androidphotos61;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import DialogFragments.*;
import android.view.View;
import objects.*;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.content.*;


public class AddAlbum extends AppCompatActivity {

    ArrayList<Album> albums;
    int position;
    private EditText albumTextField;
    public static final String ALBUM_INDEX = "movieIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        albums = new ArrayList<Album>();
        try{
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
        }catch(Exception e){}


        if(bundle.size() > 1){
            toolbar.setTitle("Edit Album");
            position = bundle.getInt("position");
            albumTextField = (EditText)findViewById(R.id.newAlbumTextField);
            albumTextField.setText(albums.get(position).getAlbumName());
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addAlbum(View view){
        EditText albumTextField = findViewById(R.id.newAlbumTextField);
        String newAlbumName = albumTextField.getText().toString();

        if(newAlbumName.trim().length() == 0 || newAlbumName == null){
            Bundle bundle = new Bundle();
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Album Name Cannot Be Empty");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;

        }
        boolean albumExists = false;
        for(int i = 0; i < albums.size(); i++){
            if(newAlbumName.toLowerCase().trim().equals(albums.get(i).getAlbumName().toLowerCase())){
                albumExists = true;
                break;
            }
        }

        if(albumExists){
            Bundle bundle = new Bundle();
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Album Name Already Exists. Create Different Album Name.");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }else{
            Bundle bundle = new Bundle();
            bundle.putSerializable("albums_list", albums);
            bundle.putString("albumName", newAlbumName);
            bundle.putInt("position", position);
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



}
