package com.example.androidphotos61;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import adapters.CustomPhotoListAdapter;
import objects.Album;
import objects.Photo;
import android.widget.GridView;

public class PhotosList extends AppCompatActivity {

    public CustomPhotoListAdapter imgAdapter;
    public ArrayList<Album> albums;
    GridView gridView;

    public static final int ADD_PHOTO_CODE=1;
    public static final int VIEW_PHOTO_DISPLAY=2;

    static String albumName;
    static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        albumName = (String) bundle.getString("albumTitle");
        position = bundle.getInt("position");

        imgAdapter = new CustomPhotoListAdapter(this, position);
        loadAlbums();

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(imgAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(albumName);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addPhoto();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addPhoto(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, AddPhoto.class);
        bundle.putInt("position", position);
        bundle.putString("albumTitle", albumName);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == ADD_PHOTO_CODE){
            try {
                Bundle bundle = intent.getExtras();
                int photoID = bundle.getInt("photoID");
                FileInputStream fis = openFileInput("data.ser");
                ObjectInputStream in = new ObjectInputStream(fis);
                albums = (ArrayList<Album>) in.readObject();

                albums.get(position).getPhotos().add(new Photo(photoID));
                FileOutputStream fos = openFileOutput("data.ser", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(albums);
                fos.close();
                oos.close();
            } catch (Exception e){}
        }

        gridView.setAdapter(new CustomPhotoListAdapter(this, position));

    }

    public void loadAlbums(){
        try {
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
        } catch (Exception e){

        }

    }


}
