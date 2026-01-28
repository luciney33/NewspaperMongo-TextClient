package dao.mapper;

import dao.model.ArticleEntity;
import dao.model.NewspaperEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class NewspaperEntityMapper {

    public NewspaperEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        NewspaperEntity entity = new NewspaperEntity();
        entity.setId(doc.getObjectId("_id"));
        entity.setName(doc.getString("name"));
        entity.setArticles(new ArrayList<>());

        List<Document> articlesDoc = doc.getList("articles", Document.class);
        if (articlesDoc != null) {
            for (Document articleDoc : articlesDoc) {
                entity.getArticles().add(ArticleEntityMapper.documentToEntity(articleDoc));
            }
        }

        return entity;
    }

    public Document entityToDocument(NewspaperEntity entity) {
        if (entity == null) {
            return null;
        }

        Document doc = new Document();
        if (entity.getId() != null) {
            doc.append("_id", entity.getId());
        }
        doc.append("name", entity.getName());

        List<Document> articlesDoc = new ArrayList<>();
        if (entity.getArticles() != null) {
            for (ArticleEntity article : entity.getArticles()) {
                articlesDoc.add(ArticleEntityMapper.entityToDocument(article));
            }
        }
        doc.append("articles", articlesDoc);

        return doc;
    }
}

