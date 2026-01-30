package dao.mapper;

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
}

