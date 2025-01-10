package traffic;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner scanner = new Scanner(System.in);

    private static int askForPositiveInteger(String message) {
        System.out.print(message);
        String errorMessage = "Incorrect Input. Try again:";
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input > 0) {
                    return input;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    public static int askForNumberOfRoads() {
        String message = "Input the number of roads: ";
        return askForPositiveInteger(message);
    }

    public static int askForNumberOfInterval() {
        String message = "Input the interval: ";
        return askForPositiveInteger(message);
    }

    public static String askForMenuAction() {
        while (true) {
            System.out.println("""
            Menu:
            1. Add road
            2. Delete road
            3. Open system
            0. Quit
            """);
            String input = scanner.nextLine();
            if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("0")) {
                return input;
            } else {
                System.out.println("Incorrect option");
                askForConsoleClear();
            }
        }
    }

    public static void clearConsole() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException ignored) {

        }
    }

    public static void askForConsoleClear() {
        scanner.nextLine();
        clearConsole();
    }

    public static String askForRoadName() {
        System.out.print("Input road name: ");
        while (true) {
            String input = scanner.nextLine();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Incorrect Input. Try again:");
            }
        }
    }
}
