package cosas;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pileta {
    private int capacidad, cantAct;
    private boolean entrar, inicioActividad, terminoActividad;
    private Lock lock;
    private Condition nadar, salir;

    public Pileta(int capacidad) {
        this.capacidad = capacidad;
        entrar = true;
        inicioActividad = false;
        terminoActividad = false;
        cantAct = 0;
        lock = new ReentrantLock(true);
        nadar = lock.newCondition();
        salir = lock.newCondition();
    }

    /**
     * Método utilizado por el administrador, cuando paso una hora y se puede iniciar la actividad,
     * la inicia.
     */
    public void iniciar() {
        lock.lock();
        entrar = false;
        inicioActividad = true;
        terminoActividad = false;
        nadar.signalAll();
        lock.unlock();
    }

    /**
     * Método utilizado por el visitante para ver si puede entrar en esta pileta.
     * @return true si hay espacio y false de lo contrario
     */
    public boolean intentarEntrar() {
        boolean e;
        lock.lock();
        //System.out.println(Thread.currentThread().getName() + " intentando entrar a una pileta");
        if (e = entrar && cantAct < capacidad) {
            cantAct++;
        }
        lock.unlock();
        return e;
    }

    /**
     * Método que utiliza el visitante, espera a que el admin les diga que ya inicio la activiad.
     */
    public void esperarAQueInicie() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " esperando que abran la pileta");
            while (!inicioActividad) {
                nadar.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Método utilizado por el visitante.
     */
    public void nadarConDelfines() {
        System.out.println(Thread.currentThread().getName() + " nadando con delfines...");
    }

    /**
     * Método utilizado por el administrador, cuando pasó el tiempo que dura la actividad, les
     * dice a los visitantes que pueden salir.
     */
    public void terminar() {
        lock.lock();
        entrar = true;
        inicioActividad = false;
        terminoActividad = true;
        salir.signalAll();
        lock.unlock();
    }

    /**
     * Método utilizado por el visitante cuando quiete salir de la pileta.
     */
    public void salir() {
        lock.lock();
        try {
            while (!terminoActividad) { // mientras no termino la actividad espera
                salir.await();
            }
            cantAct--;
            System.out.println(Thread.currentThread().getName() + " chau de pileta");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     *  Pregunta si la pileta está llena.
     * @return true si la pileta está completa, false de lo contrio
     */
    public boolean isCompleta() {
        boolean completa;
        lock.lock();
        completa = cantAct == capacidad;
        lock.unlock();
        return completa;
    }
}
