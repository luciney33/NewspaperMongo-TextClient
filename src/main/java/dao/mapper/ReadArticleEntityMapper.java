package dao.mapper;

import dao.model.ReadArticleEntity;
import org.bson.Document;

public class ReadArticleEntityMapper {

    public static ReadArticleEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        return ReadArticleEntity.builder()
                .idReader(doc.getObjectId("_idReader"))
                .rating(doc.getInteger("rating", 0))
                .build();
    }

    public static Document entityToDocument(ReadArticleEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Document()
                .append("_idReader", entity.getIdReader())
                .append("rating", entity.getRating());
    }
}

