package actividades;

import cosas.Pileta;
import hilos.AdminNadoDelfines;
import hilos.Reloj;

/**
 * Nado con delfines: para realizarla se dispone de 4 piletas. Es necesario que el visitante elija un
 * horario para realizar la actividad entre los horarios preestablecidos de la misma. Se conforman grupos
 * de 10 personas por pileta. En cada pileta nadaran dos delfines y la actividad dura aproximadamente
 * 45 minutos. La política del parque es que en cada horario puede haber solo 1 grupo incompleto (de
 * las 4 piletas)
 */
public class NadoDelfines implements Actividad {
    private int duracionTurno, cantAct, capacidad;
    private Pileta[] piletas;
    private boolean abierto, iniciar;

    public NadoDelfines(int cantPiletas, int capacidadPiletas) {
        this.capacidad = cantPiletas * capacidadPiletas;
        this.cantAct = 0;
        this.duracionTurno = Reloj.DURACION_MIN * 45; // 45 minutos
        this.piletas = new Pileta[cantPiletas];
        for (int i = 0; i < piletas.length; i++) {
            piletas[i] = new Pileta(capacidadPiletas);
        }
        this.iniciar = false;
        AdminNadoDelfines admin = new AdminNadoDelfines("AdminPiletas", this);
        admin.start();
    }

    /**
     * Solo el administrador de las piletas ejecuta este método.
     * @return el conjunto de las piletas
     */
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
            } catch (InterruptedException ignored) {
            }
        }
        if (abierto) {
            cantAct++;
            System.out.println(Thread.currentThread().getName() + " entró a NadoDelfines");
        }
        return abierto;
    }

    /**
     * El visitante recorre todas las piletas e intenta entrar a una.
     * @return  la pileta a la que pudo entrar, null si no entro a ninguna
     */
    public Pileta entrarAUnaPileta() {
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
     * Método que ejecuta el reloj para actualizar la actividad, se puede iniciar cuando esten todas las piletas
     * completas me nos una.
     */
    public synchronized void iniciarTurno() {
        if (sePuedeIniciar()) {
            iniciar = true;
            notifyAll(); // despieta al administrador de las piletas
        }
    }

    /**
     * Método que ejecuta el reloj.
     * La política del parque es que en cada horario puede haber solo 1 grupo incompleto.
     */
    private boolean sePuedeIniciar() {
        int cantCompeta = 0;
        for (Pileta p : piletas) {
            if (p.isCompleta()) cantCompeta++;
        }
        return cantCompeta >= piletas.length - 1;
    }

    /**
     * Método utilizado por el administrador de las piletas.
     */
    public synchronized void esperarTurno() {
        while (!iniciar) {
            try {
                System.out.println(Thread.currentThread().getName() + " esperando turno...");
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        iniciar = false; // para que vuelva a esperar en la proxima iteracion
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
