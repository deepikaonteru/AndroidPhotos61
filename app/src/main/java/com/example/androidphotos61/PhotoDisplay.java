package com.example.androidphotos61;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import adapters.CustomTagListAdapter;
import objects.*;
import android.view.*;
import android.content.*;
import android.app.Activity;

public class PhotoDisplay extends AppCompatActivity {

    int photoPosition;
    int albumPosition;
    ArrayList<Album> albums;
    ArrayList<String> tags;
    String albumName;
    ListView listView;
    public static final int MOVE_TO_ALBUM=1;
    public static final int ADD_TAG=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Photo Display");
        setSupportActionBar(toolbar);

        Button leftButton = (Button)findViewById(R.id.leftButton);
        Button rightButton = (Button)findViewById(R.id.rightButton);
        Button addTag = (Button)findViewById(R.id.addTag);
        Button moveToAlbum = (Button)findViewById(R.id.moveToAlbum);
        listView = (ListView)findViewById(R.id.tagsList);

        ImageView img = (ImageView) findViewById(R.id.imageView);
        Bundle bundle = getIntent().getExtras();
        photoPosition = bundle.getInt("position");
        albumPosition = bundle.getInt("albumPosition");
        albumName = bundle.getString("albumTitle");



        try{
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
        }catch(Exception e){}


        tags = new ArrayList<String>();
        for(int i = 0; i < albums.get(albumPosition).getPhotos().get(photoPosition).getTags().size(); i++){
            tags.add(albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagType + ": " +
                    albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagValue);
        }
        CustomTagListAdapter adapter = new CustomTagListAdapter(tags, albumPosition, photoPosition, this);

        listView.setAdapter(adapter);

        img.setImageResource(albums.get(albumPosition).getPhotos().get(photoPosition).getPhotoID());
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(photoPosition != 0) {
                    photoPosition--;
                    goToLeftPhoto();
                    tags = new ArrayList<String>();
                    for(int i = 0; i < albums.get(albumPosition).getPhotos().get(photoPosition).getTags().size(); i++){
                        tags.add(albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagType + ": " +
                                albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagValue);
                    }
                    CustomTagListAdapter adapter = new CustomTagListAdapter(tags, albumPosition, photoPosition, getApplicationContext());
                    listView.setAdapter(adapter);
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoPosition + 1 < albums.get(albumPosition).getPhotos().size()){
                    photoPosition++;
                    goToRightPhoto();
                    tags = new ArrayList<String>();
                    for(int i = 0; i < albums.get(albumPosition).getPhotos().get(photoPosition).getTags().size(); i++){
                        tags.add(albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagType + ": " +
                                albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagValue);
                    }
                    CustomTagListAdapter adapter = new CustomTagListAdapter(tags, albumPosition, photoPosition, getApplicationContext());
                    listView.setAdapter(adapter);
                }
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTag(albums.get(albumPosition).getPhotos().get(photoPosition).getTags());
            }
        });

        moveToAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAlbum(albums.get(albumPosition).getPhotos().get(photoPosition).getPhotoID());

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void goToRightPhoto(){
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(albums.get(albumPosition).getPhotos().get(photoPosition).getPhotoID());

    }

    public void goToLeftPhoto(){
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(albums.get(albumPosition).getPhotos().get(photoPosition).getPhotoID());
    }

    public void addTag(ArrayList<Tag> tags){
        try {
            Bundle bundle = new Bundle();
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            bundle.putSerializable("albums_list", albums);
            bundle.putInt("albumPosition", albumPosition);
            bundle.putInt("photoPosition", photoPosition);
            Intent intent = new Intent(getApplicationContext(), AddTag.class);
            intent.putExtras(bundle);
            ((Activity) this).startActivityForResult(intent, ADD_TAG);

        }catch(Exception e){

        }
    }

    public void moveToAlbum(int photoID){
        try {
            Bundle bundle = new Bundle();
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            bundle.putSerializable("albums_list", albums);
            bundle.putInt("photoPosition", photoPosition);
            bundle.putInt("albumPosition", albumPosition);
            Intent intent = new Intent(getApplicationContext(), MoveToAlbum.class);
            intent.putExtras(bundle);
            ((Activity) this).startActivityForResult(intent, MOVE_TO_ALBUM);

        }catch(Exception e){

        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle = new Bundle();
        bundle.putInt("position", albumPosition);
        bundle.putString("albumTitle", albumName);
        Intent intent = new Intent(getApplicationContext(), PhotosList.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = getIntent().getExtras();
        int currentPhotoPosition = bundle.getInt("position");
        int currentAlbumPosition = bundle.getInt("albumPosition");
        try{
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
        }catch(Exception e){}


        tags = new ArrayList<String>();
        for(int i = 0; i < albums.get(albumPosition).getPhotos().get(photoPosition).getTags().size(); i++){
            tags.add(albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagType + ": " +
                    albums.get(albumPosition).getPhotos().get(photoPosition).getTags().get(i).tagValue);
        }
        listView.setAdapter(new CustomTagListAdapter(tags, albumPosition, photoPosition, this));


    }

}
