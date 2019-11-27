package actividades;

import java.util.concurrent.Semaphore;

public class NadoDelfines2 implements Actividad {
    private Semaphore piletas, mutex;
    private boolean abierto;
    private int capacidadTotal;

    public NadoDelfines2(int cantPiletas, int capacidadPiletas) {
        this.abierto = false;
        this.capacidadTotal = cantPiletas * cantPiletas;
        piletas = new Semaphore(capacidadTotal);
    }

    @Override
    public void abrir() {
        try {
            mutex.acquire();
            abierto = true;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean entrar() {
        boolean entro = false;
        try {
            mutex.acquire();
            entro = abierto;
            mutex.release();
            if (entro) {
                piletas.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return entro;
    }

    public void iniciar(){
        try {
            piletas.acquire(piletas.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void terminar() {
        piletas.release(capacidadTotal);
    }

    @Override
    public void salir() {

    }

    @Override
    public void cerrar() {
        try {
            mutex.acquire();
            abierto = false;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAbierto() {
        boolean r = false;
        try {
            mutex.acquire();
            r = abierto;
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r;
    }
}
