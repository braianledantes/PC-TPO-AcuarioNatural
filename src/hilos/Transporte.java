package hilos;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Transporte extends Thread {
    private int capacidad, tiempoViaje;
    private boolean disponible, abierto;
    private int cantAct;
    private CountDownLatch irDestino, volverParada;
    private Semaphore bajarse, arrancarTren, mutex;

    public Transporte(String name, int capacidad, int tiempoViaje) {
        super(name);
        this.tiempoViaje = tiempoViaje;
        this.capacidad = capacidad;
        disponible = false;
        abierto = false;
        cantAct = 0;
        bajarse = new Semaphore(0);
        arrancarTren = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    @Override
    public void run() {
        while (true) {
            enEstacion();
            viajar();
            llegarADestino();
            volver();
        }
    }

    public synchronized void abrir() {
        abierto = true;
        notify();
    }

    public synchronized void cerrar() {
        abierto = false;
        notify();
    }

    private synchronized void esperarAQueAbra() {
        try {
            while (!abierto) {
                wait();
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void enEstacion() {
        try {
            esperarAQueAbra();
            System.out.println(getName() + " en la estacion");
            // se crea una nueva espera
            irDestino = new CountDownLatch(capacidad);
            mutex.acquire();
            disponible = true; // para que se suban
            mutex.release();
            arrancarTren.acquire(); // se traba hasta que llegue el primer pasajero
            irDestino.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    public boolean subirse() {
        boolean subirse = false;
        try {
            mutex.acquire();
            subirse = disponible && cantAct < capacidad;
            if (subirse) {
                if (++cantAct == 1) { // si es el primero arranca el tren
                    arrancarTren.release();
                }
            }
            mutex.release();
            if (subirse) {
                System.out.println(Thread.currentThread().getName() + " se subio al " + getName());
                irDestino.countDown();
            }
        } catch (InterruptedException ignored) {
        }
        return subirse;
    }

    private void viajar() {
        try {
            mutex.acquire();
            disponible = false;  // para que no se suba nadie mas
            System.out.println(getName() + " viajando con " + cantAct + " pasajeros...");
            mutex.release();

            Reloj.dormirHilo(tiempoViaje, tiempoViaje * 3 / 2);

        } catch (InterruptedException ignored) {
        }
    }

    private void llegarADestino() {
        try {
            mutex.acquire();
            volverParada = new CountDownLatch(cantAct);
            bajarse.release(cantAct); // libero los pasajeros para que se bajen
            System.out.println(getName() + " llegó a destino");
            mutex.release();
        } catch (InterruptedException ignored) {
        }
    }

    public void bajarse() {
        try {
            bajarse.acquire(); // se traba para que no haga nada durante el viaje
            mutex.acquire();
            cantAct--;
            System.out.println(Thread.currentThread().getName() + " se bajo del " + getName());
            volverParada.countDown();
            mutex.release();
        } catch (InterruptedException ignored) {
        }
    }

    private void volver() {
        try {
            volverParada.await(); // tren quiere irse
            System.out.println(getName() + " volviendo...");
            Reloj.dormirHilo(tiempoViaje, tiempoViaje * 3 / 2);
        } catch (InterruptedException ignored) {
        }
    }
}
