# microframework-shootout
Demonstration of multiple Java microframeworks.
Aim is to build Java application in a simple way, without having to select and use big frameworks.
* Ninja Framework
* Jooby
* Pippo



# Ninja Framework

##Getting started
mvn archetype:generate -DarchetypeGroupId=org.ninjaframework -DarchetypeArtifactId=ninja-servlet-archetype-simple

mvn clean install
mvn ninja:run

localhost:8080
Will give you a frontend and REST api.


SuperDevMode: more like super restart mode, on compile, server quickly restarts

You can use JPA with a relational database. An example archetype exists: ninja-servlet-jpa-blog-archetype. But since we all
know JPA very well, let's have a look at how we can use MongoDB.

## Add MongoDb

https://github.com/bihe/ninja-mongodb
add maven dependency:
      <dependency>
          <groupId>net.binggl</groupId>
          <artifactId>ninja-mongodb-module</artifactId>
          <version>1.0.7</version>
      </dependency>

Start MongoDB:
docker run -p 27017:27017 -d mongo

Add properties to application.conf
ninja.mongodb.host=localhost
ninja.mongodb.port=27017
ninja.mongodb.dbname=MyMongoDB


## Rest example
Create new Message class:
```java
@Entity
public class Message extends MorphiaModel {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```
Add MongoDao

```java
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
```

Add to ApplicationController:
```java
public Result saveMessage(Message message){
       this.mongoDao.saveMessage(message);
       System.out.println("saveMessageJson");

       return Results.noContent().status(201);
   }

   public Result findMessages(){

       return Results.json().render(this.mongoDao.findMessages());
   }
```
Add routes
```java
router.POST().route("/message.json").with(ApplicationController::saveMessage);
router.GET().route("/message.json").with(ApplicationController::findMessages);
```

Demo with Advanced rest client

## Ninja uses
Guice for dependency injection
JPA for relational database
Morphia for MongoDB

fat jar size: 28M


# Jooby

## Getting started
mvn archetype:generate -DarchetypeArtifactId=jooby-archetype -DarchetypeGroupId=org.jooby -DarchetypeVersion=1.1.3

mvn jooby:run

http://localhost:8080/

## MongoDB


<dependency>
  <groupId>org.jooby</groupId>
  <artifactId>jooby-morphia</artifactId>
  <version>1.1.3</version>
</dependency>

application.conf:
db = "mongodb://localhost/mydb"


<dependency>
     <groupId>org.jooby</groupId>
     <artifactId>jooby-jackson</artifactId>
   </dependency>

   ```java
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
   ```

```java
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
```

Modules demo:

```java
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
```

```java
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
```



//TODO: explain that yoiu can also do MVC style routes

# Pippo
mvn archetype:generate -DarchetypeGroupId=ro.pippo -DarchetypeArtifactId=pippo-quickstart -DarchetypeVersion=1.5.0

mvn compile exec:java

http://localhost:8338


## Add mongodb
application.properties:
mongodb.hosts = mongodb://localhost:27017

<dependency>
    <groupId>ro.pippo</groupId>
    <artifactId>pippo-session-mongodb</artifactId>
    <version>${pippo.version}</version>
</dependency>

Add to PippoApplication:
//instead of GET, can also use controllers or initializers
```java
public class PippoApplication extends Application {

    private final static Logger log = LoggerFactory.getLogger(PippoApplication.class);
    private MongoClient client;

    @Override
    protected void onInit() {
        this.client = MongoDBFactory.create(getPippoSettings());
        getRouter().ignorePaths("/favicon.ico");

        // send 'Hello World' as response
        GET("/", routeContext -> routeContext.send("Hello World"));

        // send a template as response
        GET("/template", routeContext -> {
            String message;

            String lang = routeContext.getParameter("lang").toString();
            if (lang == null) {
                message = getMessages().get("pippo.greeting", routeContext);
            } else {
                message = getMessages().get("pippo.greeting", lang);
            }

            routeContext.setLocal("greeting", message);
            routeContext.render("hello");
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
```


pom.xml:


PippoApplication.onInit:
  this.registerContentTypeEngine(GsonEngine.class);
