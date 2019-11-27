package actividades;

import Exceptions.NoExisteCajaException;
import hilos.Cajera;
import hilos.Reloj;

import java.util.concurrent.Semaphore;

public class Tienda implements Actividad {
    private boolean abierto;
    private Cajera[] cajeras;
    private Semaphore[] cajas;
    private Semaphore[] atender;
    private Semaphore[] pagar;

    public Tienda(int cantCajas) {
        cajas = new Semaphore[cantCajas];
        for (int i = 0; i < cajas.length; i++) {
            cajas[i] = new Semaphore(1, true);
        }
        atender = new Semaphore[cantCajas];
        for (int i = 0; i < atender.length; i++) {
            atender[i] = new Semaphore(0, true);
        }
        pagar = new Semaphore[cantCajas];
        for (int i = 0; i < atender.length; i++) {
            pagar[i] = new Semaphore(0, true);
        }
        cajeras = new Cajera[cantCajas];
        for (int i = 0; i < cajeras.length; i++) {
            cajeras[i] = new Cajera("C" + i, i, this);
            cajeras[i].start();
        }
    }

    @Override
    public synchronized void abrir() {
        abierto = true;
    }

    @Override
    public synchronized boolean entrar() {
        if (abierto)
            System.out.println(Thread.currentThread().getName() + " entro a la tienda");
        return abierto;
    }

    public void comprar() {
        System.out.println(Thread.currentThread().getName() + " comprando...");
        try {
            Thread.sleep(Reloj.DURACION_HORA / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pagar(int caja) throws NoExisteCajaException {
        if (caja < 0 || caja >= cajas.length) throw new NoExisteCajaException(caja);
        try {
            System.out.println(Thread.currentThread().getName() + " esperando para pagar en caja:" + caja + "...");
            cajas[caja].acquire();
            atender[caja].release();
            pagar[caja].acquire();
            System.out.println(Thread.currentThread().getName() + " pagando en caja:" + caja + "...");
            Thread.sleep(Reloj.DURACION_HORA / 8);
            cajas[caja].release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCantCajas() {
        return cajeras.length;
    }

    public void atender(int caja) throws NoExisteCajaException {
        if (caja < 0 || caja >= cajas.length) throw new NoExisteCajaException(caja);
        try {
            atender[caja].acquire();
            System.out.println(Thread.currentThread().getName() + " atendiendo por caja:" + caja + "...");
            Thread.sleep(Reloj.DURACION_HORA / 6);
            pagar[caja].release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salir() {
        System.out.println(Thread.currentThread().getName() + " salio de tienda...");
    }

    @Override
    public synchronized void cerrar() {
        abierto = false;
    }

    @Override
    public synchronized boolean isAbierto() {
        return abierto;
    }
}
