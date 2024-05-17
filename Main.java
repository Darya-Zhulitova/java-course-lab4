import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Main {
    private final static int FLOOR_NUMBER = 10; // number of floors
    private final static double ELEVATOR_SPEED = 1.0; // floors per second
    private final static double STOP_TIME = 1.0; // stop duration in seconds

    public static void main(String[] args) {
        Random random = new Random();
        Queue<Request> requestQueue = new ArrayDeque<>();
        ElevatorBalancer elevatorBalancer = new ElevatorBalancer(ELEVATOR_SPEED, STOP_TIME, requestQueue);
        elevatorBalancer.start();

        try {
            while (true) {
                int r = random.nextInt(FLOOR_NUMBER - 1);
                int start, end;
                if (random.nextBoolean()) {
                    start = 1;
                    end = r + 2;
                } else {
                    start = r + 2;
                    end = 1;
                }
                synchronized (requestQueue) {
                    requestQueue.add(new Request(start, end));
                    System.out.println("New request from " + start + " to " + end + " is created");
                    requestQueue.notify();
                }
                Thread.sleep( 5000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
