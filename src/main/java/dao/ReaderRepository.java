package dao;

import dao.model.CredentialEntity;
import dao.model.ReaderEntity;

import java.util.List;

public interface ReaderRepository {
    List<ReaderEntity> getAll();
    ReaderEntity get(String name);
    List<ReaderEntity> getAllByArticle(String description);

    int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation);
    int delete(ReaderEntity reader,  boolean confirmation);

}
