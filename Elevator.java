import java.util.ArrayDeque;
import java.util.Queue;

public class Elevator extends Thread {
    private final int id;
    private final int timeToFloor;
    private final int timeToStop;
    private int floor = 1;
    private final Queue<Integer> requestQueue = new ArrayDeque<>();

    public Elevator(int id, double elevatorSpeed, double stopTime) {
        super("Elevator №" + id);
        this.id = id;
        timeToFloor = (int) (1000 / elevatorSpeed);
        timeToStop = (int) (stopTime * 1000);
    }

    public int getFloor() {
        return floor;
    }

    public int getDirection(int floor, int nextFloor) {
        if (nextFloor > floor) {
            return 1;
        } else {
            return -1;
        }
    }

    public boolean isWaiting() {
        return requestQueue.isEmpty();
    }

    public void addRequest(int floor) {
        requestQueue.add(floor);
    }

    public Queue<Integer> lock() {
        return requestQueue;
    }

    @Override
    public String toString() {
        return "Elevator №" + id;
    }


    @Override
    public void run() {
        System.out.println(this + " is running");
        try {
            while (true) {
                synchronized (requestQueue) {
                    if (!requestQueue.isEmpty()) {
                        int nextFloor = requestQueue.remove();
                        while (floor != nextFloor) {
                            floor += getDirection(floor, nextFloor);
                            Thread.sleep(timeToFloor);
                            System.out.println(this + " is on floor " + floor);
                        }
                        System.out.println(this + " open doors on floor " + floor);
                        Thread.sleep(timeToStop);
                        System.out.println(this + " close doors on floor " + floor);
                    } else {
                        requestQueue.wait();
                    }
                }
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}