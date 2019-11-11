package hilos;

import actividades.FaroMirador;

public class AdminFaroMirador implements Runnable {
    FaroMirador faroMirador;

    public AdminFaroMirador(FaroMirador faroMirador) {
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