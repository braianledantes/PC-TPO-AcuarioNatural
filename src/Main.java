import hilos.Reloj;
import hilos.Visitante;
import parque.Parque;

public class Main {
    public static void main(String[] args) {
        int hCierre = 18, molinetes = 4, nVisitantes = 4;
        Parque parque = new Parque(molinetes, hCierre);
        Reloj.getInstance(parque).start(); // inicio el reloj
        Thread[] visitantes = new Thread[nVisitantes];

        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(new Visitante(parque), "Visitante" + i);
            visitantes[i].start();
        }
    }
}
