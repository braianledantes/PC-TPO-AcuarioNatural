package hilos;

import parque.Parque;

import java.util.Random;

public class Reloj extends Thread {
    public static final int DURACION_HORA = 10000; // en milisegundos
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
                Thread.sleep(DURACION_HORA);
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
     * Duerme un hilo entre los horas enviados por parámetro
     *
     * @param min segundos iniciales
     * @param max segundos limite
     */
    public static void dormirHilo(int min, int max) {
        try {
            min *= DURACION_HORA;
            max *= DURACION_HORA;
            if (max != 0)
                Thread.sleep(min + random.nextInt(max - min));
            else
                Thread.sleep(min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
