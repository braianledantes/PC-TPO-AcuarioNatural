package test;

import actividades.FaroMiradorLocks;

public class Test_FaroMirador {
    public static void main(String[] args) {
        Thread[] visitantes = new Thread[7];
        FaroMiradorLocks faroMiradorLocks = new FaroMiradorLocks(5);
        faroMiradorLocks.abrir();
        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    faroMiradorLocks.entrar();
                    faroMiradorLocks.admirarVista();
                    faroMiradorLocks.desenderPorTobogan();
                    faroMiradorLocks.salir();
                }
            }, "v" + i);
            visitantes[i].start();
        }
    }
}
