class DekkerAlgorithm {
    private volatile boolean[] flag = new boolean[2];
    private volatile int turn = 0;

    public void lock(int processId) {
        int otherProcessId = 1 - processId;
        flag[processId] = true;
        while (flag[otherProcessId]) {
            if (turn == otherProcessId) {
                flag[processId] = false;
                while (turn == otherProcessId) {
                    // Espera ocupada
                }
                flag[processId] = true;
            }
        }
    }

    public void unlock(int processId) {
        flag[processId] = false;
        turn = 1 - processId;
    }
}

class Process extends Thread {
    private final DekkerAlgorithm dekker;
    private final int id;
    private final int iterations;
    private volatile boolean running = true;

    public Process(DekkerAlgorithm dekker, int id, int iterations) {
        this.dekker = dekker;
        this.id = id;
        this.iterations = iterations;
    }

    public void stopThread() {
        running = false;
    }

    public void run() {
        int count = 0;
        while (running && count < iterations) {
            dekker.lock(id);
            // Sección crítica
            System.out.println("Proceso " + id + ": Entrando en la sección crítica");
            try {
                Thread.sleep(7000); // Simulación de una operación en la sección crítica
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Proceso " + id + ": Saliendo de la sección crítica");
            // Fin de la sección crítica
            dekker.unlock(id);
            // Sección no crítica
            try {
                Thread.sleep(7000); // Simulación de una operación fuera de la sección crítica
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Fin de la sección no crítica
            count++;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        DekkerAlgorithm dekker = new DekkerAlgorithm();
        int iterations = 3; // Cambia este valor al número deseado de iteraciones
        Process p0 = new Process(dekker, 0, iterations);
        Process p1 = new Process(dekker, 1, iterations);
        p0.start();
        p1.start();
        try {
            // Espera a que ambos hilos terminen
            p0.join();
            p1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Programa finalizado.");
    }
}

