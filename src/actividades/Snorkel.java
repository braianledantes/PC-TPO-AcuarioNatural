package actividades;

import hilos.AsistenteSnorkel;
import hilos.Reloj;

import java.util.concurrent.Semaphore;

/**
 * TODO hacer la actividad Snorkel
 * Disfruta de Snorkel ilimitado : existe la posibilidad de realizar snorkel en una laguna, para lo cual es
 * necesario adquirir previamente el equipo de snorkel, salvavidas y patas de ranas, que deberán ser
 * devueltos al momento de finalizar la actividad. En el ingreso a la actividad hay un stand donde dos
 * asistentes entregan el equipo mencionado. La única limitación en cuanto a capacidad es dada por la
 * cantidad de equipos completos (snorkel, salvavidas y patas de rana) existentes
 */
public class Snorkel implements Actividad {
    private Semaphore snorkel, salvavidas, patasRana, adquirirEquipo, entregarEquipo, mutex;
    private boolean abierto;
    private Thread[] asistentes;

    public Snorkel(int cantSnorkel, int cantSalvavidas, int cantPatasRana) {
        mutex = new Semaphore(1);
        snorkel = new Semaphore(cantSnorkel);
        salvavidas = new Semaphore(cantSalvavidas);
        patasRana = new Semaphore(cantPatasRana);
        adquirirEquipo = new Semaphore(0, true);
        entregarEquipo = new Semaphore(0, true);
        abierto = false;
        asistentes = new Thread[2];
        // se crean los asistentes
        for (int i = 0; i < asistentes.length; i++) {
            asistentes[i] = new Thread(new AsistenteSnorkel(this), "Asistente" + (i + 1));
            asistentes[i].start();
        }
    }

    @Override
    public void abrir() {
        try {
            mutex.acquire();
            abierto = true;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean entrar() {
        boolean entro = false;
        try {
            mutex.acquire();
            entro = abierto;
            if (entro) System.out.println(Thread.currentThread().getName() + " entro al Snorkel");
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return entro;
    }

    /**
     * Método utiizado por los visitantes
     */
    public void adquirirEquipo() {
        try {
            //System.out.println(Thread.currentThread().getName() + " esperando por obtener el equipo...");
            entregarEquipo.release();
            adquirirEquipo.acquire();
            System.out.println(Thread.currentThread().getName() + " obtuvo el equipo");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método utiizado por los asistentes
     */
    public void entregarEquipo() {
        try {
            entregarEquipo.acquire();
            salvavidas.acquire();
            patasRana.acquire(2);
            snorkel.acquire();
            System.out.println(Thread.currentThread().getName() + " entregando el equipo a un visitante...");
            Thread.sleep(Reloj.DURACION_HORA / 4); // nada 15 minutos
            adquirirEquipo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void nadar() {
        System.out.println(Thread.currentThread().getName() + " está nadando...");
        Reloj.dormirHilo(3, 4);
    }

    @Override
    public void salir() {
        salvavidas.release();
        patasRana.release(2);
        snorkel.release();
        System.out.println(Thread.currentThread().getName() + " salio de Snorkel");
    }

    @Override
    public void cerrar() {
        try {
            mutex.acquire();
            abierto = false;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAbierto() {
        boolean ret = false;
        try {
            mutex.acquire();
            ret = abierto;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
