import actividades.Restaurante;

import java.util.Random;

public class Visitante implements Runnable {
    private Parque parque;
    int cualRest;
    private Random random;

    public Visitante(Parque parque) {
        this.parque = parque;
        this.random = new Random();
        this.cualRest = random.nextInt(Parque.CANT_RESTUARANTES);
    }

    @Override
    public void run() {
        int[] recorrido = new int[5];
        while (true) {
            if (parque.entrar()) {
                almorzar();
                siguienteRestaurante();
                merendar();
                parque.salir();
            }
            hacerOtraCosa();
        }
    }

    public void almorzar() {
        Restaurante restaurante = parque.entrarAlRestaurante(cualRest);
        restaurante.almorzar(2);
    }

    public void siguienteRestaurante() {
        cualRest = (cualRest + 1) % Parque.CANT_RESTUARANTES;
    }

    public void merendar() {
        Restaurante restaurante = parque.entrarAlRestaurante(cualRest);
        restaurante.merendar(1);
    }

    public void hacerOtraCosa() {
        Reloj.dormirHilo(3, 5);
        System.out.println(Thread.currentThread().getName() + " haciendo otra cosa");
    }
}
