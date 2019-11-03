package hilos;

import actividades.FaroMirador;

import java.util.concurrent.Semaphore;

/**
 * TODO ver como hacer la administracion de los visitantes que se quieren tirar por el tobogan, usar BlokingQueue
 */
public class AdminFaroMirador implements Runnable {
    private FaroMirador faroMirador;
    private Semaphore tobogan1, tobogan2;

    public AdminFaroMirador(Semaphore tobogan1, Semaphore tobogan2) {
        this.tobogan1 = tobogan1;
        this.tobogan2 = tobogan2;
    }

    @Override
    public void run() {
        // TODO implementar run()
    }
}
