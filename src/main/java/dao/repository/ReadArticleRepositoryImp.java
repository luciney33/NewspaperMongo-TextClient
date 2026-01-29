package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dao.ReadArticleRepository;
import dao.mapper.ReadArticleEntityMapper;
import dao.model.ReadArticleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.conversions.Bson;

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
    public List<ReadArticleEntity> getAllByArticleId() {
        return List.of();
    }

    @Override
    public int save(ReadArticleEntity readArticle) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            System.err.println("❌ ReadArticle inválido");
            return 0;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
            Bson filterExisting = Filters.elemMatch("articles",
                    Filters.elemMatch("readarticle",
                            Filters.eq("_idReader", readArticle.getIdReader())));

            Document existingDoc = newspapersCollection.find(filterExisting).first();
            if (existingDoc != null) {
                System.out.println("⚠ El reader ya tiene un rating registrado");
                return -2; // Código especial para indicar que ya existe
            }

            // Buscar el newspaper y article (asumimos que se pasa en algún contexto)
            // Como no tenemos el ID del newspaper ni del article, añadiremos el rating al primer artículo encontrado
            Document firstNewspaper = newspapersCollection.find().first();

            if (firstNewspaper == null) {
                System.err.println("❌ No hay newspapers en la base de datos");
                return 0;
            }

            // Convertir entity a document
            Document readArticleDoc = ReadArticleEntityMapper.entityToDocument(readArticle);

            // Añadir el rating al primer artículo del primer newspaper
            Bson filter = Filters.eq("_id", firstNewspaper.getObjectId("_id"));
            Bson update = Updates.push("articles.0.readarticle", readArticleDoc);

            long modifiedCount = newspapersCollection.updateOne(filter, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Rating añadido correctamente");
                return 1;
            } else {
                System.err.println("❌ No se pudo añadir el rating");
                return 0;
            }

        } catch (Exception e) {
            System.err.println("❌ Error al añadir rating: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void update(ReadArticleEntity readArticle) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            System.err.println("❌ ReadArticle inválido");
            return;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");

            // Buscar el documento que contiene el rating del reader
            Bson filter = Filters.elemMatch("articles",
                    Filters.elemMatch("readarticle",
                            Filters.eq("_idReader", readArticle.getIdReader())));

            Document newspaper = newspapersCollection.find(filter).first();

            if (newspaper == null) {
                System.err.println("❌ Rating no encontrado para este reader");
                return;
            }

            // Actualizar el rating usando el operador posicional $
            Bson updateFilter = Filters.and(
                    Filters.eq("_id", newspaper.getObjectId("_id")),
                    Filters.eq("articles.readarticle._idReader", readArticle.getIdReader())
            );

            Bson update = Updates.set("articles.$[].readarticle.$[elem].rating", readArticle.getRating());

            // Usar arrayFilters para actualizar el elemento específico
            newspapersCollection.updateOne(
                    updateFilter,
                    update,
                    new com.mongodb.client.model.UpdateOptions()
                            .arrayFilters(List.of(Filters.eq("elem._idReader", readArticle.getIdReader())))
            );

            System.out.println("✅ Rating modificado correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al modificar rating: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(ReadArticleEntity readArticle) {
        if (readArticle == null || readArticle.getIdReader() == null) {
            System.err.println("❌ ReadArticle inválido");
            return false;
        }

        try {
            MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");

            // Buscar el documento que contiene el rating del reader
            Bson filter = Filters.elemMatch("articles",
                    Filters.elemMatch("readarticle",
                            Filters.eq("_idReader", readArticle.getIdReader())));

            Document newspaper = newspapersCollection.find(filter).first();

            if (newspaper == null) {
                System.err.println("❌ Rating no encontrado para este reader");
                return false;
            }

            // Eliminar el rating del array usando $pull
            Bson updateFilter = Filters.eq("_id", newspaper.getObjectId("_id"));
            Bson update = Updates.pull("articles.$[].readarticle",
                    Filters.eq("_idReader", readArticle.getIdReader()));

            long modifiedCount = newspapersCollection.updateOne(updateFilter, update).getModifiedCount();

            if (modifiedCount > 0) {
                System.out.println("✅ Rating eliminado correctamente");
                return true;
            } else {
                System.err.println("❌ No se pudo eliminar el rating");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar rating: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
