package dao;

import com.google.inject.Inject;
import models.Message;
import net.binggl.ninja.mongodb.MongoDB;

import java.util.List;

/**
 * Created by Erwin on 07/09/2017.
 */
public class MongoDao {

    private final MongoDB mongoDB;

    @Inject
    private MongoDao(MongoDB mongoDB) {
        this.mongoDB = mongoDB;
    }


    public void saveMessage(Message message){
        this.mongoDB.save(message);
    }

    public List<Message> findMessages(){
        return this.mongoDB.findAll(Message.class);
    }
}
