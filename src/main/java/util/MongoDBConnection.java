package util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
@ApplicationScoped
public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    @Inject
    private MongoDBConnection(MongoClient mongoClient, MongoDatabase database) {
        this.mongoClient = mongoClient;
        this.database = database;
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            mongoClient = MongoClients.create("mongodb://informatica.iesquevedo.es:2323/");
            database = mongoClient.getDatabase("lucia_peñafiel_MongoDB");
        }
        return database;
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
