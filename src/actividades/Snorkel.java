package actividades;

/**
 * Disfruta de Snorkel ilimitado : existe la posibilidad de realizar snorkel en una laguna, para lo cual es
 * necesario adquirir previamente el equipo de snorkel, salvavidas y patas de ranas, que deberán ser
 * devueltos al momento de finalizar la actividad. En el ingreso a la actividad hay un stand donde dos
 * asistentes entregan el equipo mencionado. La única limitación en cuanto a capacidad es dada por la
 * cantidad de equipos completos (snorkel, salvavidas y patas de rana) existentes
 */
public class Snorkel implements Actividad {
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
