package hilos;

import parque.Parque;

import java.util.Random;

public class Reloj extends Thread {
    private int hora;
    private static Reloj reloj;
    private static Random random = new Random();
    private Parque parque;

    private Reloj(Parque parque) {
        this.hora = 8; // que arranque desde las 7:00 am xq no quiero esperar tanto
        this.parque = parque;
    }

    public static Reloj getInstance(Parque parque) {
        if (reloj == null) reloj = new Reloj(parque);
        return reloj;
    }

    public static int getHora() {
        return reloj.hora;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Son a las " + hora);
                Thread.sleep(10000); // una hora dura 10 segundos
                incrementarHora();
                switch (reloj.hora) {
                    case Parque.HORA_INICIO_INGRESO:
                        parque.abrir();
                        break;
                    case Parque.HORA_FIN_INGRESO:
                        parque.cerrarIngreso();
                        break;
                    case Parque.HORA_CIERRE:
                        parque.cerrar();
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void incrementarHora() {
        hora = (hora + 1) % 24;
    }

    /**
     * Duerme un hilo entre los segundos enviados por parámetro
     * @param de segundos iniciales
     * @param a segundos limite
     */
    public static void dormirHilo(int de, int a) {
        try {
            Thread.sleep((de * 1000)+ random.nextInt(a*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
