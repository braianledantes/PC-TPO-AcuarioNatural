import actividades.*;

/**
 * TODO terminar las otras actividades y terminar el parque
 * Se desea simular el funcionamiento del parque ecológico “ECO-PCS”, un acuario natural, desde que los
 * visitantes llegan al parque hasta que se van.
 * Al parque se puede acceder en forma particular o por tour, en el caso del tour, se trasladan a través de
 * colectivos folklóricos con una capacidad no mayor a 25 personas, que llegan a un estacionamiento destinado
 * para tal fin. Al momento de arribar al parque se le entregarán pulseras a los visitantes que le permitirán el
 * acceso al parque.
 * El ingreso al parque está indicado a través del paso de k molinetes. Una vez ingresado, el visitante puede
 * optar por ir al shop o disfrutar de las actividades del parque.
 * En el shop se pueden adquirir suvenires de distinta clase, los cuales se pueden abonar en una de las dos cajas
 * disponibles.
 * El complejo se encuentra abierto para el ingreso de 09:00 a 17:00hs. Considere que las actividades cierran a
 * las 18.00 hrs.
 */
public class Parque implements Actividad {
    private int molinetes, hCierre;
    private Restaurante[] restaurantes;
    public static final int CANT_RESTUARANTES = 3;
    public static final int HORA_INICIO_INGRESO = 9;
    public static final int HORA_FIN_INGRESO = 17;
    public static final int HORA_CIERRE = 18;
    private FaroMirador faroMirador;

    public Parque(int molinetes, int hCierre) {
        this.molinetes = molinetes;
        this.hCierre = hCierre;
        this.restaurantes = new Restaurante[CANT_RESTUARANTES];
        for (int i = 0; i < restaurantes.length; i++) {
            restaurantes[i] = new Restaurante(2 + (i * 3));
        }
        faroMirador = new FaroMirador(10);
    }

    public void abrir() {
        // TODO terminar las otras actividades
        for (Restaurante r : restaurantes) {
            r.abrir();
        }
    }

    public boolean entrar() {
        // TODO implementar
        return true;
    }

    public Restaurante entrarAlRestaurante(int r) {
        Restaurante restaurante = null;
        if (restaurantes[r].isAbierto())
            restaurante = restaurantes[r];
        return restaurante;
    }

    public FaroMirador entrarAlFaroMirador() {
        FaroMirador retorno = null;
        if (faroMirador.isAbierto()) {
            retorno = faroMirador;
        }
        return retorno;
    }

    @Override
    public void salir() {
        // TODO implementar
    }

    public void cerrarIngreso() {
        // TODO implementar
    }

    public void cerrar() {
        // TODO terminar para las otras actividades
        for (Restaurante r : restaurantes) {
            r.cerrar();
        }
    }

    @Override
    public boolean isAbierto() {
        return false;
    }
}