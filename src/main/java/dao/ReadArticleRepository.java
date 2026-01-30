package dao;

import dao.model.ReadArticleEntity;

import java.util.List;

public interface ReadArticleRepository {
    List<ReadArticleEntity> getAllByArticleId();
    int save(ReadArticleEntity readArticle, String articleDescription);
    void update(ReadArticleEntity readArticle, String articleDescription);
    boolean delete(ReadArticleEntity readArticle, String articleDescription);
}
