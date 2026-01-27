package domain.service;

import dao.CredentialRepository;
import dao.model.CredentialEntity;
import domain.model.CredentialDTO;
import jakarta.inject.Inject;
import lombok.Data;

@Data
public class CredentialService {
    private final CredentialRepository credentialRepository;

    @Inject
    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public boolean checkLogin(CredentialDTO credentialDTO) {
        CredentialEntity credentialEntity = credentialRepository.get(credentialDTO.getUsername());
        if (credentialEntity == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return credentialEntity.getPassword().equals(credentialDTO.getPassword());
    }
}
