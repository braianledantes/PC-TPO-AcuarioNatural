package actividades;

import cosas.Gomon;
import hilos.Camioneta;
import hilos.Reloj;
import hilos.Transporte;
import hilos.Visitante;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
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
    private Transporte tren;
    private Gomon[] gomones;
    private boolean abierto, camionetaFin, camionetaInicio, compitiendo;
    private CyclicBarrier precompetencia, largada;
    private Gomon gomonGanador;
    private int enCompetencia, cantMaxCompetidores, enPrecompetencia;

    public CarreraGomones(int cantGomonesIndividuales, int cantGomonesCompartidos, int capacidadTren) {
        this.gomones = new Gomon[cantGomonesIndividuales + cantGomonesCompartidos];
        this.cantMaxCompetidores = cantGomonesIndividuales + (cantGomonesCompartidos * 2);
        this.tren = new Transporte("Tren", capacidadTren, Reloj.DURACION_HORA);
        this.abierto = false;

        this.precompetencia = new CyclicBarrier(cantMaxCompetidores);
        Camioneta camioneta = new Camioneta(this);

        this.gomonGanador = null;
        this.camionetaInicio = true;
        this.camionetaFin = false;
        this.enCompetencia = 0;
        this.enPrecompetencia = 0;
        this.compitiendo = false;

        // creo los gomones individuales
        for (int i = 0; i < cantGomonesIndividuales; i++) {
            gomones[i] = new Gomon(1);
        }
        // creo los gomones compartidos
        for (int i = cantGomonesIndividuales; i < gomones.length; i++) {
            gomones[i] = new Gomon(2);
        }

        this.tren.start();
        camioneta.start();
    }

    @Override
    public synchronized void abrir() {
        tren.abrir();
        abierto = true;
        System.out.println("Carrera de gomones abierto");
    }

    /**
     * Para entrar en el parque.
     * @return true si es posivle entrar y falso de lo contrario
     */
    @Override
    public synchronized boolean entrar() {
        return abierto;
    }

    /**
     * El competidor trata de ir al inicio de la carrera en tren y si no puede va en bicicleta.
     */
    public void irAlInicio() {
        if (tren.subirse()) {
            tren.bajarse();
        } else {
            irEnBici();
        }
    }

    private void irEnBici() {
        System.out.println(Thread.currentThread().getName() + " en bicicleta...");
        Reloj.dormirHilo(Reloj.DURACION_HORA / 2, Reloj.DURACION_HORA);
    }

    /**
     * El competidor espera en precompetencia mientras haya gente compitiendo.
     */
    public void esperarEnPrecompetencia() {
        try {
            synchronized (this) {
                while (compitiendo || enPrecompetencia == cantMaxCompetidores) {
                    wait();
                }
                enPrecompetencia++;
            }
            precompetencia.await(2 * Reloj.DURACION_HORA, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | BrokenBarrierException ignored) {
        } catch (TimeoutException e) {
            precompetencia.reset();
        } finally {
            synchronized (this) {
                if (largada == null) {
                    largada = new CyclicBarrier(enPrecompetencia);
                    // se van todos de precompetencia
                    enPrecompetencia = 0;
                    compitiendo = true;
                    notifyAll();
                }
            }
        }
    }

    /**
     * Método utilizado por la camioneta.
     * Espera a que todos los competidores dejen los bolsos.
     */
    public synchronized void esperarQueDejenBolsos() {
        System.out.println("Camioneta en inicio");
        compitiendo = false;
        notifyAll();
        while (camionetaInicio) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * El competidor espera a que esté la camioneta disponible para poder dejar su bolso allí.
     */
    public synchronized void dejarBolso() {
        // TODO ver la manera de que lleguen los dos juntos a la meta cuando el gomón es compartido
        while (!camionetaInicio) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(Reloj.DURACION_MIN * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " dejó su bolso");
    }

    /**
     * El competidor se sube a un gomón que esté disponible, no importa si es indiviual o compartido.
     * @return retorna el gomon al que se subio el visitante
     */
    public Gomon subirseAGomon(Visitante visitante) {
        int i = 0;
        while (i < gomones.length && !gomones[i].subir(visitante)) {
            i++;
        }
        try {
            Thread.sleep(Reloj.DURACION_MIN * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " se subio al gomon");
        return gomones[i];
    }

    /**
     * Cuando esten todos o no venga nadie mas se larga la carrera y el primero en salir libera la camioneta
     * para que lleve los bolsos.
     * @param gomon
     */
    public void competir(Gomon gomon) {
        try {
            //System.out.println(Thread.currentThread().getName() + " esperando para competir... ");
            largada.await();
        } catch (InterruptedException | BrokenBarrierException ignored) {
        }
        synchronized (this) {
            // si es el primero en salir, la camioneta lleva los bolsos
            enCompetencia++;
            if (enCompetencia == 1) {
                camionetaInicio = false;
                largada = null;
            }
            notifyAll();
        }
        gomon.correr();
    }

    /**
     * Método utilizado por la camioneta.
     * Cuando todos dejan los bolsos en la camioneta se larga la carrera y la camineta deja los bolsos en el final.
     */
    public void llevarBolsos() {
        System.out.println("Camioneta llevando los bolsos...");
        Reloj.dormirHilo(Reloj.DURACION_HORA, Reloj.DURACION_HORA * 3 / 2);
        synchronized (this) {
            camionetaFin = true;
            notifyAll();
        }
    }

    /**
     * Si el gomón enviado por parámetro es el ganador, lo notifica.
     * Si es el último competidor en llegar a la meta resetea el gomón ganador.
     *
     * @param gomon gomon con el cual se corrió la carrera
     */
    public synchronized void terminarCarrera(Gomon gomon) {
        // si es el primero guarda el gomon ganador
        if (gomonGanador == null) {
            gomonGanador = gomon;
        }
        gomon.llegoAlFinal();
        if (gomonGanador == gomon) {
            gomon.gano();
        }
        gomon.bajarse();
        enCompetencia--;
        // si es el último en llegar resetea el gomon ganador
        if (enCompetencia == 0) {
            gomonGanador = null;
        }
        notifyAll();
    }

    /**
     * El competidor espera a que esté la camioneta en la meta para poder retirar su bolso,
     * y si es el último libera a la camioneta para que vaya al inicio de la carrera.
     */
    public synchronized void retirarBolso() {
        while (!camionetaFin) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (enCompetencia == 0) {
            camionetaFin = false;
            notifyAll();
        }
        System.out.println(Thread.currentThread().getName() + " retiró su bolso");
    }

    /**
     * Método utilizado por la camioneta.
     * Espera a que los competidores retiren sus bolsos.
     */
    public synchronized void esperarQueRetirenBolsos() {
        while (camionetaFin) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método utilizado por la camioneta.
     * Vuelve al punto de partida de la carrera para que los competidores dejen sus bolsos.
     */
    public void volver() {
        System.out.println("Camioneta volviendo...");
        Reloj.dormirHilo(Reloj.DURACION_HORA, Reloj.DURACION_HORA * 3 / 2);
        synchronized (this) {
            // les avisa que pueden dejar sus bolsos
            camionetaInicio = true;
            notifyAll();
        }
    }

    @Override
    public synchronized void salir() {
        System.out.println(Thread.currentThread().getName() + " salió de la carrera de gomones");
    }

    @Override
    public synchronized void cerrar() {
        tren.cerrar();
        abierto = false;
    }

    @Override
    public synchronized boolean isAbierto() {
        return abierto;
    }
}


