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
                    try {
                        faroMiradorLocks.entrar();
                        faroMiradorLocks.admirarVista();
                        faroMiradorLocks.desenderPorTobogan();
                        faroMiradorLocks.salir();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "v" + i);
            visitantes[i].start();
        }

        Thread admin = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        faroMiradorLocks.administrar();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "admin");
        admin.start();

    }
}
