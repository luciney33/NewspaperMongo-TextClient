package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dao.TypeRepository;
import dao.model.TypeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TypeRepositoryImp implements TypeRepository {
    private MongoDatabase database;

    public TypeRepositoryImp() {
    }

    @Inject
    public TypeRepositoryImp(MongoDatabase database) {
        this.database = database;
    }


    @Override
    public List<TypeEntity> getAllTypes() {
        List<TypeEntity> types = new ArrayList<>();
        Set<String> uniqueTypes = new HashSet<>();

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
            try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document newspaper = cursor.next();
                    List<Document> articles = newspaper.getList("articles", Document.class);

                    if (articles != null) {
                        for (Document article : articles) {
                            String type = article.getString("type");
                            if (type != null && !type.trim().isEmpty()) {
                                uniqueTypes.add(type);
                            }
                        }
                    }
                }
            }
            for (String typeName : uniqueTypes) {
                types.add(TypeEntity.builder().name(typeName).build());
            }

        } catch (Exception e) {
            System.err.println("Error al obtener tipos: " + e.getMessage());
            e.printStackTrace();
        }

        return types;
    }
}
