package actividades;

import hilos.AdminFaroMirador;
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
    private Semaphore escalera;
    private Semaphore tobogan1, tobogan2;
    private int capacidadEscalera;
    private Thread admin;

    public FaroMirador(int capacidadEscalera) {
        this.abierto = false;
        this.capacidadEscalera = capacidadEscalera;
        escalera = new Semaphore(capacidadEscalera, true);
        tobogan1 = new Semaphore(1);
        tobogan1 = new Semaphore(1);
        admin = new Thread(new AdminFaroMirador(tobogan1, tobogan2), "AdminFaroFirador");
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public boolean entrar() {
        // TODO terminar metodo entrar()
        try {
            escalera.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void subir() {
        // TODO terminar metodo subir()
        try {
            escalera.acquire();
            System.out.println(Thread.currentThread().getName() + " subiendo escalera");
            Reloj.dormirHilo(2,5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void adminarVista() {
        // TODO terminar metodo adminarVista()
        System.out.println(Thread.currentThread().getName() + " admirando vista desde faro");
        Reloj.dormirHilo(2,5);
    }

    public void desenderPorTobogan() {
        // TODO terminar metodo desenderPorTobogan()
        escalera.release();
    }

    public void avisarAdmin() {
        // TODO implementar metodo avisarAdmin()
    }

    @Override
    public void salir() {
        // TODO implementar metodo salir()
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
}
