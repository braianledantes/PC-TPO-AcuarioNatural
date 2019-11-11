package test;

import actividades.FaroMirador;

public class Test_FaroMirador {
    public static void main(String[] args) {
        Thread[] visitantes = new Thread[7];
        FaroMirador faroMirador = new FaroMirador(5, 7);
        faroMirador.abrir();
        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (faroMirador.entrar()) {
                        faroMirador.admirarVista();
                        faroMirador.desenderPorTobogan();
                        faroMirador.salir();
                    } else {
                        System.out.println("no pudo entrar");
                    }
                }
            }, "v" + i);
            visitantes[i].start();
        }
    }
}
