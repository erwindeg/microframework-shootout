package nl.edegier;

import com.mongodb.BasicDBObject;

/**
 * Created by Erwin on 07/09/2017.
 */

public class Message extends BasicDBObject {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
