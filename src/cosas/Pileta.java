package cosas;

import actividades.NadoDelfines;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pileta {
    private int capacidad, cantAct;
    private boolean entrar, inicio, termino;
    private Lock lock;
    private Condition nadar, salir;

    public Pileta(int capacidad) {
        this.capacidad = capacidad;
        entrar = true;
        inicio = false;
        termino = false;
        cantAct = 0;
        lock = new ReentrantLock(true);
        nadar = lock.newCondition();
        salir = lock.newCondition();
    }

    public void iniciar() {
        lock.lock();
        entrar = false;
        inicio = true;
        termino = false;
        nadar.signalAll();
        lock.unlock();
    }

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

    public void esperarAQueInicie() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " esperando que abran la pileta");
            while (!inicio) {
                nadar.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void nadarConDelfines() {
        System.out.println(Thread.currentThread().getName() + " nadando con delfines...");
    }

    public void terminar() {
        lock.lock();
        entrar = true;
        inicio = false;
        termino = true;
        salir.signalAll();
        lock.unlock();
    }

    public void salir() {
        lock.lock();
        try {
            while (!termino) {
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

    public boolean isCompleta() {
        boolean completa;
        lock.lock();
        completa = cantAct == capacidad;
        lock.unlock();
        return completa;
    }
}
