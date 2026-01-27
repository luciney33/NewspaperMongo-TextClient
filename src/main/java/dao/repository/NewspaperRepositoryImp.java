package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.NewspaperRepository;
import dao.model.NewspaperEntity;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
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
        List<NewspaperEntity> newspapersList = new ArrayList<>();

        List<Document> documents = collection.find()
                .into(new ArrayList<>());

        for (Document document : documents) {
            NewspaperEntity newspaper = gson.fromJson(document.toJson(), NewspaperEntity.class);
            newspapersList.add(newspaper);
        }

        return newspapersList;
    }
}
