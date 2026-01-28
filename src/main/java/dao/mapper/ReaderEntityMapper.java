package dao.mapper;

import dao.model.ReaderEntity;
import dao.model.SubscriptionEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ReaderEntityMapper {

    public static ReaderEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        ReaderEntity entity = new ReaderEntity();
        entity.setId(doc.getObjectId("_id"));
        entity.setName(doc.getString("name"));
        entity.setDob(doc.getString("dob"));
        entity.setSubscriptions(new ArrayList<>());

        List<Document> subscriptionsDoc = doc.getList("subscriptions", Document.class);
        if (subscriptionsDoc != null) {
            for (Document subDoc : subscriptionsDoc) {
                entity.getSubscriptions().add(SubscriptionEntityMapper.documentToEntity(subDoc));
            }
        }

        return entity;
    }

    public static Document entityToDocument(ReaderEntity entity) {
        if (entity == null) {
            return null;
        }

        Document doc = new Document();
        if (entity.getId() != null) {
            doc.append("_id", entity.getId());
        }
        doc.append("name", entity.getName());
        doc.append("dob", entity.getDob());

        List<Document> subscriptionsDoc = new ArrayList<>();
        if (entity.getSubscriptions() != null) {
            for (SubscriptionEntity subscription : entity.getSubscriptions()) {
                subscriptionsDoc.add(SubscriptionEntityMapper.entityToDocument(subscription));
            }
        }
        doc.append("subscriptions", subscriptionsDoc);

        return doc;
    }
}

