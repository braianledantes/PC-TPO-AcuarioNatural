package test;

import actividades.Tienda;

import java.util.Random;

public class TestTienda {
    public static void main(String[] args) {
        Tienda tienda = new Tienda(2);
        Random random = new Random(System.currentTimeMillis());
        Thread[] visitantes = new Thread[10];
        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(() -> {
                tienda.entrar();
                tienda.comprar();
                tienda.pagar(random.nextInt(tienda.getCantCajas()));
                tienda.salir();
            }, "V" + i);
            visitantes[i].start();
        }
    }
}
