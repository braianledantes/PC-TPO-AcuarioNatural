package test;

import hilos.Reloj;
import hilos.Visitante;
import parque.Parque;

public class TestRestaurante {
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
                    if (parque.isAbierto()) {
                        parque.entrar();
                        almorzar();
                        siguienteRestaurante();
                        merendar();
                        parque.salir();
                    }
                }
            };
            visitantes[i].start();
        }
    }
}
