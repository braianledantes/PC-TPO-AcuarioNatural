package hilos;

import actividades.CarreraGomones;
import actividades.FaroMiradorLocks;
import actividades.Restaurante;
import cosas.Bolso;
import parque.Parque;

import java.util.Random;

public class Visitante implements Runnable {
    private Parque parque;
    int cualRest;
    private Random random;
    private Bolso bolso;
    private int id;

    public Visitante(int id, Parque parque) {
        this.id = id;
        this.parque = parque;
        this.random = new Random();
        this.cualRest = random.nextInt(Parque.CANT_RESTUARANTES - 1);
        this.bolso = new Bolso("Bolso" + id);
    }

    @Override
    public void run() {
        int[] recorrido = new int[5];
        while (true) {
            if (parque.isAbierto()) {
                parque.entrar();
                //almorzar();
                //siguienteRestaurante();
                //merendar();
                //visitarFaroMirador();
                visitarCarreraGomones();
                parque.salir();
            }
            volverAlOtroDia();
        }
    }

    void visitarCarreraGomones() {
        CarreraGomones carreraGomones = parque.entrarCarreraGomones();
        if (carreraGomones != null) {
            carreraGomones.irAlInicio();
            carreraGomones.dejarBolso();
            carreraGomones.bajarEnGomones();
        }
    }

    void visitarFaroMirador() {
        FaroMiradorLocks faroMirador = parque.entrarAlFaroMirador();
        if (faroMirador != null) {
            faroMirador.entrar();
            faroMirador.admirarVista();
            faroMirador.desenderPorTobogan();
            faroMirador.salir();
        }
    }

    void establecerRecorrido() {
        // TODO implementar metodo establecerRecorrido()
    }

    void almorzar() {
        Restaurante restaurante = parque.entrarAlRestaurante(cualRest);
        if (restaurante != null) {
            restaurante.entrar();
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

    void volverAlOtroDia() {
        int horaSalida = Reloj.getHora();
        int tiempoEspera = 24 - horaSalida + Parque.HORA_INICIO_INGRESO;
        if (horaSalida < Parque.HORA_INICIO_INGRESO) {
            tiempoEspera = Parque.HORA_INICIO_INGRESO - horaSalida;
        }
        //System.out.println(Thread.currentThread().getName() + " haciendo otra cosa " + horaSalida + " - " + tiempoEspera);
        try {
            Thread.sleep(tiempoEspera * 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
