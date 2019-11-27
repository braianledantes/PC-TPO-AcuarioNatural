package actividades;

import hilos.Reloj;

import java.util.concurrent.Semaphore;

/**
 * <p>
 * Restaurante: en el pago del acceso al parque se encuentra incluido el almuerzo y la merienda.
 * Existen tres restaurantes, pero solamente se puede consumir un almuerzo y una merienda en
 * cualquiera de ellos. Puede tomar el almuerzo en un restaurante y la merienda en otro. Los restaurantes
 * tienen capacidad limitada. Las personas son atendidas en orden de llegada. Los restaurantes tienen
 * habilitada una cola de espera.
 */
public class Restaurante implements Actividad {
    private boolean abierto;
    private String nombre;
    private Semaphore lugares, mutex;

    public Restaurante(String nombre, int capacidad) {
        this.nombre = nombre;
        mutex = new Semaphore(1);
        lugares = new Semaphore(capacidad, true);
        abierto = false;
    }

    @Override
    public void abrir() {
        try {
            mutex.acquire();
            abierto = true;
            mutex.release();
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public boolean entrar() {
        boolean entro = false;
        try {
            mutex.acquire();
            entro = abierto;
            mutex.release();
            if (entro)
                lugares.acquire();
            System.out.println(Thread.currentThread().getName() + " entro a " + nombre);
        } catch (InterruptedException ignored) {
        }
        return entro;
    }

    public void almorzar(int horas) {
        System.out.println(Thread.currentThread().getName() + " está almorzando en " + nombre);
        Reloj.dormirHilo(horas * Reloj.DURACION_HORA / 2, horas * Reloj.DURACION_HORA);
    }

    public void merendar(int horas) {
        System.out.println(Thread.currentThread().getName() + " está merendando en " + nombre);
        Reloj.dormirHilo(horas * Reloj.DURACION_HORA / 2, horas * Reloj.DURACION_HORA);
    }

    @Override
    public void salir() {
        lugares.release();
        System.out.println(Thread.currentThread().getName() + " salio de " + nombre);
    }

    @Override
    public void cerrar() {
        try {
            mutex.acquire();
            abierto = false;
            mutex.release();
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public boolean isAbierto() {
        boolean re = false;
        try {
            mutex.acquire();
            re = abierto;
            mutex.release();
        } catch (InterruptedException ignored) {
        }
        return re;
    }
}
