package actividades;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * TODO implementar clase
 * Carrera de gomones por el río : esta actividad permite que los visitantes deciendan por el río, que se
 * encuentra rodeado de manglares, compitiendo entre ellos. Para ello es necesario llegar hasta el inicio
 * de la actividad a través de bicicletas que se prestan en un stand de bicicletas, o a través de un tren
 * interno que tiene una capacidad de 15 personas como máximo. Al llegar al inicio del recorrido cada
 * persona dispondrá de un bolso con llave, en donde podrá guardar todas las pertenencias que no quiera
 * mojar. Los bolsos están identificados con un número al igual que la llave, los bolsos serán llevados en
 * una camioneta, hasta el final del recorrido en donde podrán ser retirados por el visitante. Para bajar se
 * utilizan gomones, individuales o con capacidad para 2 personas. La cantidad de gomones de cada tipo
 * es limitada. Para habilitar una largada es necesario que haya h gomones listos para salir, no importa el
 * tipo.
 */
public class CarreraGomones implements Actividad {
    private int participantes;
    private HashMap<Integer, String> bolsos;
    private Tren tren;
    private Gomon[] gomones;
    private boolean abierto;

    public CarreraGomones(int participantes) {
        this.participantes = participantes;
        bolsos = new HashMap<>();
        this.gomones = new Gomon[participantes];
        this.tren = new Tren("Tren de la alegria");
        this.tren.start();
        this.abierto = false;
    }

    @Override
    public void abrir() {
        // TODO implementar metodo abrir()
        abierto = true;
    }


    @Override
    public void entrar() {
        // TODO implementar metodo entrar()
    }

    public void irAlInicio() {
        /*
         * llegar hasta el inicio de la actividad a través de bicicletas que se prestan en un stand de bicicletas,
         * o a través de un tren
         * interno que tiene una capacidad de 15 personas como máximo.
         */
        if (!tren.isSalioTren()) {
            tren.subirse();
            tren.bajarse();
        } else {
            irEnBici();
        }

    }

    private void irEnBici() {
        try {
            System.out.println(Thread.currentThread().getName() + " en bicicleta");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dejarBolso() {

        /*
         * Al llegar al inicio del recorrido cada
         * persona dispondrá de un bolso con llave, en donde podrá guardar todas las pertenencias que no quiera
         * mojar. Los bolsos están identificados con un número al igual que la llave, los bolsos serán llevados en
         * una camioneta, hasta el final del recorrido en donde podrán ser retirados por el visitante.
         */
    }

    public void bajarEnGomones() {
        /*
         * Para bajar se
         * utilizan gomones, individuales o con capacidad para 2 personas. La cantidad de gomones de cada tipo
         * es limitada. Para habilitar una largada es necesario que haya h gomones listos para salir, no importa el
         * tipo.
         */
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
        return false;
    }
}

class Gomon {
    private int capacidad;

    private Gomon(int capacidad) {
        this.capacidad = capacidad;
    }

    public static Gomon newGomonIndividual() {
        return new Gomon(1);
    }

    public static Gomon newGomonDuo() {
        return new Gomon(2);
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}

class Camioneta extends Thread {

    @Override
    public void run() {

    }
}

class Tren extends Thread {
    public static final int CAPACIDAD = 15;
    private boolean salioTren;
    private int cantAct;
    CountDownLatch latch;
    Semaphore bajarse, arrancarTren, mutex;

    public Tren(String name) {
        super(name);
        salioTren = false;
        cantAct = 0;
        bajarse = new Semaphore(0);
        arrancarTren = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    public boolean isSalioTren() {
        return salioTren;
    }

    @Override
    public void run() {
        while (true) {
            esperarQueSuban();
            llevarPasajeros();
            dejarPasajeros();
            volver();
        }
    }

    public void esperarQueSuban() {
        try {
            // se crea una nueva espera
            latch = new CountDownLatch(CAPACIDAD);
            arrancarTren.acquire(); // se traba hasta que llegue el primer pasajero
            latch.await(5, TimeUnit.SECONDS);
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
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + " se subio al tren");
            bajarse.acquire(); // se traba para que no haga nada durante el viaje
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void llevarPasajeros() {
        try {
            // para que no se suba nadie mas
            mutex.acquire();
            salioTren = true;
            System.out.println("tren llevando " + cantAct + " pasajeros chuu chuuu");
            mutex.release();

            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dejarPasajeros() {
        try {
            mutex.acquire();
            salioTren = false;
            bajarse.release(cantAct); // libero los pasajeros para que se bajen
            System.out.println("El tren dejó los pasajeros");
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bajarse() {
        try {
            mutex.acquire();
            cantAct--;
            System.out.println(Thread.currentThread().getName() + " se bajo del tren");
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void volver() {

    }
}