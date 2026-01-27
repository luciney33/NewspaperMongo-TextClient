package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.NewspaperRepository;
import dao.model.NewspaperEntity;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.List;

public class NewspaperRepositoryImp implements NewspaperRepository {
    private final MongoCollection<Document> collection;
    private final Gson gson;
    @Inject
    public NewspaperRepositoryImp(MongoCollection<Document> collection, Gson gson) {
        this.collection = collection;
        this.gson = gson;
    }

    @Override
    public List<NewspaperEntity> getAll() {

        return List.of();
    }
}
