package actividades;

public interface Actividad {

    public void abrir();

    public void entrar() throws InterruptedException;

    public void salir();

    public void cerrar();

    public boolean isAbierto();
}
