package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidphotos61.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import objects.Album;

import static android.content.Context.MODE_PRIVATE;

public class CustomTagListAdapter extends BaseAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private int albumPosition;
    private int photoPosition;

    public CustomTagListAdapter(ArrayList<String> list, int albumPosition, int photoPosition, Context context) {
        this.list = list;
        this.context = context;
        this.albumPosition = albumPosition;
        this.photoPosition = photoPosition;
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
            view = inflater.inflate(R.layout.tag_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.tagItemString);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.deleteTagButton);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position);
                deleteTag(position);
                notifyDataSetChanged();
            }
        });


        return view;
    }

    public void deleteTag(int position){

        try {
            File file = new File(context.getFilesDir(), "data.ser");
            FileInputStream fis = context.getApplicationContext().openFileInput("data.ser");
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Album> albums = (ArrayList<Album>) in.readObject();
            albums.get(albumPosition).getPhotos().get(photoPosition).getTags().remove(position);
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

}
