package dao;

import dao.model.CredentialEntity;
import dao.model.ReaderEntity;

import java.util.List;

public interface ReaderRepository {
    List<ReaderEntity> getAll();
    ReaderEntity get(int id);
    List<ReaderEntity> getAllByArticle(int articleId);

    int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation);
    int delete(ReaderEntity reader,  boolean confirmation);

}
