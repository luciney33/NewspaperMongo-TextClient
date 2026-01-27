package ui;

import domain.model.CredentialDTO;
import domain.service.CredentialService;
import jakarta.inject.Inject;

public class CredentialUI {
    private final CredentialService credentialService;

    @Inject
    public CredentialUI(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    public boolean checkLogin(CredentialDTO credentialDTO) {
        return credentialService.checkLogin(credentialDTO);
    }
}
