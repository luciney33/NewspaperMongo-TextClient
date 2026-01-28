package dao.mapper;

import dao.model.SubscriptionEntity;
import org.bson.Document;

public class SubscriptionEntityMapper {

    public static SubscriptionEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        return SubscriptionEntity.builder()
                .id(doc.getObjectId("_id"))
                .build();
    }

    public static Document entityToDocument(SubscriptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Document()
                .append("_id", entity.getId());
    }
}

