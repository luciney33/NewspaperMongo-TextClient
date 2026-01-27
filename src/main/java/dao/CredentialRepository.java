package dao;

import dao.model.CredentialEntity;


public interface CredentialRepository {
    CredentialEntity get(String username);
}
