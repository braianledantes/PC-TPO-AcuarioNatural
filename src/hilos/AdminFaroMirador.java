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
            faroMirador.administrar();
        }
    }
}