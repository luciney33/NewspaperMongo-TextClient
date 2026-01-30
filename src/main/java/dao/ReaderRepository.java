package dao;

import dao.model.CredentialEntity;
import dao.model.ReaderEntity;

import java.util.List;

public interface ReaderRepository {
    List<ReaderEntity> getAll();
    ReaderEntity get(String name);
    int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation);
    int delete(ReaderEntity reader,  boolean confirmation);

}
