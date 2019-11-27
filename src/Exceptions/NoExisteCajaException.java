package Exceptions;

public class NoExisteCajaException extends RuntimeException {

    public NoExisteCajaException(int posicion) {
        super("No existe caja en la posicion " + posicion);
    }
}
