package actividades;

import java.util.concurrent.Semaphore;

/**
 * u0
 * <p>
 * Restaurante: en el pago del acceso al mas.Parque se encuentra incluido el almuerzo y la merienda.
 * Existen tres restaurantes, pero solamente se puede consumir un almuerzo y una merienda en
 * cualquiera de ellos. Puede tomar el almuerzo en un restaurante y la merienda en otro. Los restaurantes
 * tienen capacidad limitada. Las personas son atendidas en orden de llegada. Los restaurantes tienen
 * habilitada una cola de espera.
 */
public class Restaurante implements Actividad {
    private boolean abierto;
    private String nombre;
    private int cantAct;
    private Semaphore lugares, mutex;
    public static final int HORA_INICIO_ALMUERZO = 11;
    public static final int HORA_FIN_ALMUERZO = 13;
    public static final int HORA_INICIO_MERIENDA = 15;
    public static final int HORA_FIN_MERIENDA = 17;

    public Restaurante(String nombre, int capacidad) {
        this.nombre = nombre;
        mutex = new Semaphore(1);
        lugares = new Semaphore(capacidad, true);
        cantAct = 0;
        abierto = false;
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public boolean entrar() {
        // TODO cuando cierre el parque y hay gente en la fila los tiene que echar
        boolean pudoEntrar = false;
        try {
            lugares.acquire();
            mutex.acquire();
            cantAct++;
            mutex.release();
            System.out.println(Thread.currentThread().getName() + " entro a " + nombre);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void almorzar(int horas) {
        try {
            System.out.println(Thread.currentThread().getName() + " está almorzando en " + nombre);
            Thread.sleep(horas * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void merendar(int horas) {
        try {
            System.out.println(Thread.currentThread().getName() + " está merendando en " + nombre);
            Thread.sleep(horas * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salir() {
        try {
            mutex.acquire();
            if (abierto) {
                cantAct--;
                lugares.release();
            } else {
                cantAct = 0;
                lugares.release(cantAct);
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " salio de " + nombre);
    }

    @Override
    public void cerrar() {
        abierto = false;
        lugares.release();
    }

    @Override
    public boolean isAbierto() {
        return abierto;
    }
}
