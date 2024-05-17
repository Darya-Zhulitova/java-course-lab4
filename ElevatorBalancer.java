import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class ElevatorBalancer extends Thread {
    private final List<Elevator> elevators = new ArrayList<>();
    private final Queue<Request> requestQueue;

    public ElevatorBalancer(double elevatorSpeed, double stopTime, Queue<Request> requestQueue) {
        super("ElevatorBalancer");
        this.requestQueue = requestQueue;
        for (int i = 1; i <= 3; ++i) {
            Elevator elevator = new Elevator(i, elevatorSpeed, stopTime);
            elevator.start();
            elevators.add(elevator);
        }
    }

    @Override
    public void run() {
        System.out.println("Elevator balancer is running");
        try {
            while (true) {
                Request request;
                synchronized (requestQueue) {
                    while (requestQueue.isEmpty())
                        requestQueue.wait();
                    request = requestQueue.poll();
                }
                Elevator bestElevator = getNearestElevator(request.begin());
                synchronized (bestElevator.lock()) {
                    bestElevator.addRequest(request.begin());
                    bestElevator.addRequest(request.end());
                    System.out.println(bestElevator + " gets request from " + request.begin() + " to " + request.end());
                    bestElevator.lock().notify();
                }
            }
        } catch (
                InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Elevator getNearestElevator(int floor) {
        int smallestDistance = Integer.MAX_VALUE;
        Elevator nearestElevator = null;
        while (nearestElevator == null) {
            for (Elevator elevator : elevators) {
                if (elevator.isWaiting()) {
                    if (Math.abs(floor - elevator.getFloor()) < smallestDistance) {
                        smallestDistance = Math.abs(floor - elevator.getFloor());
                        nearestElevator = elevator;
                    }
                }
            }
        }
        return nearestElevator;
    }
}

