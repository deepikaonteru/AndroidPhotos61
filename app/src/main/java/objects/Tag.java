package objects;

import java.io.Serializable;

public class Tag implements Serializable {
    public String tagType;
    public String tagValue;

    public Tag(String tagType, String tagValue){
        this.tagType = tagType;
        this.tagValue = tagValue;
    }
}
