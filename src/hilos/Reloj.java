package hilos;

import parque.Parque;

import java.util.Random;

public class Reloj extends Thread {
    public static final int DURACION_HORA = 60000; // en milisegundos
    public static final int DURACION_MIN = DURACION_HORA / 60; // en milisegundos
    private int hora;
    private static Reloj reloj;
    private static Random random = new Random();
    private Parque parque;

    private Reloj(Parque parque, int horaInicio) {
        this.hora = horaInicio;
        this.parque = parque;
    }

    public static Reloj getInstance(Parque parque) {
        if (reloj == null) reloj = new Reloj(parque, 9);
        return reloj;
    }

    public static int getHora() {
        return reloj.hora;
    }

    public void run() {
        while (true){
            System.out.println("Son las " + hora + "hs");
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
            parque.actualizarActividades();
            try {
                Thread.sleep(DURACION_HORA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            incrementarHora();
        }
    }

    private void incrementarHora() {
        hora = (hora + 1) % 24;
    }

    /**
     * Duerme un hilo entre el tiempo en milisegundos enviados por parámetro,
     * si el máximo es cero duerme las horas enviadas por min
     */
    public static void dormirHilo(int min, int max) {
        try {
            if (max != 0)
                Thread.sleep(min + random.nextInt(max - min));
            else
                Thread.sleep(min);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
