package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.androidphotos61.R;

import java.util.ArrayList;

public class CustomSearchResultsAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Integer> photoIDs;


    @Override
    public int getCount() {
        return photoIDs.size();
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

        if (convertView == null) {  // if it's not recycled, initialize some attributes

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.search_result_item, null);
        }

        imageView = (ImageView) gridView.findViewById(R.id.searchImg);
        imageView.setImageResource(photoIDs.get(position));

        return gridView;
    }

    public CustomSearchResultsAdapter(Context c, ArrayList<Integer> photos){
        mContext = c;
        photoIDs = photos;
    }
}
