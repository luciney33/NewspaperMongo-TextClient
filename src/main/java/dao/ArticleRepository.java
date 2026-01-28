package dao;

import dao.model.ArticleEntity;

import java.util.List;

public interface ArticleRepository {
    List<ArticleEntity> getAll();
    ArticleEntity get(String description);
    int save(ArticleEntity article, String newspaperName);
    boolean delete(int articleId, boolean confirmation);
    int update(String oldDescription, ArticleEntity updatedArticle);
}
