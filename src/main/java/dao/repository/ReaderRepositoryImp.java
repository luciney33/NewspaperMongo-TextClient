package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.ReaderRepository;
import dao.model.CredentialEntity;
import dao.model.ReaderEntity;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.List;

public class ReaderRepositoryImp implements ReaderRepository {
    private final MongoCollection<Document> collection;
    private final Gson gson;

    @Inject
    public ReaderRepositoryImp(MongoCollection<Document> collection, Gson gson) {
        this.collection = collection;
        this.gson = gson;
    }

    @Override
    public List<ReaderEntity> getAll() {
        return List.of();
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
