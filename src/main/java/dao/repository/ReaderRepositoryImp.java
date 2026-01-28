package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dao.ReaderRepository;
import dao.mapper.ReaderEntityMapper;
import dao.model.CredentialEntity;
import dao.model.ReaderEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReaderRepositoryImp implements ReaderRepository {
    private MongoCollection<Document> collection;

    // Constructor sin parámetros para CDI
    public ReaderRepositoryImp() {
    }

    @Inject
    public ReaderRepositoryImp(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public List<ReaderEntity> getAll() {
        List<ReaderEntity> readers = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ReaderEntity reader = ReaderEntityMapper.documentToEntity(doc);
                readers.add(reader);
            }
        }

        return readers;
    }

    @Override
    public ReaderEntity get(int id) {
        return null;
    }

    @Override
    public List<ReaderEntity> getAllByArticle(int articleId) {
        return List.of();
    }

    @Override
    public int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation) {
        return 0;
    }

    @Override
    public int delete(ReaderEntity reader, boolean confirmation) {
        return 0;
    }
}
