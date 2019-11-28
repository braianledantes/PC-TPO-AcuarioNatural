package cosas;

import hilos.Reloj;
import hilos.Visitante;

import java.util.ArrayList;

public class Gomon {
    private int cantAct, capacidad, corriendo;
    private ArrayList<Visitante> grupo;

    public Gomon(int capacidad) {
        this.capacidad = capacidad;
        grupo = new ArrayList<>(capacidad);
        corriendo = 0;
        cantAct = 0;
    }

    public synchronized boolean subir(Visitante visitante) {
        if (cantAct < capacidad) {
            grupo.add(visitante);
            cantAct++;
            return true;
        }
        return false;
    }

    public synchronized void correr() {
        corriendo++;
        System.out.print("");
        if (corriendo < grupo.size()) {
            esperarCompanero();
        } else {
            System.out.println(getNombres() + " compitiendo...");
            Reloj.dormirHilo(Reloj.DURACION_HORA, 2 * Reloj.DURACION_HORA);
        }
        corriendo--;
        notifyAll();
    }

    private synchronized void esperarCompanero() {
        while (cantAct == grupo.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void llegoAlFinal() {
        if (cantAct == grupo.size()) {
            System.out.println(getNombres() + " llegó al final");
        }
    }

    public synchronized void gano() {
        if (cantAct == grupo.size()) {
            System.out.println(getNombres() + " ganó");
        }
    }

    public synchronized void bajarse() {
        if (cantAct > 0) {
            cantAct--;
            grupo.clear();
            notifyAll();
        }
    }

    public synchronized String getNombres() {
        StringBuilder nombres = new StringBuilder();

        for (Visitante v : grupo) {
            nombres.append(v.getName()).append(" ");
        }
        return nombres.toString().strip();
    }
}
