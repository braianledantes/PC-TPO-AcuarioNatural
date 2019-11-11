package actividades;

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
    private int capacidadEscalera, capacidadCima, quieren_tirarse, queTobogan, subiendo, mirando;
    private boolean disponibleToboganA, disponibleToboganB;
    private Lock lock;
    private Condition subir, mirar, tirarse, administrar, toboganA, toboganB;

    public FaroMiradorLocks(int capacidadEscalera) {
        this.abierto = false;
        this.capacidadEscalera = capacidadEscalera;
        capacidadCima = capacidadEscalera + 10;
        queTobogan = 0;
        quieren_tirarse = 0;
        subiendo = 0;
        mirando = 0;
        lock = new ReentrantLock(true);
        subir = lock.newCondition();
        mirar = lock.newCondition();
        tirarse = lock.newCondition();
        administrar = lock.newCondition();
        toboganA = lock.newCondition();
        toboganB = lock.newCondition();

        disponibleToboganA = true;
        disponibleToboganB = true;
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public void entrar() throws InterruptedException {
        lock.lock();
        while (subiendo == capacidadEscalera) {
            subir.await();
        }
        subiendo++;
        lock.unlock();

        System.out.println(Thread.currentThread().getName() + " subiendo escalera");
        Thread.sleep(1000);
    }

    public void admirarVista() throws InterruptedException {
        lock.lock();
        while (mirando == capacidadCima) {
            mirar.await();
        }
        mirando++;
        subiendo--;
        subir.signal();
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + " admirando vista");
        Thread.sleep(2000);
    }

    public void desenderPorTobogan() throws InterruptedException {
        int cualTobogan;
        lock.lock();
        quieren_tirarse++;
        administrar.signal();
        System.out.println(Thread.currentThread().getName() + " quiere tirarse");
        while (queTobogan == 0) {
            tirarse.await();
        }
        quieren_tirarse--;
        mirando--;
        mirar.signal();
        cualTobogan = queTobogan;
        queTobogan = 0;
        switch (cualTobogan) {
            case 1:
                while (!disponibleToboganA) {
                    toboganA.await();
                }
                disponibleToboganA = false;
                break;
            case 2:
                while (!disponibleToboganB) {
                    toboganB.await();
                }
                disponibleToboganB = false;
                break;
        }
        lock.unlock();

        System.out.println(Thread.currentThread().getName() + " desendiendo por tobogan " + cualTobogan);
        Thread.sleep(1000);

        lock.lock();
        if (cualTobogan == 1) {
            disponibleToboganA = true;
        } else if (cualTobogan == 2) {
            disponibleToboganB = true;
        }
        administrar.signal();
        lock.unlock();
    }

    public void administrar() throws InterruptedException {
        lock.lock();
        while (quieren_tirarse == 0 || (!disponibleToboganA && !disponibleToboganB)){
            administrar.await();
        }
        if (disponibleToboganA){
            queTobogan = 1;
        } else if (disponibleToboganB){
            queTobogan = 2;
        }
        tirarse.signal();
        lock.unlock();
    }

    @Override
    public void salir() {
        System.out.println(Thread.currentThread().getName() + " salio");
    }

    @Override
    public void cerrar() {
        abierto = false;
    }

    @Override
    public boolean isAbierto() {
        return abierto;
    }
}
