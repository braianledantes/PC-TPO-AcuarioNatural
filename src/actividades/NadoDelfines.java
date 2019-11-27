package actividades;

import cosas.Pileta;
import hilos.AdminNadoDelfines;
import hilos.Reloj;

import java.util.concurrent.Semaphore;

/**
 * Nado con delfines: para realizarla se dispone de 4 piletas. Es necesario que el visitante elija un
 * horario para realizar la actividad entre los horarios preestablecidos de la misma. Se conforman grupos
 * de 10 personas por pileta. En cada pileta nadaran dos delfines y la actividad dura aproximadamente
 * 45 minutos. La política del parque es que en cada horario puede haber solo 1 grupo incompleto (de
 * las 4 piletas)
 */
public class NadoDelfines implements Actividad {
    private AdminNadoDelfines admin;
    private int duracionTurno, cantAct, capacidad;
    private Pileta[] piletas;
    private boolean abierto, iniciar;

    public NadoDelfines(int cantPiletas, int capacidadPiletas) {
        this.capacidad = cantPiletas * capacidadPiletas;
        this.cantAct = 0;
        this.duracionTurno = Reloj.DURACION_HORA * 3 / 4; // 45 minutos
        this.piletas = new Pileta[cantPiletas];
        for (int i = 0; i < piletas.length; i++) {
            piletas[i] = new Pileta(capacidadPiletas);
        }
        this.iniciar = false;
        admin = new AdminNadoDelfines("AdminPiletas", this);
        admin.start();
    }

    public Pileta[] getPiletas() {
        return piletas;
    }

    public synchronized int getDuracionTurno() {
        return duracionTurno;
    }

    @Override
    public synchronized void abrir() {
        abierto = true;
    }

    @Override
    public synchronized boolean entrar() {
        while (abierto && cantAct == capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (abierto) cantAct++;
        System.out.println(Thread.currentThread().getName() + " entró a NadoDelfines");
        return abierto;
    }

    public Pileta entrarAPileta() {
        Pileta p = null;
        boolean entro;
        int i = 0;
        while (p == null && i < piletas.length) {
            entro = piletas[i].intentarEntrar();
            if (entro) p = piletas[i];
            i++;
        }
        return p;
    }

    /**
     * Método utilizado por el administrador de las piletas.
     */
    public synchronized void esperarTurno() {
        while (!iniciar) {
            try {
                System.err.println(Thread.currentThread().getName() + " esperando turno...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        iniciar = false;
    }

    public synchronized void iniciarTurno() {
        if (sePuedeIniciar()) {
            iniciar = true;
            notifyAll(); // despieta al administrador de las piletas
        }
    }

    /**
     * La política del parque es que en cada horario puede haber solo 1 grupo incompleto.
     *
     * @return
     */
    public boolean sePuedeIniciar() {
        int cantCompeta = 0;
        for (Pileta p : piletas) {
            if (p.isCompleta()) cantCompeta++;
        }
        return cantCompeta >= piletas.length - 1;
    }

    @Override
    public synchronized void salir() {
        cantAct--;
        System.out.println(Thread.currentThread().getName() + " salió de NadoDelfines");
        notifyAll();
    }

    @Override
    public synchronized void cerrar() {
        abierto = false;
    }

    @Override
    public synchronized boolean isAbierto() {
        return abierto;
    }
}
