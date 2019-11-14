package parque;

import actividades.*;
import hilos.Transporte;

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
    public static final int CANT_RESTUARANTES = 3;
    public static final int HORA_INICIO_INGRESO = 9;
    public static final int HORA_FIN_INGRESO = 17;
    public static final int HORA_CIERRE = 18;
    private boolean abierto;
    private int molinetes, hCierre;
    private Restaurante[] restaurantes;
    private FaroMirador faroMirador;
    private CarreraGomones carreraGomones;
    private Transporte[] colectivos;


    public Parque(int molinetes, int hCierre) {
        this.molinetes = molinetes;
        this.abierto = false;
        this.hCierre = hCierre;
        this.restaurantes = new Restaurante[CANT_RESTUARANTES];

        String[] nombresRestaurantes = {"Gusto Restaurant", "Morfi", "Macdonals"};

        for (int i = 0; i < restaurantes.length; i++) {
            restaurantes[i] = new Restaurante(nombresRestaurantes[i], 10);
        }
        this.colectivos = new Transporte[2];
        for (int i = 0; i < colectivos.length; i++) {
            colectivos[i] = new Transporte("Colectivo" + i,8, 2);
            colectivos[i].start();
        }
        faroMirador = new FaroMirador(5, 10);
        carreraGomones = new CarreraGomones(5, 3, 15);
    }

    public synchronized void abrir() {
        // TODO abrir todas las actividades
        for (Restaurante r : restaurantes) {
            r.abrir();
        }
        faroMirador.abrir();
        carreraGomones.abrir();
        abierto = true;
        for (int i = 0; i < colectivos.length; i++) {
            colectivos[i].abrir();
        }
    }

    public boolean tomarCole() {
        boolean tomoUno = false;
        int i = 0;
        while (!tomoUno && i < colectivos.length) {
            tomoUno = colectivos[i].subirse();
            if (tomoUno) {
                colectivos[i].bajarse();
            }
            i++;
        }
        return tomoUno;
    }

    public boolean entrar() {
        // TODO implementar
       // System.out.println(Thread.currentThread().getName() + " entro al parque");
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

    public CarreraGomones entrarCarreraGomones() {
        CarreraGomones carrera = null;
        if (faroMirador.isAbierto()) {
            carrera = carreraGomones;
        }
        return carrera;
    }

    @Override
    public void salir() {
        // TODO implementar
        System.out.println(Thread.currentThread().getName() + " salió del parque.");
    }

    public synchronized void cerrarIngreso() {
        // TODO cerrar todas las actividades
        for (Restaurante r : restaurantes) {
            r.cerrar();
        }
        faroMirador.cerrar();
        for (int i = 0; i < colectivos.length; i++) {
            colectivos[i].cerrar();
        }
        carreraGomones.cerrar();
        abierto = false;
    }

    public synchronized void cerrar() {
        abierto = false;
    }

    @Override
    public synchronized boolean isAbierto() {
        return abierto;
    }
}
