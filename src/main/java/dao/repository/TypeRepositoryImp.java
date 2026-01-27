package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.TypeRepository;
import dao.model.TypeEntity;
import jakarta.inject.Inject;
import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class TypeRepositoryImp implements TypeRepository {
    private final MongoCollection<Document> collection;
    private final Gson gson;

    @Inject
    public TypeRepositoryImp(MongoCollection<Document> collection, Gson gson) {
        this.collection = collection;
        this.gson = gson;
    }


    @Override
    public List<TypeEntity> getAllTypes() {
        return List.of();
    }
}
