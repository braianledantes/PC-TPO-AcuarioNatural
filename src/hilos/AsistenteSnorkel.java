package hilos;

import actividades.Snorkel;

public class AsistenteSnorkel implements Runnable {
    private Snorkel snorkel;

    public AsistenteSnorkel(Snorkel snorkel) {
        this.snorkel = snorkel;
    }

    @Override
    public void run() {
        while (true) {
            snorkel.entregarEquipo();
        }
    }
}
