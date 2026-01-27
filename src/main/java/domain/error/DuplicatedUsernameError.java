package domain.error;


import common.Constantes;

public final class DuplicatedUsernameError extends RuntimeException {
    public DuplicatedUsernameError() {
        super(Constantes.DUPLICATED_USERNAME_ERROR);
    }
}
