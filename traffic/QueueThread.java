package traffic;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class QueueThread extends Thread {
    private int elapsedTime;
    private final int roads;
    private final int interval;
    private ProgramState programState;
    private final LinkedList<Road> queue;

    public QueueThread(String name, int roads, int interval) {
        super(name);
        this.elapsedTime = 0;
        this.roads = roads;
        this.interval = interval;
        this.programState = ProgramState.NOT_STARTED;
        this.queue = new LinkedList<>();
    }

    public void setProgramState(ProgramState programState) {
        this.programState = programState;
    }

    public LinkedList<Road> getQueue() {
        return this.queue;
    }

    private void recalculateTime(int firstRoadTime) {
        for (Road road : this.queue) {
            if (!road.isOpen()) {
                road.setRemainingTime((road.getIndex() - 1) * this.interval + firstRoadTime);
            }
        }
    }

    public void addRoad(Road road) {
        if (!this.queue.isEmpty()) {
            this.queue.add(road);
            this.recalculateTime(this.queue.getFirst().getRemainingTime());
        } else {
            this.queue.add(road);
            road.setOpen(true);
        }
    }

    private Road getFirstRoadByIndex() {
        Road firstRoad = this.queue.getFirst();
        for (Road road : this.queue) {
            if (road.getIndex() < firstRoad.getIndex()) {
                firstRoad = road;
            }
        }
        return firstRoad;
    }

    private Road getOpenRoad() {
        Road openedRoad = null;
        for (Road road : this.queue) {
            if (road.isOpen()) {
                openedRoad = road;
                break;
            }
        }
        return openedRoad;
    }

    public void removeRoad() {
        if (!this.queue.isEmpty()) {
            Road firstRoad = this.getFirstRoadByIndex();
            this.queue.remove(firstRoad);
            for (Road road : this.queue) {
                road.setIndex(road.getIndex() - 1);
            }
            if (!this.queue.isEmpty()) {
                if (firstRoad.isOpen()) {
                    Road secondRoad = this.getFirstRoadByIndex();
                    secondRoad.setOpen(true);
                    secondRoad.setRemainingTime(firstRoad.getRemainingTime());
                    this.recalculateTime(secondRoad.getRemainingTime());
                } else {
                    Road openRoad = this.getOpenRoad();
                    if (openRoad != null) {
                        this.recalculateTime(openRoad.getRemainingTime());
                    }
                }
            }
            System.out.printf("%s deleted!\n", firstRoad.getName());
        }
    }

    public void updateRoads() {
        if (!this.queue.isEmpty()) {
            for (Road road : this.queue) {
                road.setRemainingTime(road.getRemainingTime() - 1);
            }
            if (this.queue.getFirst().getRemainingTime() <= 0) {
                Road firstRoad = this.queue.removeFirst();
                if (!this.queue.isEmpty()) {
                    Road secondRoad = this.queue.getFirst();
                    secondRoad.setRemainingTime(this.interval);
                    secondRoad.setOpen(true);
                    this.queue.add(firstRoad);
                    firstRoad.setRemainingTime((this.queue.size() - 2) * this.interval + this.interval);
                    firstRoad.setOpen(false);
                } else {
                    firstRoad.setRemainingTime(this.interval);
                    this.queue.add(firstRoad);
                }
            }
        }
    }

    @Override
    public void run() {
        while (this.programState != ProgramState.EXITED) {
            try {
                Thread.sleep(1000L);
                this.elapsedTime++;
                if (this.programState == ProgramState.SYSTEM) {
                    UserInterface.clearConsole();
                    System.out.printf("""
                       ! %ds. have passed since system startup !
                       ! Number of roads: %d !
                       ! Interval: %d !
                       """, this.elapsedTime, this.roads, this.interval);
                    if (!this.queue.isEmpty()) {
                        System.out.println();
                        List<Road> roads = this.queue.stream().sorted(Comparator.comparingInt(Road::getIndex)).toList();
                        for (Road road : roads) {
                            System.out.printf(
                                "%sRoad \"%s\" will be %s for %ds.\u001B[0m\n",
                                road.isOpen() ? "\u001B[32m" : "\u001B[31m",
                                road.getName(),
                                road.isOpen() ? "open" : "closed",
                                road.getRemainingTime()
                            );
                        }
                        System.out.println();
                    }
                    System.out.println("! Press \"Enter\" to open menu !");
                }
                this.updateRoads();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
