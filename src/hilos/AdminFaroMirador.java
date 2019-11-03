package hilos;

import actividades.FaroMirador;

import java.util.concurrent.Semaphore;

public class AdminFaroMirador implements Runnable {
    private FaroMirador faroMirador;
    private Semaphore tobogan1, tobogan2;

    public AdminFaroMirador(Semaphore tobogan1, Semaphore tobogan2) {
        this.tobogan1 = tobogan1;
        this.tobogan2 = tobogan2;
    }

    @Override
    public void run() {

    }
}
