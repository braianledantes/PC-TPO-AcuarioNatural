package hilos;

import actividades.CarreraGomones;
import actividades.FaroMirador;
import actividades.Restaurante;
import actividades.Snorkel;
import cosas.Gomon;
import parque.Parque;

import java.util.Random;

public class Visitante implements Runnable {
    private Parque parque;
    private int cualRest;
    private Gomon gomon;
    private Random random;
    private int[] recorrido;

    public Visitante(Parque parque) {
        this.parque = parque;
        this.random = new Random();
        this.cualRest = random.nextInt(Parque.CANT_RESTUARANTES - 1);
        recorrido = new int[4];
    }

    public Gomon getGomon() {
        return gomon;
    }

    public void setGomon(Gomon gomon) {
        this.gomon = gomon;
    }

    @Override
    public void run() {
        //int i = 0;
        while (true) {
            if (parque.isAbierto()) {
                //irAlParque();
                parque.entrar();
                //visitarFaroMirador();
                //almorzar();
                //siguienteRestaurante();
                //merendar();
                //visitarCarreraGomones();
                visitarSnorkel();
                parque.salir();
            }
            //i++;
            volverDespues();
        }
    }

    /**
     * Si no puede ir al parque en conlectivo para llegar más rápido, va a ir en auto
     */
    void irAlParque() {
        if (!parque.tomarCole()) {
            tomarAuto();
        }
    }

    void visitarSnorkel() {
        Snorkel snorkel = parque.entrarSnorkel();
        if (snorkel != null && snorkel.entrar()) {
            snorkel.adquirirEquipo();
            snorkel.nadar();
            snorkel.salir();
        }
    }

    void visitarCarreraGomones() {
        CarreraGomones carreraGomones = parque.entrarCarreraGomones();
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

    void visitarFaroMirador() {
        FaroMirador faroMirador = parque.entrarAlFaroMirador();
        if (faroMirador != null && faroMirador.entrar()) {
            faroMirador.admirarVista();
            faroMirador.desenderPorTobogan();
            faroMirador.salir();
        }
    }

    void almorzar() {
        Restaurante restaurante = parque.entrarAlRestaurante(cualRest);
        if (restaurante != null && restaurante.entrar()) {
            restaurante.almorzar(2);
            restaurante.salir();
        }
    }

    void siguienteRestaurante() {
        cualRest = (cualRest + 1) % Parque.CANT_RESTUARANTES;
    }

    void merendar() {
        Restaurante restaurante = parque.entrarAlRestaurante(cualRest);
        if (restaurante != null) {
            restaurante.entrar();
            restaurante.merendar(1);
            restaurante.salir();
        }
    }

    void tomarAuto() {
        System.out.println(Thread.currentThread().getName() + " yendo en auto al parque");
        Reloj.dormirHilo(2, 3);
        System.out.println(Thread.currentThread().getName() + " llegó en auto al parque");
    }

    void volverDespues() {
        int horaSalida = Reloj.getHora();
        int horaEspera;
        if (horaSalida < Parque.HORA_INICIO_INGRESO) {
            horaEspera = Parque.HORA_INICIO_INGRESO - horaSalida;
        } else {
            horaEspera = 24 - horaSalida + Parque.HORA_INICIO_INGRESO;
        }
        //System.out.println(Thread.currentThread().getName() + " haciendo otra cosa por " + horaEspera + "hs");
        Reloj.dormirHilo(horaEspera, 0);
    }
}
