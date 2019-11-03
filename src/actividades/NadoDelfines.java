package actividades;

/**
 * Nado con delfines: para realizarla se dispone de 4 piletas. Es necesario que el visitante elija un
 * horario para realizar la actividad entre los horarios preestablecidos de la misma. Se conforman grupos
 * de 10 personas por pileta. En cada pileta nadaran dos delfines y la actividad dura aproximadamente
 * 45 minutos. La política del parque es que en cada horario puede haber solo 1 grupo incompleto (de
 * las 4 piletas)
 */
public class NadoDelfines implements Actividad {
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
}
