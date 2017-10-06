package nl.edegier;

import nl.edegier.dao.MessageDao;
import nl.edegier.model.Message;
import org.jooby.Jooby;
import org.jooby.MediaType;
import org.jooby.json.Jackson;
import org.jooby.mongodb.Monphia;

/**
 * @author jooby generator
 */
public class App extends Jooby {

  {
    use(new Monphia());
    use(new Jackson());
    use(new MessageDao());

    get("/", () -> "Hello World!");

    get("/messages", () -> {
      return require(MessageDao.class).getMessages();
    });

    post("/messages", (request, response) -> {
      Message message = request.body().to(Message.class);
      require(MessageDao.class).saveMessage(message);
      response.status(201);
    }).consumes(MediaType.json);
  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
