package adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Context;

import com.example.androidphotos61.PhotoDisplay;
import com.example.androidphotos61.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import objects.Album;
import objects.Photo;
import android.os.*;

import static android.content.Context.MODE_PRIVATE;
import static com.example.androidphotos61.PhotosList.*;

public class CustomPhotoListAdapter extends BaseAdapter {

    private Context mContext;
    private int albumPosition;
    private ArrayList<Photo> photos;

    @Override
    public int getCount() {
        try{
            File file = new File(mContext.getFilesDir(), "data.ser");
            FileInputStream fis = mContext.openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            photos = albums.get(albumPosition).photos;
            return photos.size();
        }catch(Exception e){

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        ImageView imageView;
        ImageButton imageButton;

        if (convertView == null) {  // if it's not recycled, initialize some attributes

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.photos_list_item, null);
        }

        imageView = (ImageView) gridView.findViewById(R.id.img);
        imageButton = (ImageButton) gridView.findViewById(R.id.removeImageButton);


        imageView.setImageResource(photos.get(position).getPhotoID());
        imageButton.setImageResource(R.drawable.ic_remove_circle_black_24dp);
        imageButton.setBackground(null);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                displayPhoto(position);
                notifyDataSetChanged();
            }
        });

        Bundle bundle = new Bundle();
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deletePhoto(position);
                notifyDataSetChanged();
            }
        });

        return gridView;
    }

    public void deletePhoto(int position){

        try {
            File file = new File(mContext.getFilesDir(), "data.ser");
            FileInputStream fis = mContext.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            albums.get(albumPosition).getPhotos().remove(position);
            fis.close();
            in.close();
            FileOutputStream fos = mContext.openFileOutput("data.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(albums);
            oos.close();
            fos.close();
        } catch (Exception e){

        }

    }

    public void displayPhoto(int position){
        Bundle bundle = new Bundle();
        try {
            File file = new File(mContext.getFilesDir(), "data.ser");
            FileInputStream fis = mContext.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            Album album = albums.get(albumPosition);
            bundle.putString("albumTitle", album.getAlbumName());
            bundle.putInt("position", position);
            bundle.putInt("albumPosition", albumPosition);
            Intent intent = new Intent(mContext.getApplicationContext(), PhotoDisplay.class);
            intent.putExtras(bundle);
            ((Activity) mContext).startActivityForResult(intent, VIEW_PHOTO_DISPLAY);
        }catch (Exception e){}
    }
    public CustomPhotoListAdapter(Context c, int p){
        mContext = c;
        albumPosition = p;
    }
}
