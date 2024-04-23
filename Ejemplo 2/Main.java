class Dekker {
    private volatile boolean[] want = new boolean[2];
    private volatile int turn = 0;
    private volatile int count = 0;

    public void enter(int id) {
        int other = 1 - id;
        want[id] = true;
        while (want[other]) {
            if (turn == other) {
                want[id] = false;
                while (turn == other)
                    Thread.yield();
                want[id] = true;
            }
        }
    }

    public void leave(int id) {
        turn = 1 - id;
        want[id] = false;
    }
}

class Process extends Thread {
    private int id;
    private Dekker dekker;

    public Process(int id, Dekker dekker) {
        this.id = id;
        this.dekker = dekker;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            dekker.enter(id);
            // Sección crítica
            System.out.println("Proceso " + id + " está en la sección crítica");
            dekker.leave(id);
            // Sección no crítica
            System.out.println("Proceso " + id + " está en la sección no crítica");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Dekker dekker = new Dekker();
        Process p1 = new Process(0, dekker);
        Process p2 = new Process(1, dekker);
        p1.start();
        p2.start();
    }
}
