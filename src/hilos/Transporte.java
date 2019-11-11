package hilos;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Transporte extends Thread {
    private int capacidad;
    private boolean salioTren;
    private int cantAct;
    private CountDownLatch irDestino, volverParada;
    private Semaphore bajarse, arrancarTren, volver, mutex;

    public Transporte(String name, int capacidad) {
        super(name);
        this.capacidad = capacidad;
        salioTren = false;
        cantAct = 0;
        bajarse = new Semaphore(0);
        arrancarTren = new Semaphore(0);
        volver = new Semaphore(0);
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
            arrancarTren.acquire(); // se traba hasta que llegue el primer pasajero
            irDestino.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subirse() {
        try {
            mutex.acquire();
            if (++cantAct == 1) {
                arrancarTren.release(); // arranca el tren
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
            // para que no se suba nadie mas
            mutex.acquire();
            salioTren = true;
            System.out.println(getName() + " viajando con " + cantAct + " pasajeros chuu chuuu");
            mutex.release();

            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean disponible() {
        boolean salio = false;
        try {
            mutex.acquire();
            salio = salioTren || cantAct == capacidad;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return salio;
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
            volverParada.countDown();
            if (cantAct == 0) { // si es el ultimo libera el tren para que vuelva
                volver.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void volver() {
        try {
            //   volver.acquire(); // tren quiere irse
            volverParada.await();
            System.out.println(getName() + " volviendo");
            TimeUnit.SECONDS.sleep(5);
            mutex.acquire();
            salioTren = false;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
