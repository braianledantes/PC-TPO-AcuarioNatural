package test;

import hilos.Reloj;
import hilos.Visitante;
import parque.Parque;

public class TestNadoDelfines {
    public static void main(String[] args) {
        int molinetes = 4, nVisitantes = 45;
        Parque parque = new Parque(molinetes);
        parque.abrir();
        Reloj.getInstance(parque).start(); // inicio el reloj
        Thread[] visitantes = new Thread[nVisitantes];

        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(new Visitante(parque) {
                @Override
                public void run() {
                    while (true){
                        vistarNadoDelfines();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, "V" + i);
            visitantes[i].start();
        }
    }
}

