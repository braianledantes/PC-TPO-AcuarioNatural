package cosas;

public class Gomon {
    private int ocupados, capacidad;

    public Gomon(int capacidad) {
        this.capacidad = capacidad;
        ocupados = 0;
    }

    public synchronized boolean subir() {
        if (ocupados < capacidad) {
            ocupados++;
            return true;
        }
        return false;
    }

    public synchronized void bajarse() {
        if (ocupados > 0) {
            ocupados--;
        }
    }
}
