package dao.repository;

import com.mongodb.client.MongoCollection;
import dao.mapper.ArticleEntityMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import dao.model.ArticleEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.*;


@Log4j2
@ApplicationScoped
public class ArticleRepositoryImp implements dao.ArticleRepository {
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

        if (newspaper == null) {
            log.error("No se encontró el periódico con nombre: {}", newspaperName);
            return 0;
        }
        long modifiedCount = collection.updateOne(
                new Document("_id", newspaper.getObjectId("_id")),
                new Document("$push", new Document("articles", articleDoc))
        ).getModifiedCount();

        log.info("Artículo '{}' añadido al periódico '{}'",
                 article.getDescription(),
                 newspaper.getString("name"));

        return (int) modifiedCount;
    }

    @Override
    public boolean delete(int articleId, boolean confirmation) {
        return false;
    }

    @Override
    public int update(String oldDescription, ArticleEntity updatedArticle) {
        // Find the newspaper that contains the article with the old description
        Document query = new Document("articles.description", oldDescription);
        Document newspaper = collection.find(query).first();

        if (newspaper == null) {
            log.error("No se encontró ningún artículo con descripción: {}", oldDescription);
            return 0;
        }

        // Update the article using positional operator ($)
        Document updateQuery = new Document("articles.description", oldDescription);
        Document updateDoc = new Document("$set", new Document()
                .append("articles.$.description", updatedArticle.getDescription())
                .append("articles.$.type", updatedArticle.getType()));

        long modifiedCount = collection.updateOne(updateQuery, updateDoc).getModifiedCount();

        log.info("Artículo actualizado: '{}' -> '{}' (tipo: {})", 
                 oldDescription, 
                 updatedArticle.getDescription(), 
                 updatedArticle.getType());

        return (int) modifiedCount;
    }
}
