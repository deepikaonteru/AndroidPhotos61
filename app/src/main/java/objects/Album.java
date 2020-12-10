package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    public String albumName;
    public int photoAmount;
    public ArrayList<Photo> photos;

    public Album(String albumName){
        this.albumName = albumName;
        this.photoAmount = 0;
        this.photos = new ArrayList<Photo>();
    }

    public String getAlbumName(){
        return this.albumName;
    }

    public ArrayList<Photo> getPhotos() { return this.photos; }

    public String toString(){
        return this.albumName;
    }


}
