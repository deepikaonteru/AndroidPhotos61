package com.example.androidphotos61;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.MenuItem;
import java.io.*;
import java.util.ArrayList;

import adapters.CustomAlbumListAdapter;
import objects.*;
import android.content.*;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> albumNames;
    private ArrayList<Album> albums;
    private String albumName;
    private ListView list;
    private int position;

    public static final int ADD_ALBUM_CODE=1;
    public static final int EDIT_ALBUM_CODE=2;
    public static final int VIEW_PHOTOS_LIST=3;
    public static final int SEARCH_CODE = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list);

        Button button = (Button) findViewById(R.id.action_add);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);
        list = (ListView) findViewById(R.id.albums_list);

        albums = new ArrayList<Album>();
        albumNames = new ArrayList<String>();
        loadAlbums();


        for(int i = 0; i < albums.size(); i++){
            albumNames.add(albums.get(i).getAlbumName());
        }


        CustomAlbumListAdapter adapter = new CustomAlbumListAdapter(albumNames, this);

        list.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                createAnAlbum();
                return true;
            case R.id.action_search:
                searchForPhotos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void createAnAlbum(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("albums_list",albums);
        Intent intent = new Intent(this, AddAlbum.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, ADD_ALBUM_CODE);
    }

    public void searchForPhotos(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("albums_list",albums);
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SEARCH_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if (resultCode != RESULT_OK) {
            return;
        }

        Bundle bundle = intent.getExtras();
        albums = (ArrayList<Album>) bundle.getSerializable("albums_list");
        albumName = bundle.getString("albumName");
        position = bundle.getInt("position");

        if(requestCode == EDIT_ALBUM_CODE){
            Album temp = albums.get(position);
            temp.albumName = albumName;
            albums.set(position, temp);
            albumNames.set(position,albumName);
            File file = new File(getFilesDir(), "data.ser");
            try{
                FileOutputStream fos = openFileOutput("data.ser", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(albums);
            }catch (Exception e){}
        }

        if(requestCode == ADD_ALBUM_CODE){
            albums.add(new Album(albumName));
            albumNames.add(albumName);
            File file = new File(getFilesDir(), "data.ser");
            try{
                FileOutputStream fos = openFileOutput("data.ser", MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(albums);
            }catch (Exception e){}
        }

        list.setAdapter(new CustomAlbumListAdapter(albumNames, this));


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
