package com.example.androidphotos61;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import DialogFragments.AddEditDialogFragment;
import objects.*;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class AddTag extends AppCompatActivity {

    ArrayList<Album> albums;
    int currentAlbumPosition;
    int currentPhotoPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add A Tag");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        albums = (ArrayList<Album>)bundle.getSerializable("albums_list");
        currentAlbumPosition = bundle.getInt("albumPosition");
        currentPhotoPosition = bundle.getInt("photoPosition");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), PhotoDisplay.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        return true;
    }

    public void addTag(View view){
        Bundle bundle = new Bundle();
        EditText tagTypeField = findViewById(R.id.tagTypeTextField);
        String tagType = tagTypeField.getText().toString().toLowerCase().trim();

        EditText tagValueField = findViewById(R.id.tagValueTextField);
        String tagValue = tagValueField.getText().toString().toLowerCase().trim();

        if(tagType.isEmpty()){
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Tag Type Cannot Be Empty");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        else if(tagValue.isEmpty()){
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Tag Value Cannot Be Empty");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }
        else if(!tagType.equals("location") && !tagType.equals("person")){
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Tag Type Must Be \"Location\" or \"Person\"");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }

        boolean tagExists = false;
        ArrayList<Tag> currentTags = albums.get(currentAlbumPosition).getPhotos().get(currentPhotoPosition).getTags();
        for(int i = 0; i < currentTags.size(); i++){
            if(currentTags.get(i).tagType.equals(tagType) && currentTags.get(i).tagValue.equals(tagValue)){
                tagExists = true;
                break;
            }
        }

        if(tagExists){
            bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                    "Tag Exists. Enter A Different Tag");
            DialogFragment newFragment = new AddEditDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "badfields");
            return;
        }else{
            albums.get(currentAlbumPosition).getPhotos().get(currentPhotoPosition).getTags().add(new Tag(tagType, tagValue));
            try{
                FileOutputStream fos = openFileOutput("data.ser", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(albums);
                fos.close();
                oos.close();
                bundle.putInt("position", currentPhotoPosition);
                bundle.putString("albumTitle", albums.get(currentPhotoPosition).getAlbumName());
                bundle.putInt("albumPosition", currentAlbumPosition);
                Intent intent = new Intent(this, PhotoDisplay.class);
                intent.putExtras(bundle);
                setResult(RESULT_OK);
                finish();
            }catch(Exception e){

            }
        }

    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

}
