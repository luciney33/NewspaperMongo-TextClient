package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dao.ReadArticleRepository;
import dao.mapper.ReadArticleEntityMapper;
import dao.model.ReadArticleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@ApplicationScoped
public class ReadArticleRepositoryImp implements ReadArticleRepository {
    private MongoCollection<Document> collection;
    private MongoDatabase database;

    public ReadArticleRepositoryImp() {
    }

    @Inject
    public ReadArticleRepositoryImp(MongoCollection<Document> collection, MongoDatabase database) {
        this.collection = collection;
        this.database = database;
    }


    @Override
    public List<ReadArticleEntity> getAllByArticleId(String description) {
        List<ReadArticleEntity> readArticles = new ArrayList<>();
        MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");

        try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document newspaper = cursor.next();
                List<Document> articles = newspaper.getList("articles", Document.class);
                if (articles != null) {
                    for (Document article : articles) {
                        String articleDescription = article.getString("description");
                        if (description.equals(articleDescription)) {
                            List<Document> readarticles = article.getList("readarticle", Document.class);
                            if (readarticles != null) {
                                for (Document readarticleDoc : readarticles) {
                                    ReadArticleEntity readArticleEntity = ReadArticleEntityMapper.documentToEntity(readarticleDoc);
                                    if (readArticleEntity != null) {
                                        readArticles.add(readArticleEntity);
                                    }
                                }
                            }
                            return readArticles;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al obtener readarticles por descripción de artículo: " + e.getMessage(), e);
        }

        return readArticles;
    }

    @Override
    public int save(ReadArticleEntity readArticle, String articleDescription) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            log.warn("ReadArticle inválido");
            return -1;
        }

        if (articleDescription == null || articleDescription.trim().isEmpty()) {
            log.warn("Article description inválida");
            return -1;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
            Document targetNewspaper = null;
            int articleIndex = -1;
            int existingRatingIndex = -1;

            try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document newspaper = cursor.next();
                    List<Document> articles = newspaper.getList("articles", Document.class);

                    if (articles != null) {
                        for (int i = 0; i < articles.size(); i++) {
                            Document article = articles.get(i);
                            String desc = article.getString("description");

                            if (articleDescription.equals(desc)) {
                                targetNewspaper = newspaper;
                                articleIndex = i;
                                List<Document> readarticles = article.getList("readarticle", Document.class);
                                if (readarticles != null) {
                                    for (int j = 0; j < readarticles.size(); j++) {
                                        Document ra = readarticles.get(j);
                                        if (readArticle.getIdReader().equals(ra.getObjectId("_idReader"))) {
                                            existingRatingIndex = j;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (targetNewspaper != null) break;
                }
            }

            if (targetNewspaper == null) {
                log.warn("No se encontró artículo con descripción: " + articleDescription);
                return -1;
            }
            List<Document> articles = targetNewspaper.getList("articles", Document.class);
            Document targetArticle = articles.get(articleIndex);
            List<Document> readarticles = targetArticle.getList("readarticle", Document.class);
            if (readarticles == null) {
                readarticles = new ArrayList<>();
            }
            Document readArticleDoc = ReadArticleEntityMapper.entityToDocument(readArticle);
            if (existingRatingIndex != -1) {
                log.info("El reader ya tiene un rating en este artículo. Actualizando...");
                readarticles.set(existingRatingIndex, readArticleDoc);
            } else {
                readarticles.add(readArticleDoc);
            }

            targetArticle.put("readarticle", readarticles);
            articles.set(articleIndex, targetArticle);
            targetNewspaper.put("articles", articles);

            Document query = new Document("_id", targetNewspaper.getObjectId("_id"));
            long modifiedCount = newspapersCollection.replaceOne(query, targetNewspaper).getModifiedCount();

            if (modifiedCount == 0) {
                log.warn("ninguna fila modifica del ReadArticle");
                return -1;
            }

            if (existingRatingIndex != -1) {
                log.info("Rating actualizado correctamente");
            } else {
                log.info("Rating añadido correctamente");
            }

            return 1;

        } catch (Exception e) {
            log.error("Error al añadir rating: " + e.getMessage(), e);
            return -1;
        }
    }

    @Override
    public void update(ReadArticleEntity readArticle, String articleDescription) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            log.warn("ReadArticle inválido");
            return;
        }

        if (articleDescription == null || articleDescription.trim().isEmpty()) {
            log.warn("Article description inválida");
            return;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
            Document targetNewspaper = null;
            int articleIndex = -1;
            int ratingIndex = -1;

            try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document newspaper = cursor.next();
                    List<Document> articles = newspaper.getList("articles", Document.class);

                    if (articles != null) {
                        for (int i = 0; i < articles.size(); i++) {
                            Document article = articles.get(i);
                            String desc = article.getString("description");

                            if (articleDescription.equals(desc)) {
                                List<Document> readarticles = article.getList("readarticle", Document.class);
                                if (readarticles != null) {
                                    for (int j = 0; j < readarticles.size(); j++) {
                                        Document ra = readarticles.get(j);
                                        if (readArticle.getIdReader().equals(ra.getObjectId("_idReader"))) {
                                            targetNewspaper = newspaper;
                                            articleIndex = i;
                                            ratingIndex = j;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (targetNewspaper != null) break;
                        }
                    }
                    if (targetNewspaper != null) break;
                }
            }

            if (targetNewspaper == null || articleIndex == -1 || ratingIndex == -1) {
                log.warn("Rating no encontrado para este reader en el artículo: " + articleDescription);
                return;
            }
            List<Document> articles = targetNewspaper.getList("articles", Document.class);
            Document targetArticle = articles.get(articleIndex);
            List<Document> readarticles = targetArticle.getList("readarticle", Document.class);
            Document targetRating = readarticles.get(ratingIndex);
            targetRating.put("rating", readArticle.getRating());
            readarticles.set(ratingIndex, targetRating);
            targetArticle.put("readarticle", readarticles);
            articles.set(articleIndex, targetArticle);
            targetNewspaper.put("articles", articles);
            Document query = new Document("_id", targetNewspaper.getObjectId("_id"));
            newspapersCollection.replaceOne(query, targetNewspaper);

            log.info("Rating modificado correctamente");

        } catch (Exception e) {
            log.error("Error al modificar rating: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(ReadArticleEntity readArticle, String articleDescription) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            log.warn("ReadArticle inválido");
            return false;
        }

        if (articleDescription == null || articleDescription.trim().isEmpty()) {
            log.warn("Article description inválida");
            return false;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
            Document targetNewspaper = null;
            int articleIndex = -1;
            int ratingIndex = -1;

            try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document newspaper = cursor.next();
                    List<Document> articles = newspaper.getList("articles", Document.class);

                    if (articles != null) {
                        for (int i = 0; i < articles.size(); i++) {
                            Document article = articles.get(i);
                            String desc = article.getString("description");

                            if (articleDescription.equals(desc)) {
                                List<Document> readarticles = article.getList("readarticle", Document.class);
                                if (readarticles != null) {
                                    for (int j = 0; j < readarticles.size(); j++) {
                                        Document ra = readarticles.get(j);
                                        if (readArticle.getIdReader().equals(ra.getObjectId("_idReader"))) {
                                            targetNewspaper = newspaper;
                                            articleIndex = i;
                                            ratingIndex = j;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (targetNewspaper != null) break;
                        }
                    }
                    if (targetNewspaper != null) break;
                }
            }

            if (targetNewspaper == null || articleIndex == -1 || ratingIndex == -1) {
                log.warn("Rating no encontrado para este reader en el artículo: " + articleDescription);
                return false;
            }

            List<Document> articles = targetNewspaper.getList("articles", Document.class);
            Document targetArticle = articles.get(articleIndex);
            List<Document> readarticles = targetArticle.getList("readarticle", Document.class);
            readarticles.remove(ratingIndex);
            targetArticle.put("readarticle", readarticles);
            articles.set(articleIndex, targetArticle);
            targetNewspaper.put("articles", articles);
            Document query = new Document("_id", targetNewspaper.getObjectId("_id"));
            long modifiedCount = newspapersCollection.replaceOne(query, targetNewspaper).getModifiedCount();

            if (modifiedCount > 0) {
                log.info("Rating eliminado correctamente");
                return true;
            } else {
                log.warn("No se pudo eliminar el rating");
                return false;
            }

        } catch (Exception e) {
            log.error("Error al eliminar rating: " + e.getMessage(), e);
            return false;
        }
    }
}
