package nl.edegier.model;

import org.mongodb.morphia.annotations.Entity;


/**
 * Created by Erwin on 04/10/2017.
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