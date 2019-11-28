package test;

import actividades.FaroMirador;
import hilos.Reloj;
import hilos.Visitante;
import parque.Parque;

public class TestFaroMirador {
    public static void main(String[] args) {
        int molinetes = 4, nVisitantes = 5;
        Parque parque = new Parque(molinetes);
        parque.abrir();
        Reloj.getInstance(parque).start(); // inicio el reloj
        Thread[] visitantes = new Thread[nVisitantes];

        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Visitante("V" + i, parque) {
                @Override
                public void run() {
                    while (true) {
                        visitarFaroMirador();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            visitantes[i].start();
        }
    }
}
