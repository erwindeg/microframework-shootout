package nl.edegier;

import org.mongodb.morphia.annotations.Entity;

/**
 * Created by Erwin on 07/09/2017.
 */
@Entity
public class Message {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
