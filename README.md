# microframework-shootout
Demonstration of multiple Java microframeworks.
Aim is to build Java application in a simple way, without having to select and use big frameworks.



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
