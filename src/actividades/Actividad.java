package actividades;

public interface Actividad {
    public void abrir();

    public boolean entrar() throws InterruptedException;

    public void salir();

    public void cerrar();
}
