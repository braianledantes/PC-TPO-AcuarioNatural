import hilos.Reloj;
import hilos.Visitante;
import parque.Parque;

public class Main {
    public static void main(String[] args) {
        int molinetes = 4, nVisitantes = 70;
        Parque parque = new Parque(molinetes);
        Reloj.getInstance(parque).start(); // inicio el reloj
        Visitante[] visitantes = new Visitante[nVisitantes];

        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Visitante("V" + i, parque);
            visitantes[i].start();
        }
    }
}
