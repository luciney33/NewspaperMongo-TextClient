package dao;

import dao.model.ArticleEntity;

import java.util.List;

public interface ArticleRepository {
    List<ArticleEntity> getAll();
    ArticleEntity get(String description);
    int save(ArticleEntity article, String newspaperName);
    boolean delete(String description, boolean confirmation);
    void update(ArticleEntity article);
}
