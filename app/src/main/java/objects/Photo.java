package objects;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Photo implements Serializable {
    int photoID;
    ArrayList<Tag> tags;

    public Photo(int photoID){
        this.photoID = photoID;
        this.tags = new ArrayList<Tag>();
    }

    public int getPhotoID(){
        return this.photoID;
    }

    public ArrayList<Tag> getTags(){
        return this.tags;
    }
}
