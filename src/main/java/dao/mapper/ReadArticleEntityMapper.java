package dao.mapper;

import dao.model.ReadArticleEntity;
import org.bson.Document;

public class ReadArticleEntityMapper {

    public static ReadArticleEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        return ReadArticleEntity.builder()
                .idReader(doc.getInteger("id_reader", 0))
                .rating(doc.getInteger("rating", 0))
                .build();
    }

    public static Document entityToDocument(ReadArticleEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Document()
                .append("id_reader", entity.getIdReader())
                .append("rating", entity.getRating());
    }
}

