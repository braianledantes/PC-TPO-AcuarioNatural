package test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SnorkelGonza {
    private Lock lock;
    private Condition administrador;
    private Condition visitante;
    private Thread asistentes[];
    private int cantEquipos;
    private int genteEsperando;
    private int entregado;

    public SnorkelGonza(int cantEquipos, int cantAsistentes) {
        this.cantEquipos = cantEquipos;
        genteEsperando = 0;
        entregado = 0;
        lock = new ReentrantLock(true);
        administrador = lock.newCondition();
        visitante = lock.newCondition();
        asistentes = new Thread[cantAsistentes];
        for (int i = 0; i < asistentes.length; i++) {
            asistentes[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                        entregarEquipos();
                }
            }, "Asistente" + i);
            asistentes[i].start();
        }
    }

    public void pedirEquipo() {
        try {
            lock.lock();
            this.genteEsperando++;
            administrador.signalAll();
            System.out.println(Thread.currentThread().getName()
                    + " esta esperando un equipo---- entregado:" + this.entregado);
            while (entregado == 0) {
                visitante.await();
            }
            entregado--;
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public void entregarEquipos() {
        try {
            lock.lock();
            System.err.println(Thread.currentThread().getName()
                    + " cantEquipos:" + cantEquipos + " genteEsperando:" + genteEsperando);
            while (cantEquipos == 0 || genteEsperando == 0) {
                administrador.await();
            }
            cantEquipos--;
            genteEsperando--;
            entregado++;
            Thread.sleep(1000);
            System.err.println(Thread.currentThread().getName()
                    + " dio un equipo, entregado:" + entregado);
            visitante.signal();
        } catch (InterruptedException ignored) {
        } finally {
            lock.unlock();
        }
    }

    public void nadar() {
        try {
            System.out.println(Thread.currentThread().getName() + " esta nadando...");
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    public void devolver() {
        lock.lock();
        cantEquipos++;
        System.out.println(Thread.currentThread().getName()
                + " devolvio un equipo, cantEquipos:" + cantEquipos);
        administrador.signalAll();
        lock.unlock();
    }
}
