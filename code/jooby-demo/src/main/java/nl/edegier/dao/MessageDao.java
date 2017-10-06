package nl.edegier.dao;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import nl.edegier.model.Message;
import org.jooby.Env;
import org.jooby.Jooby;
import org.mongodb.morphia.Datastore;

import java.util.List;

/**
 * Created by Erwin on 06/10/2017.
 */
public class MessageDao implements Jooby.Module{

    @Inject
    Datastore datastore;

    public void saveMessage(Message message){
        datastore.save(message);
    }

    public List<Message> getMessages(){
        return datastore.find(Message.class).asList();
    }

    @Override
    public Config config() {
        return ConfigFactory.parseResources(getClass(), "MessageDao.properties");
    }

    @Override
    public void configure(Env env, Config config, Binder binder) throws Throwable {

    }
}
