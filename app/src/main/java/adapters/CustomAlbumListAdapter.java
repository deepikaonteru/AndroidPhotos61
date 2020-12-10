package adapters;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.example.androidphotos61.*;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import objects.Album;

import static com.example.androidphotos61.MainActivity.*;

public class CustomAlbumListAdapter extends BaseAdapter{


    private ArrayList<String> list = new ArrayList<String>();
    private Context context;


    public CustomAlbumListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.album_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.tagItemString);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.deleteTagButton);
        Button editBtn = (Button)view.findViewById(R.id.edit_btn);
        Button viewBtn = (Button)view.findViewById(R.id.view_btn);


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position);
                deleteAlbum(position);
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                editAlbum(position);
                notifyDataSetChanged();
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                showAlbum(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }


    public void showAlbum(int position){
        Bundle bundle = new Bundle();
        try {
            File file = new File(context.getFilesDir(), "data.ser");
            FileInputStream fis = context.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            Album album = albums.get(position);
            bundle.putString("albumTitle", album.getAlbumName());
            bundle.putInt("position", position);
            Intent intent = new Intent(context.getApplicationContext(), PhotosList.class);
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, VIEW_PHOTOS_LIST);
        }catch (Exception e){}

    }

    public void deleteAlbum(int position){

        try {
            File file = new File(context.getFilesDir(), "data.ser");
            FileInputStream fis = context.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            albums.remove(position);
            fis.close();
            in.close();
            FileOutputStream fos = context.openFileOutput("data.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(albums);
            oos.close();
            fos.close();
        } catch (Exception e){

        }

    }

    public void editAlbum(int position){
        try {
            Bundle bundle = new Bundle();
            File file = new File(context.getFilesDir(), "data.ser");
            FileInputStream fis = context.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            bundle.putSerializable("albums_list", albums);
            bundle.putInt("position", position);
            Intent intent = new Intent(context.getApplicationContext(), AddAlbum.class);
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, EDIT_ALBUM_CODE);

        }catch(Exception e){

        }
    }

}
