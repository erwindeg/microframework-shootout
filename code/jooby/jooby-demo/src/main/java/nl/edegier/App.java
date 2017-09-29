package nl.edegier;

import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.json.Jackson;
import org.jooby.mongodb.Monphia;
import org.mongodb.morphia.Datastore;

/**
 * @author jooby generator
 */
public class App extends Jooby {

    {
        get("/", () -> "Hello World!");
        use(new Monphia());
        use(new Jackson());

        get("/messages", () -> {
            Datastore ds = require(Datastore.class);
            return ds.find(Message.class).asList();

        });

        post("/messages", (request, response) -> {
            Message message = request.body().to(Message.class);
            Datastore ds = require(Datastore.class);
            ds.save(message);
            response.status(201);
        }).consumes(MediaType.json);

    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
