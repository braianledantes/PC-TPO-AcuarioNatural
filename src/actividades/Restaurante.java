package actividades;

import java.util.concurrent.Semaphore;

/**
 * Restaurante: en el pago del acceso al Parque se encuentra incluido el almuerzo y la merienda.
 * Existen tres restaurantes, pero solamente se puede consumir un almuerzo y una merienda en
 * cualquiera de ellos. Puede tomar el almuerzo en un restaurante y la merienda en otro. Los restaurantes
 * tienen capacidad limitada. Las personas son atendidas en orden de llegada. Los restaurantes tienen
 * habilitada una cola de espera.
 */
public class Restaurante implements Actividad {
    boolean abierto;
    Semaphore lugares;

    public Restaurante(int capacidad) {
        lugares = new Semaphore(capacidad, true);
    }

    @Override
    public void abrir() {
        abierto = true;
    }

    @Override
    public boolean entrar(){
        boolean pudoEntrar = abierto;
        if (pudoEntrar){
            try {
                lugares.acquire();
                System.out.println(Thread.currentThread().getName() + " entro a un restaurante");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return pudoEntrar;
    }

    public void almorzar(int horas) {
        try {
            System.out.println(Thread.currentThread().getName() + " está almorzando");
            Thread.sleep(horas * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void merendar(int horas) {
        try {
            System.out.println(Thread.currentThread().getName() + " está merendando");
            Thread.sleep(horas * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void salir() {
        lugares.release();
        System.out.println(Thread.currentThread().getName() + " salio a un restaurante");
    }

    @Override
    public void cerrar() {
        abierto = false;
    }

    public boolean isAbierto(){
        return abierto;
    }
}
