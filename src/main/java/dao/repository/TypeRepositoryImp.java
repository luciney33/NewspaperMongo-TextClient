package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.TypeRepository;
import dao.model.TypeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TypeRepositoryImp implements TypeRepository {
    private MongoCollection<Document> collection;
    private Gson gson;

    // Constructor sin parámetros para CDI
    public TypeRepositoryImp() {
    }

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
