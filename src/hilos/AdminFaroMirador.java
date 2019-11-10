package hilos;

import actividades.FaroMiradorLocks;

public class AdminFaroMirador implements Runnable {
    FaroMiradorLocks faroMirador;

    public AdminFaroMirador(FaroMiradorLocks faroMirador) {
        this.faroMirador = faroMirador;
    }

    @Override
    public void run() {
        while (true) {
            try {
                faroMirador.administrar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}