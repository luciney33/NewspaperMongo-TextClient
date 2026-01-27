package dao;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import dao.model.ArticleEntity;
import dao.model.NewspaperEntity;
import dao.model.ReadArticleEntity;
import util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class NewspaperDAO {
    private final MongoCollection<Document> collection;
    private final Gson gson;

    @Inject
    public NewspaperDAO() {
        this.collection = MongoDBConnection.getCollection("newspapers");
        this.gson = new Gson();
    }

    public List<NewspaperEntity> findAll() {
        List<NewspaperEntity> newspapers = new ArrayList<>();
        collection.find().into(new ArrayList<>()).forEach(doc ->
            newspapers.add(gson.fromJson(doc.toJson(), NewspaperEntity.class)));
        return newspapers;
    }

    public NewspaperEntity findById(String id) {
        try {
            Document doc = collection.find(eq("_id", new ObjectId(id))).first();
            return doc != null ? gson.fromJson(doc.toJson(), NewspaperEntity.class) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public void addArticle(String newspaperId, Document articleDoc) {
        collection.updateOne(
            eq("_id", new ObjectId(newspaperId)),
            push("articles", articleDoc));
    }

    public void updateArticle(String newspaperId, int articleIndex, Document updatedArticle) {
        collection.updateOne(
            eq("_id", new ObjectId(newspaperId)),
            set("articles." + articleIndex, updatedArticle));
    }

    public void deleteArticle(String newspaperId, int articleIndex) {
        // Primero ponemos el elemento en null
        collection.updateOne(
            eq("_id", new ObjectId(newspaperId)),
            unset("articles." + articleIndex));

        // Luego eliminamos los nulls del array
        collection.updateOne(
            eq("_id", new ObjectId(newspaperId)),
            pull("articles", null));
    }

    public void addRating(String newspaperId, int articleIndex, Document rating) {
        collection.updateOne(
            eq("_id", new ObjectId(newspaperId)),
            push("articles." + articleIndex + ".readarticle", rating));
    }

    public void updateRating(String newspaperId, int articleIndex, int readerId, int newRating) {
        // Buscar el newspaper y actualizar el rating específico
        NewspaperEntity newspaper = findById(newspaperId);
        if (newspaper != null && articleIndex < newspaper.getArticles().size()) {
            ArticleEntity article = newspaper.getArticles().get(articleIndex);
            List<Document> readArticles = new ArrayList<>();

            for (int i = 0; i < article.getReadarticle().size(); i++) {
                Document ratingDoc = new Document();
                if (article.getReadarticle().get(i).getIdReader() == readerId) {
                    ratingDoc.append("id_reader", readerId).append("rating", newRating);
                } else {
                    ratingDoc.append("id_reader", article.getReadarticle().get(i).getIdReader())
                             .append("rating", article.getReadarticle().get(i).getRating());
                }
                readArticles.add(ratingDoc);
            }

            article.setReadarticle(new ArrayList<>());
            for (Document doc : readArticles) {
                article.getReadarticle().add(gson.fromJson(doc.toJson(), ReadArticleEntity.class));
            }

            Document articleDoc = Document.parse(gson.toJson(article));
            updateArticle(newspaperId, articleIndex, articleDoc);
        }
    }

    public void deleteRating(String newspaperId, int articleIndex, int readerId) {
        NewspaperEntity newspaper = findById(newspaperId);
        if (newspaper != null && articleIndex < newspaper.getArticles().size()) {
            ArticleEntity article = newspaper.getArticles().get(articleIndex);
            article.getReadarticle().removeIf(ra -> ra.getIdReader() == readerId);

            Document articleDoc = Document.parse(gson.toJson(article));
            updateArticle(newspaperId, articleIndex, articleDoc);
        }
    }

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        for (NewspaperEntity newspaper : findAll()) {
            for (ArticleEntity article : newspaper.getArticles()) {
                if (!types.contains(article.getType())) {
                    types.add(article.getType());
                }
            }
        }
        return types;
    }
}

