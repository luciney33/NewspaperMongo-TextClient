package dao.repository;

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

    // Constructor sin parámetros para CDI
    public TypeRepositoryImp() {
    }

    @Inject
    public TypeRepositoryImp(MongoCollection<Document> collection) {
        this.collection = collection;
    }


    @Override
    public List<TypeEntity> getAllTypes() {
        return List.of();
    }
}
