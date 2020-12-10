package com.example.androidphotos61;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import DialogFragments.AddEditDialogFragment;
import objects.*;

public class MoveToAlbum extends AppCompatActivity {

    ArrayList<Album> albums;
    int photoPosition;
    int currentAlbumPosition;
    int newAlbumPosition;
    Photo currentPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Move To Album");
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        albums = new ArrayList<Album>();
        albums = (ArrayList<Album>) bundle.getSerializable("albums_list");
        photoPosition = bundle.getInt("photoPosition");
        currentAlbumPosition = bundle.getInt("albumPosition");
        currentPhoto = albums.get(currentAlbumPosition).getPhotos().get(photoPosition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void moveToAlbum(View view){
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
        boolean photoExistsInNewAlbum = false;
        for(int i = 0; i < albums.size(); i++){
            if(newAlbumName.toLowerCase().trim().equals(albums.get(i).getAlbumName().toLowerCase())){
                newAlbumPosition = i;
                albumExists = true;
                for(int j = 0; j < albums.get(newAlbumPosition).getPhotos().size(); j++) {
                    if(currentPhoto.getPhotoID() == albums.get(newAlbumPosition).getPhotos().get(j).getPhotoID()) {
                        photoExistsInNewAlbum = true;
                        break;
                    }
                }
            }
        }

        if(albumExists && !photoExistsInNewAlbum) {

            Photo photo;
            photo = albums.get(currentAlbumPosition).getPhotos().get(photoPosition);
            albums.get(currentAlbumPosition).getPhotos().remove(photoPosition);
            albums.get(newAlbumPosition).getPhotos().add(photo);
            try{
                FileOutputStream fos = openFileOutput("data.ser", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(albums);
                fos.close();
                oos.close();
                Bundle bundle = new Bundle();
                bundle.putInt("position", newAlbumPosition);
                bundle.putString("albumTitle", albums.get(newAlbumPosition).getAlbumName());
                Intent intent = new Intent(this, PhotosList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch(Exception e){

            }


        }else if (albumExists && photoExistsInNewAlbum){
            Bundle bundle = new Bundle();
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Photo Already Exists In Destined Album");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }else if(!albumExists){
            Bundle bundle = new Bundle();
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Album Does Not Exist");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }



    }

    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), PhotoDisplay.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        return true;
    }



}
