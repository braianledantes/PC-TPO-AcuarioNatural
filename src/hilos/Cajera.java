package hilos;

import actividades.Tienda;

public class Cajera extends Thread {
    int posCaja;
    Tienda tienda;

    public Cajera(String name, int posCaja, Tienda tienda) {
        super(name);
        this.posCaja = posCaja;
        this.tienda = tienda;
    }

    @Override
    public void run() {
        while (true){
            tienda.atender(posCaja);
        }
    }
}
