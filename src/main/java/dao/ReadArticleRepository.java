package dao;

import dao.model.ReadArticleEntity;

import java.util.List;

public interface ReadArticleRepository {
    List<ReadArticleEntity> getAllByArticleId();
    int save(ReadArticleEntity readArticle);
    void update(ReadArticleEntity readArticle);
    boolean delete(ReadArticleEntity readArticle);
}
