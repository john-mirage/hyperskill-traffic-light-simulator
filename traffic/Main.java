package traffic;

public class Main {
    public static void main(String[] args){
        System.out.println("Welcome to the traffic management system!");
        final int roads = UserInterface.askForNumberOfRoads();
        final int interval = UserInterface.askForNumberOfInterval();
        QueueThread queueThread = new QueueThread("QueueThread", roads, interval);
        queueThread.start();
        mainLoop:
        while (true) {
            queueThread.setProgramState(ProgramState.MENU);
            String action = UserInterface.askForMenuAction();
            switch (action) {
                case "1":
                    String roadName = UserInterface.askForRoadName();
                    if (queueThread.getQueue().size() < roads) {
                        queueThread.addRoad(new Road(roadName, queueThread.getQueue().size(), interval));
                        System.out.printf("%s added!\n", roadName);
                    } else {
                        System.out.println("queue is full");
                    }
                    UserInterface.askForConsoleClear();
                    break;
                case "2":
                    if (!queueThread.getQueue().isEmpty()) {
                        queueThread.removeRoad();
                    } else {
                        System.out.println("queue is empty");
                    }
                    UserInterface.askForConsoleClear();
                    break;
                case "3":
                    queueThread.setProgramState(ProgramState.SYSTEM);
                    UserInterface.askForConsoleClear();
                    break;
                case "0":
                    queueThread.setProgramState(ProgramState.EXITED);
                    try {
                        queueThread.join();
                        System.out.println("Bye!");
                        break mainLoop;
                    } catch (Exception e) {
                        System.out.println("Error!");
                    }
            }
        }
    }
}
