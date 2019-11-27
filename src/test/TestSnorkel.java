package test;

public class TestSnorkel {

    public static void main(String[] args) {

        SnorkelGonza snorkel =
                new SnorkelGonza(10, 2);
        Thread[] visitantes = new Thread[15];
        for (int i = 0; i < visitantes.length; i++) {
            visitantes[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    snorkel.pedirEquipo();
                    snorkel.nadar();
                    snorkel.devolver();
                }
            }, "vis" + i);
            visitantes[i].start();
        }
    }
}

