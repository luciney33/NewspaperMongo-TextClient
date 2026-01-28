package dao.mapper;

import dao.model.ArticleEntity;
import dao.model.ReadArticleEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ArticleEntityMapper {

    public static ArticleEntity documentToEntity(Document doc) {
        if (doc == null) {
            return null;
        }

        ArticleEntity entity = new ArticleEntity();
        entity.setDescription(doc.getString("description"));
        entity.setType(doc.getString("type"));
        entity.setReadarticle(new ArrayList<>());

        List<Document> readArticlesDoc = doc.getList("readarticle", Document.class);
        if (readArticlesDoc != null) {
            for (Document readDoc : readArticlesDoc) {
                entity.getReadarticle().add(ReadArticleEntityMapper.documentToEntity(readDoc));
            }
        }

        return entity;
    }

    public static Document entityToDocument(ArticleEntity entity) {
        if (entity == null) {
            return null;
        }

        Document doc = new Document();
        doc.append("description", entity.getDescription());
        doc.append("type", entity.getType());

        List<Document> readArticlesDoc = new ArrayList<>();
        if (entity.getReadarticle() != null) {
            for (ReadArticleEntity readArticle : entity.getReadarticle()) {
                readArticlesDoc.add(ReadArticleEntityMapper.entityToDocument(readArticle));
            }
        }
        doc.append("readarticle", readArticlesDoc);

        return doc;
    }
}

