package com.example.androidphotos61;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import DialogFragments.AddEditDialogFragment;
import objects.Album;

import static com.example.androidphotos61.MainActivity.*;


public class SearchActivity extends AppCompatActivity {

    ArrayList<Album> albums;
    ArrayList<Album> tempAlbums;

    EditText tagTypeField;
    EditText tagValueField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        Button b = (Button) findViewById(R.id.findResultsButton);
        b.setText("Search");
        setSupportActionBar(toolbar);
        albums = new ArrayList<Album>();
        try{
            File file = new File(getFilesDir(), "data.ser");
            FileInputStream fis = openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) in.readObject();
        } catch(Exception e){}

        tagTypeField = (EditText)findViewById(R.id.searchTagTypeTextField);
        tagValueField = (EditText)findViewById(R.id.searchTagValueTextField);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void search(View view){

        Bundle bundle = new Bundle();
        int code;
        ArrayList<Integer> tempSearchResults1 = new ArrayList<Integer>();
        ArrayList<Integer> tempSearchResults2 = new ArrayList<Integer>();
        ArrayList<Integer> searchResults = new ArrayList<Integer>();

        String tagType = tagTypeField.getText().toString().trim().toLowerCase();
        String tagValue = tagValueField.getText().toString().trim().toLowerCase();
        String tag1 ="";
        String tag2 = "";
        String value1="";
        String value2= "";
        //person and person, person and location, person or location etc
        if(tagType.contains(" and ") || tagType.contains(" or ")){
            String [] splitTags = tagType.split("\\s+");
             tag1 = splitTags[0];
             tag2 = splitTags[2];

            if(tagValue.contains(" and ") || tagValue.contains(" or ")){
            String [] splitValues = tagValue.split("\\s+");
             value1 = splitValues[0];
             value2 = splitValues[2];
            }
            else{
                Toast.makeText(
                        this, "Include and/or in your values as well", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(tagType.equals("location") && !tagValue.isEmpty()){
            for(int i = 0; i < albums.size(); i++){
                for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                    for(int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++){
                        if(albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals("location")
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(tagValue)){
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                            break;
                        }
                    }
                }
            }
        }else if(tagType.equals("person") && !tagValue.isEmpty()){
            for(int i = 0; i < albums.size(); i++){
                for(int j = 0; j < albums.get(i).getPhotos().size(); j++){
                    for(int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++){
                        if(albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals("person")
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(tagValue)){
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                            break;
                        }
                    }
                }
            }
        }else if(tagType.equals("person or person") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
        }else if(tagType.equals("person or location") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
        } else if(tagType.equals("location or location") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
        } else if(tagType.equals("location or person") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            searchResults.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
        } else if(tagType.equals("person and person") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            tempSearchResults1.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            tempSearchResults2.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }

            for (int i = 0; i < tempSearchResults1.size(); i++){
                    if (tempSearchResults2.contains(tempSearchResults1.get(i))){
                        searchResults.add(tempSearchResults1.get(i));
                    }
            }

        } else if(tagType.equals("person and location") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            tempSearchResults1.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            tempSearchResults2.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }

            for (int i = 0; i < tempSearchResults1.size(); i++){
                if (tempSearchResults2.contains(tempSearchResults1.get(i))){
                    searchResults.add(tempSearchResults1.get(i));
                }
            }
        } else if(tagType.equals("location and location") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            tempSearchResults1.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            tempSearchResults2.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }

            for (int i = 0; i < tempSearchResults1.size(); i++){
                if (tempSearchResults2.contains(tempSearchResults1.get(i))){
                    searchResults.add(tempSearchResults1.get(i));
                }
            }
        } else if(tagType.equals("location and person") && !tagValue.isEmpty()) {
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag1)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value1)) {
                            tempSearchResults1.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }
            for (int i = 0; i < albums.size(); i++) {
                for (int j = 0; j < albums.get(i).getPhotos().size(); j++) {
                    for (int k = 0; k < albums.get(i).getPhotos().get(j).getTags().size(); k++) {
                        if (albums.get(i).getPhotos().get(j).getTags().get(k).tagType.equals(tag2)
                                && albums.get(i).getPhotos().get(j).getTags().get(k).tagValue.contains(value2)) {
                            tempSearchResults2.add(albums.get(i).getPhotos().get(j).getPhotoID());
                        }
                    }
                }
            }

            for (int i = 0; i < tempSearchResults1.size(); i++){
                if (tempSearchResults2.contains(tempSearchResults1.get(i))){
                    searchResults.add(tempSearchResults1.get(i));
                }
            }
        }

        else{
            if(tagType.isEmpty() || tagValue.isEmpty()){
                bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                        "Fields Cannot Be Empty");
                DialogFragment newFragment = new AddEditDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }else if(!tagType.equals("person") && !tagType.equals("location")
                    && !tagType.equals("person and person") && !tagType.equals("location and person")
                    && !tagType.equals("person and location") && !tagType.equals("location and location")
                    && !tagType.equals("person or person") && !tagType.equals("location or person")
                    && !tagType.equals("person or location") && !tagType.equals("location or location")
            ){
                bundle.putString(AddEditDialogFragment.MESSAGE_KEY,
                        "Check your tag types.");
                DialogFragment newFragment = new AddEditDialogFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "badfields");
                return;
            }

        }

        bundle.putIntegerArrayList("photos", searchResults);
        bundle.putString("filter", tagType);
        Intent intent = new Intent(getApplicationContext(), SearchResults.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SEARCH_CODE);
    }


}
