package hilos;

import actividades.*;
import cosas.Gomon;
import cosas.Pileta;
import parque.Parque;

import java.util.ArrayList;
import java.util.Random;

public class Visitante implements Runnable {
    private Parque parque;
    private int cualRest;
    private Random random;
    private ArrayList<Integer> recorrido;


    public Visitante(Parque parque) {
        this.parque = parque;
        this.random = new Random();
        this.cualRest = random.nextInt(Parque.CANT_RESTUARANTES - 1);
        recorrido = new ArrayList<>(6);
    }

    @Override
    public void run() {
        while (true) {
            if (parque.isAbierto()) {
                irAlParque();
                parque.entrar();
                if (random.nextBoolean()) {
                    visitarActividades();
                } else {
                    visitarTienda();
                }
                parque.salir();
            }
            volverDespues();
        }
    }

    private void establecerRecorrido() {
        recorrido.clear();
        int valor;
        int cantActividades = Parque.CANTIDAD_ACTIDIDADES + 1;
        for (int i = 0; i < cantActividades; i++) {
            do {
                valor = 1 + random.nextInt(cantActividades);
            } while (recorrido.contains(valor));
            recorrido.add(valor);
        }
       // System.out.println(recorrido);
    }

    /**
     * Visita las actividades según el recorrido establecido.
     */
    public void visitarActividades() {
        establecerRecorrido();
        for (int r : recorrido) {
            switch (r) {
                case 1:
                    almorzar();
                    siguienteRestaurante();
                    break;
                case 2:
                    merendar();
                    siguienteRestaurante();
                    break;
                case 3:
                    visitarFaroMirador();
                    break;
                case 4:
                    visitarCarreraGomones();
                    break;
                case 5:
                    visitarSnorkel();
                    break;
                case 6:
                    vistarNadoDelfines();
                    break;
            }
        }
    }

    /**
     * Si no puede ir al parque en conlectivo para llegar más rápido, va a ir en auto.
     */
    public void irAlParque() {
        if (!parque.tomarCole()) {
            tomarAuto();
        }
    }

    public  void visitarTienda() {
        Tienda tienda = parque.getTienda();
        if (tienda != null && tienda.entrar()) {
            tienda.comprar();
            tienda.pagar(random.nextInt(tienda.getCantCajas()));
            tienda.salir();
        }
    }

    public void vistarNadoDelfines() {
        NadoDelfines nadoDelfines = parque.getNadoDelfines();
        if (nadoDelfines != null && nadoDelfines.entrar()) {
            Pileta pileta = nadoDelfines.entrarAUnaPileta();
            if (pileta != null) {
                pileta.esperarAQueInicie();
                pileta.nadarConDelfines();
                pileta.salir();
            }
            nadoDelfines.salir();
        }
    }

    public void visitarSnorkel() {
        Snorkel snorkel = parque.getSnorkel();
        if (snorkel != null && snorkel.entrar()) {
            snorkel.adquirirEquipo();
            snorkel.nadar();
            snorkel.salir();
        }
    }

    public void visitarCarreraGomones() {
        CarreraGomones carreraGomones = parque.getCarreraGomones();
        if (carreraGomones != null) {
            if (carreraGomones.entrar()) {
                carreraGomones.irAlInicio();
                carreraGomones.esperarEnPrecompetencia();
                carreraGomones.dejarBolso();
                Gomon gomon = carreraGomones.subirseAGomon();
                carreraGomones.competir();
                carreraGomones.terminarCarrera(gomon);
                carreraGomones.retirarBolso();
                carreraGomones.salir();
            }
        }
    }

    public void visitarFaroMirador() {
        FaroMirador faroMirador = parque.getFaroMirador();
        if (faroMirador != null && faroMirador.entrar()) {
            faroMirador.admirarVista();
            faroMirador.desenderPorTobogan();
            faroMirador.salir();
        }
    }

    public void almorzar() {
        Restaurante restaurante = parque.getRestaurante(cualRest);
        if (restaurante != null && restaurante.entrar()) {
            restaurante.almorzar(2);
            restaurante.salir();
        }
    }

    public void siguienteRestaurante() {
        cualRest = (cualRest + 1) % Parque.CANT_RESTUARANTES;
    }

    public void merendar() {
        Restaurante restaurante = parque.getRestaurante(cualRest);
        if (restaurante != null && restaurante.entrar()) {
            restaurante.merendar(1);
            restaurante.salir();
        }
    }

    public void tomarAuto() {
        System.out.println(Thread.currentThread().getName() + " yendo en auto al parque...");
        Reloj.dormirHilo(Reloj.DURACION_HORA, Reloj.DURACION_HORA * 3 / 2);
        System.out.println(Thread.currentThread().getName() + " llegó en auto al parque");
    }

    /**
     * El visitante hace otra cosa hasta que el parque esté abierto.
     */
    public void volverDespues() {
        int horaSalida = Reloj.getHora();
        int horaEspera;
        if (horaSalida < Parque.HORA_INICIO_INGRESO) {
            horaEspera = Parque.HORA_INICIO_INGRESO - horaSalida;
        } else {
            horaEspera = 24 - horaSalida + Parque.HORA_INICIO_INGRESO;
        }
        try {
            //System.out.println(Thread.currentThread().getName() + " haciendo otra cosa por " + horaEspera + "hs");
            Thread.sleep(horaEspera * Reloj.DURACION_HORA);
        } catch (InterruptedException ignored) {
        }
    }
}
