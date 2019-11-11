package hilos;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Transporte extends Thread {
    private int capacidad;
    private boolean puedenSubirse;
    private int cantAct;
    private CountDownLatch irDestino, volverParada;
    private Semaphore bajarse, arrancar, mutex;

    public Transporte(String name, int capacidad) {
        super(name);
        this.capacidad = capacidad;
        puedenSubirse = true;
        cantAct = 0;
        bajarse = new Semaphore(0);
        arrancar = new Semaphore(0);
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

    private void enEstacion() {
        try {
            System.out.println(getName() + " en la estacion");
            // se crea una nueva espera
            irDestino = new CountDownLatch(capacidad);
            arrancar.acquire(); // se traba hasta que llegue el primer pasajero
            irDestino.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subirse() {
        try {
            mutex.acquire();
            if (++cantAct == 1) {
                arrancar.release(); // arranca el transporte
            }
            mutex.release();
            irDestino.countDown();
            System.out.println(Thread.currentThread().getName() + " se subio al " + getName());
            bajarse.acquire(); // se traba para que no haga nada durante el viaje
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void viajar() {
        try {
            mutex.acquire();
            puedenSubirse = false; // para que no se suba nadie mas
            System.out.println(getName() + " viajando con " + cantAct + " pasajeros...");
            mutex.release();

            TimeUnit.SECONDS.sleep(5); // simula viajar

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * El transporte está disponible si no salio o si hay espacio
     * @return
     */
    public boolean disponible() {
        boolean r = false;
        try {
            mutex.acquire();
            r = puedenSubirse || cantAct < capacidad;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r;
    }

    private void llegarADestino() {
        try {
            mutex.acquire();
            volverParada = new CountDownLatch(cantAct);
            bajarse.release(cantAct); // libero los pasajeros para que se bajen
            System.out.println(getName() + " llegó a destino");
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bajarse() {
        try {
            mutex.acquire();
            cantAct--;
            System.out.println(Thread.currentThread().getName() + " se bajo del " + getName());
            volverParada.countDown(); // se bajo
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void volver() {
        try {
            volverParada.await(); // espera a que se bajen todos los pasajeros
            System.out.println(getName() + " volviendo...");
            TimeUnit.SECONDS.sleep(5);
            mutex.acquire();
            puedenSubirse = true;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
