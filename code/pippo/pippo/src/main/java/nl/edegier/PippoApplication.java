package nl.edegier;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.gson.GsonEngine;
import ro.pippo.session.SessionDataStorage;
import ro.pippo.session.SessionManager;
import ro.pippo.session.SessionRequestResponseFactory;
import ro.pippo.session.mongodb.MongoDBFactory;
import ro.pippo.session.mongodb.MongoDBSessionDataStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Pippo application.
 *
 * @see nl.edegier.PippoLauncher#main(String[])
 */
public class PippoApplication extends Application {

    private final static Logger log = LoggerFactory.getLogger(PippoApplication.class);
    private MongoClient client;

    @Override
    protected void onInit() {
        this.client = MongoDBFactory.create(getPippoSettings());
        this.registerContentTypeEngine(GsonEngine.class);
        getRouter().ignorePaths("/favicon.ico");

        // send 'Hello World' as response
        GET("/", routeContext -> routeContext.send("Hello World"));

        GET("/messages", routeContext -> {
            FindIterable<Document> documents = client.getDatabase("database").getCollection("messages").find();
            List<String> messages = new ArrayList<>();

            for (Document document : documents) {
                messages.add(document.toJson());
            }
            
            routeContext.json().send(messages);
        });

        POST("/messages", routeContext -> {
            Message message = routeContext.json().createEntityFromBody(Message.class);
            MongoCollection col = client.getDatabase("database").getCollection("messages", BasicDBObject.class);
            col.insertOne(message);
            routeContext.status(201).send("");
        });


    }

    @Override
    protected RequestResponseFactory createRequestResponseFactory() {
        SessionDataStorage sessionDataStorage = new MongoDBSessionDataStorage(this.client.getDatabase("database"));
        SessionManager sessionManager = new SessionManager(sessionDataStorage);

        return new SessionRequestResponseFactory(this, sessionManager);
    }

    @Override
    protected void onDestroy() {
        this.client.close();
    }

}
