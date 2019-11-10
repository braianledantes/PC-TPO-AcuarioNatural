package actividades;

import hilos.Reloj;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO implementar FaroMirador
 * Faro-Mirador con vista a 40 m de altura y descenso en tobogán: Admira desde lo alto to-do el
 * esplendor de una maravilla natural y desciende en tobogán hasta una pileta. Para acceder al tobogán
 * es necesario subir por una escalera caracol, que tiene capacidad para n personas. Al llegar a la cima
 * nos encontraremos con dos toboganes para descender, la elección del tobogán es realizada por un
 * administrador de cola que indica que persona de la fila va a un tobogán y cuál va al otro. Es
 * importante destacar que una persona no se tira por el tobogán hasta que la anterior no haya llegado a
 * la pileta, es decir, sobre cada tobogán siempre hay a lo sumo una persona.
 */
public class FaroMiradorLocks implements Actividad {
    private boolean abierto;
    private Tirarse t;
    private Lock lock;
    private Condition subir, mirar, tirarse, administrar;
    private int capacidadEscalera, tirandose, subiendo, mirando, capacidadMirador;

    public FaroMiradorLocks(int capacidadEscalera) {
        this.abierto = false;
        this.capacidadEscalera = capacidadEscalera;
        this.capacidadMirador = capacidadEscalera;
        this.mirando = 0;
        lock = new ReentrantLock(true);
        subir = lock.newCondition();
        mirar = lock.newCondition();
        tirarse = lock.newCondition();
        administrar = lock.newCondition();
        new Thread(new Admin(this)).start();
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public void entrar() {
        // TODO terminar metodo entrar()
        lock.lock();
        try {
            while (subiendo == capacidadEscalera) {
                subir.await();
            }
            subiendo++;
            System.out.println(Thread.currentThread().getName() + " subiendo al faro");
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
        Reloj.dormirHilo(3,4);
    }

    public void adminarVista() {
        // TODO terminar metodo adminarVista()
        lock.lock();
        try {
            while (mirando == capacidadMirador) {
                mirar.await();
            }
            subiendo--;
            mirando++;
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }

        System.out.println(Thread.currentThread().getName() + " admirando vista desde faro");
        Reloj.dormirHilo(2, 5);
    }

    public void desenderPorTobogan() {
        // TODO ver para que se tiren a distinta velocidad y que cada tobogan sea una condition
        lock.lock();
        administrar.signal();
        Tirarse porDonde;
        while (Tirarse.ESPERAR == (porDonde = queHago())){
            try {
                tirarse.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Tirarse.TOBOGAN_A == porDonde){
            System.out.println(Thread.currentThread().getName() + " me tiro por el tobogan A");
        } else if (Tirarse.TOBOGAN_B == porDonde) {
            System.out.println(Thread.currentThread().getName() + " me tiro por el tobogan B");
        }
        tirandose++;
        mirando--;
        lock.unlock();
        Reloj.dormirHilo(1,1);
    }

    private Tirarse queHago() {
        return t;
    }

    public void administrar() {
        lock.lock();
        while (mirando == 0 || tirandose > 2){
            try {
                t = Tirarse.ESPERAR;
                administrar.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (t == Tirarse.ESPERAR){
            t = Tirarse.TOBOGAN_A;
        } else if (t == Tirarse.TOBOGAN_A){
            t = Tirarse.TOBOGAN_B;
        } else {
            t = Tirarse.TOBOGAN_A;
        }
        lock.unlock();
    }

    @Override
    public void salir() {
        // TODO implementar metodo salir()
        lock.lock();
        tirandose--;
        System.out.println(Thread.currentThread().getName() + " salio del faromirador");
        administrar.signal();
        lock.unlock();
    }

    @Override
    public void cerrar() {
        // TODO implementar metodo cerrar()
        abierto = false;
    }

    @Override
    public boolean isAbierto() {
        return abierto;
    }

    private enum Tirarse {
        ESPERAR,
        TOBOGAN_A,
        TOBOGAN_B
    }

    private static class Admin implements Runnable {
        FaroMiradorLocks faroMirador;

        public Admin(FaroMiradorLocks faroMirador) {
            this.faroMirador = faroMirador;
        }

        @Override
        public void run() {
            while (true) {
                faroMirador.administrar();
            }
        }
    }
}
