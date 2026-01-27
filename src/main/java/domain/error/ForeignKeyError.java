package domain.error;


import common.Constantes;

public final class ForeignKeyError extends RuntimeException {
    public ForeignKeyError() {
        super(Constantes.FOREIGN_KEY_ERROR);
    }
}
