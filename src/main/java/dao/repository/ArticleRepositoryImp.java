package dao.repository;

import com.mongodb.client.MongoCollection;
import dao.ArticleRepository;
import dao.mapper.ArticleEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dao.model.ArticleEntity;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.*;


@ApplicationScoped
public class ArticleRepositoryImp implements ArticleRepository {
    private MongoCollection<Document> collection;

    public ArticleRepositoryImp() {
    }

    @Inject
    public ArticleRepositoryImp(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public List<ArticleEntity> getAll() {
        List<ArticleEntity> articlesList = new ArrayList<>();

        List<Document> documents = collection.find()
                .projection(fields(excludeId(), include("articles")))
                .into(new ArrayList<>());

        for (Document document : documents) {
            List<Document> documentArticles = (List<Document>) document.get("articles");
            if (documentArticles != null) {
                for (Document article : documentArticles) {
                    articlesList.add(ArticleEntityMapper.documentToEntity(article));
                }
            }
        }

        return articlesList;
    }

    @Override
    public ArticleEntity get(String description) {
        List<Document> documents = collection.find()
                .projection(fields(excludeId(), include("articles")))
                .into(new ArrayList<>());

        for (Document document : documents) {
            List<Document> documentArticles = (List<Document>) document.get("articles");
            if (documentArticles != null) {
                for (Document article : documentArticles) {
                    ArticleEntity articleEntity = ArticleEntityMapper.documentToEntity(article);
                    if (articleEntity.getDescription().equalsIgnoreCase(description)) {
                        return articleEntity;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public int save(ArticleEntity article, String newspaperName) {
        Document articleDoc = ArticleEntityMapper.entityToDocument(article);
        Document newspaper = collection.find(new Document("name", newspaperName)).first();
        long modifiedcount = collection.updateOne(
                new Document("_id", newspaper.getObjectId("_id")),
                new Document("$push", new Document("articles", articleDoc))
        ).getModifiedCount();
        return (int) modifiedcount;
    }

    @Override
    public boolean delete(String description, boolean confirmation) {
        if (!confirmation) {
            return false;
        }

        long deletedCount = collection.updateMany(
                new Document("articles.description", description),
                new Document("$pull", new Document("articles",
                        new Document("description", description)))
        ).getModifiedCount();
        return deletedCount > 0;
    }

    @Override
    public void update(ArticleEntity article) {
        Document articleDoc = ArticleEntityMapper.entityToDocument(article);

        Document query = new Document("articles.description", article.getDescription());

        Document update = new Document("$set", new Document()
                .append("articles.$.description", article.getDescription())
                .append("articles.$.type", article.getType())
                .append("articles.$.readarticle", articleDoc.get("readarticle")));

        collection.updateOne(query, update).getModifiedCount();
    }
}
