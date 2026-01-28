package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dao.NewspaperRepository;
import dao.mapper.NewspaperEntityMapper;
import dao.model.NewspaperEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NewspaperRepositoryImp implements NewspaperRepository {
    private MongoCollection<Document> collection;
    private NewspaperEntityMapper mapper;

    public NewspaperRepositoryImp() {
    }

    @Inject
    public NewspaperRepositoryImp(MongoCollection<Document> collection, NewspaperEntityMapper mapper) {
        this.collection = collection;
        this.mapper = mapper;
    }


    @Override
    public List<NewspaperEntity> getAll() {
        List<NewspaperEntity> newspapers = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                NewspaperEntity newspaper = mapper.documentToEntity(doc);
                newspapers.add(newspaper);
            }
        }
        return newspapers;
    }
}
