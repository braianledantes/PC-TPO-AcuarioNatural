package hilos;

import actividades.NadoDelfines;
import cosas.Pileta;

public class AdminNadoDelfines extends Thread {
    private NadoDelfines nadoDelfines;
    private Pileta[] piletas;

    public AdminNadoDelfines(String name, NadoDelfines nadoDelfines) {
        super(name);
        this.nadoDelfines = nadoDelfines;
        this.piletas = nadoDelfines.getPiletas();
    }

    @Override
    public void run() {
        while (true){
            nadoDelfines.esperarTurno();
            System.out.println(getName() + " abriendo las piletas");
            for (Pileta p : piletas) {
                p.iniciar();
            }
            try {
                Thread.sleep(nadoDelfines.getDuracionTurno());
            } catch (InterruptedException ignored) {
            }
            System.out.println(getName() + " cerrando las piletas");
            for (Pileta p : piletas) {
                p.terminar();
            }
        }
    }
}
