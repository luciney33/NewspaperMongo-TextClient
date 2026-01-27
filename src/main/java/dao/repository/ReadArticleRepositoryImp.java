package dao.repository;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import dao.ReadArticleRepository;
import dao.model.ReadArticleEntity;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

import java.util.List;

@Log4j2
public class ReadArticleRepositoryImp implements ReadArticleRepository {
    private final MongoCollection<Document> collection;
    private final Gson gson;

    @Inject
    public ReadArticleRepositoryImp(MongoCollection<Document> collection, Gson gson) {
        this.collection = collection;
        this.gson = gson;
    }


    @Override
    public List<ReadArticleEntity> getAllByArticleId() {
        return List.of();
    }

    @Override
    public int save(ReadArticleEntity readArticle) {
        return 0;
    }

    @Override
    public void update(ReadArticleEntity readArticle) {

    }

    @Override
    public boolean delete(ReadArticleEntity readArticle) {
        return false;
    }
}
