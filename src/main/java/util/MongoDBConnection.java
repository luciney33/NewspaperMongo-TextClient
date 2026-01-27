package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.bson.Document;

@ApplicationScoped
public class MongoDBConnection {
    private static final String MONGO_URI = "mongodb://informatica.iesquevedo.es:2323/";
    private static final String DATABASE_NAME = "lucia_peñafiel_MongoDB";

    private static volatile MongoClient mongoClient;
    private static volatile MongoDatabase database;

    // Constructor sin parámetros para CDI
    public MongoDBConnection() {
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            synchronized (MongoDBConnection.class) {
                if (database == null) {
                    mongoClient = MongoClients.create(MONGO_URI);
                    database = mongoClient.getDatabase(DATABASE_NAME);
                }
            }
        }
        return database;
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    public static MongoClient getClient() {
        if (mongoClient == null) {
            getDatabase(); // Inicializa el cliente si no existe
        }
        return mongoClient;
    }

    // ==================== PRODUCTORES CDI ====================

    /**
     * Produce MongoClient para inyección CDI
     */
    @Produces
    @ApplicationScoped
    public MongoClient produceMongoClient() {
        return getClient();
    }

    /**
     * Produce MongoDatabase para inyección CDI
     */
    @Produces
    @ApplicationScoped
    public MongoDatabase produceMongoDatabase() {
        return getDatabase();
    }

    /**
     * Produce MongoCollection para inyección CDI
     * Infiere el nombre de la colección desde la clase que lo inyecta
     */
    @Produces
    public MongoCollection<Document> produceMongoCollection(InjectionPoint injectionPoint) {
        String collectionName = inferCollectionName(injectionPoint);
        return getCollection(collectionName);
    }

    /**
     * Produce Gson para inyección CDI
     */
    @Produces
    @Dependent
    public Gson produceGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    /**
     * Infiere el nombre de la colección MongoDB desde el nombre de la clase
     */
    private String inferCollectionName(InjectionPoint injectionPoint) {
        if (injectionPoint.getBean() == null) {
            return "default";
        }

        String className = injectionPoint.getBean().getBeanClass().getSimpleName();
        System.out.println("🔍 DEBUG inferCollectionName: Clase detectada = " + className);

        // Mapeo de nombres de clases a colecciones
        if (className.contains("Type")) {
            return "type";
        } else if (className.contains("ReadArticle")) {
            return "readArticle";
        } else if (className.contains("Article")) {
            return "articles";
        } else if (className.contains("Newspaper")) {
            System.out.println("🔍 DEBUG inferCollectionName: Retornando 'Newspapers'");
            return "Newspapers";
        } else if (className.contains("Reader")) {
            return "Readers";
        } else if (className.contains("Subscription")) {
            return "subscriptions";
        }

        // Fallback
        String baseName = className
                .replace("RepositoryImp", "")
                .replace("Repository", "")
                .replace("Imp", "");
        return baseName.toLowerCase() + "s";
    }

    @PreDestroy
    public void close() {
        if (mongoClient != null) {
            synchronized (MongoDBConnection.class) {
                if (mongoClient != null) {
                    mongoClient.close();
                    mongoClient = null;
                    database = null;
                }
            }
        }
    }
}
