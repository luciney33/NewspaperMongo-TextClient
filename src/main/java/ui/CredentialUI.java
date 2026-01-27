package ui;

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
