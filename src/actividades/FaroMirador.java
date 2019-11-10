package actividades;


import hilos.Reloj;

import java.util.concurrent.Semaphore;

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
public class FaroMirador implements Actividad {
    private boolean abierto;
    private Semaphore escalera, toboganes, mirador, administrar;
    private int capacidadEscalera, cantToboganes;

    public FaroMirador(int capacidadEscalera) {
        this.abierto = false;
        this.cantToboganes = 2;
        this.capacidadEscalera = capacidadEscalera;
        escalera = new Semaphore(capacidadEscalera, true);
        toboganes = new Semaphore(cantToboganes, true);
        mirador = new Semaphore(capacidadEscalera);
        administrar = new Semaphore(0);
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public void entrar() {
        // TODO terminar metodo entrar()
        try {
            escalera.acquire();
            System.out.println(Thread.currentThread().getName() + " subiendo escalera");
            Reloj.dormirHilo(4, 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void adminarVista() {
        // TODO terminar metodo adminarVista()
        try {
            mirador.acquire();
            System.out.println(Thread.currentThread().getName() + " admirando vista desde faro");
            escalera.release();
            Reloj.dormirHilo(5, 7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void desenderPorTobogan() {
        // TODO terminar metodo desenderPorTobogan()9
        try {
            toboganes.acquire();
            System.out.println(Thread.currentThread().getName() + " desendiendo por el tobogan");
            mirador.release();
            Reloj.dormirHilo(1,2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void administrar() {
        try {
            administrar.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salir() {
        // TODO implementar metodo salir()
        toboganes.release();
        System.out.println(Thread.currentThread().getName() + " chau faromirador");
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

    private static class Admin implements Runnable {
        FaroMirador faroMirador;

        public Admin(FaroMirador faroMirador) {
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
