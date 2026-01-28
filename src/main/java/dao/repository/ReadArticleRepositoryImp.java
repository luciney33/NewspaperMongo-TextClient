package dao.repository;

import com.mongodb.client.MongoCollection;
import dao.ReadArticleRepository;
import dao.model.ReadArticleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

import java.util.List;

@Log4j2
@ApplicationScoped
public class ReadArticleRepositoryImp implements ReadArticleRepository {
    private MongoCollection<Document> collection;

    // Constructor sin parámetros para CDI
    public ReadArticleRepositoryImp() {
    }

    @Inject
    public ReadArticleRepositoryImp(MongoCollection<Document> collection) {
        this.collection = collection;
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
