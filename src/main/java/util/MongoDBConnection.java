package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class MongoDBConnection {

    private final Configuration config;
    private MongoClient client;

    @Inject
    public MongoDBConnection(Configuration config) {
        this.config = config;
    }

    public MongoDatabase getDatabase(){
        String dbName = config.getProperty("databaseName");
        if(client == null){
            String url = config.getProperty("urlDBMongo");
            client = MongoClients.create(url);
            System.out.println("Connected to MongoDB successfully");
            return client.getDatabase(dbName);
        }

        return client.getDatabase(dbName);
    }



    @Produces
    @ApplicationScoped
    public MongoClient produceMongoClient() {
        if (client == null) {
            String url = config.getProperty("urlDBMongo");
            client = MongoClients.create(url);
        }
        return client;
    }


    @Produces
    @ApplicationScoped
    public MongoDatabase produceMongoDatabase() {
        return getDatabase();
    }


    @Produces
    public MongoCollection<Document> produceMongoCollection(InjectionPoint injectionPoint) {
        String collectionName = inferCollectionName(injectionPoint);
        return getDatabase().getCollection(collectionName);
    }


    private String inferCollectionName(InjectionPoint injectionPoint) {
        if (injectionPoint.getBean() == null) {
            return "default";
        }

        String className = injectionPoint.getBean().getBeanClass().getSimpleName();
        if (className.contains("Type")) {
            return "Newspapers";
        } else if (className.contains("ReadArticle")) {
            return "Newspapers";
        } else if (className.contains("Article")) {
            return "Newspapers";
        } else if (className.contains("Newspaper")) {
            return "Newspapers";
        } else if (className.contains("Reader")) {
            return "Readers";
        } else if (className.contains("Subscription")) {
            return "Readers";
        }

        String baseName = className
                .replace("RepositoryImp", "")
                .replace("Repository", "")
                .replace("Imp", "");
        return baseName.toLowerCase() + "s";
    }

    @PreDestroy
    public void close() {
        if (client != null) client.close();
    }
}
