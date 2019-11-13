package hilos;

import actividades.CarreraGomones;

public class Camioneta extends Thread {
    private CarreraGomones carreraGomones;

    public Camioneta(CarreraGomones carreraGomones) {
        this.carreraGomones = carreraGomones;
    }

    @Override
    public void run() {
        while (true){
            carreraGomones.esperarQueDejenBolsos();
            carreraGomones.llevarBolsos();
            carreraGomones.esperarQueRetirenBolsos();
            carreraGomones.volver();
        }
    }
}