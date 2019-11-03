package actividades;

import actividades.Actividad;

public class Tienda implements Actividad {
    @Override
    public void abrir() {

    }

    @Override
    public boolean entrar() {
        return true;
    }

    @Override
    public void salir() {

    }

    @Override
    public void cerrar() {

    }

    @Override
    public boolean isAbierto() {
        return false;
    }
}
