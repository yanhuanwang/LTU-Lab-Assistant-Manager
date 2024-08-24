/*
 *  * @author Chengkai Yan (cheyan-3)
 */
import java.util.Scanner;

public class Main {
    static final int MAX_ASSISTANTS = 100;
    static final int MAX_SESSIONS = 100;
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int[] ids = new int[MAX_ASSISTANTS];
        String[] names = new String[MAX_ASSISTANTS];
        int[] credits = new int[MAX_ASSISTANTS];
        int[] totalSalaries = new int[MAX_ASSISTANTS];
        int[] sessionCounts = new int[MAX_ASSISTANTS];
        String[][] sessions = new String[MAX_ASSISTANTS][MAX_SESSIONS];

        int assistantCount = 0;

        while (true) {
            printMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    assistantCount = addLabAssistant(ids, names, credits, assistantCount);
                    break;
                case "2":
                    assistantCount = removeLabAssistant(ids, names, credits, totalSalaries, sessionCounts, sessions, assistantCount);
                    break;
                case "3":
                    registerWorkingHours(ids, names, credits, totalSalaries, sessionCounts, sessions, assistantCount);
                    break;
                case "4":
                    printPaySlip(ids, names, credits, totalSalaries, sessionCounts, sessions, assistantCount);
                    break;
                case "5":
                    printAssistantSummary(ids, names, credits, totalSalaries, sessionCounts, assistantCount);
                    break;
                case "q":
                    System.out.println("Program exiting.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    static void printMenu() {
        System.out.println("----------------------------------");
        System.out.println("# LTU Lab Assistant Manager");
        System.out.println("----------------------------------");
        System.out.println("1. Add lab assistant");
        System.out.println("2. Remove lab assistant");
        System.out.println("3. Register working hours");
        System.out.println("4. Print pay slip");
        System.out.println("5. Print assistant summary");
        System.out.println("q. End program");
        System.out.print("> Enter your option: ");
    }

    static int addLabAssistant(int[] ids, String[] names, int[] credits, int assistantCount) {
        if (assistantCount >= MAX_ASSISTANTS) {
            System.out.println("Maximum number of assistants reached.");
            return assistantCount;
        }

        System.out.print("> Enter lab assistant's name: ");
        String name = scanner.nextLine();

        int credit = -1;
        do {
            try {
                System.out.print("> Enter number of education credits (0-400): ");
                credit = Integer.parseInt(scanner.nextLine());
                if (credit < 0 || credit > 400) {
                    System.out.println("Please enter a value between 0 and 400.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer value.");
            }
        } while (credit < 0 || credit > 400);

        int id;
        boolean unique;
        do {
            id = 1000 + (int) (Math.random() * 9000);
            unique = true;
            for (int i = 0; i < assistantCount; i++) {
                if (ids[i] == id) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        ids[assistantCount] = id;
        names[assistantCount] = name;
        credits[assistantCount] = credit;
        assistantCount++;

        System.out.println("Lab assistant " + name + " was assigned ID " + id + " and added to the system.");
        return assistantCount;
    }

    static int removeLabAssistant(int[] ids, String[] names, int[] credits, int[] totalSalaries, int[] sessionCounts, String[][] sessions, int assistantCount) {
        System.out.print("> Enter assistant's ID number: ");

        int id = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("> Enter assistant's ID number: ");
                id = Integer.parseInt(scanner.nextLine());
                validInput = true; // If parsing succeeds, exit the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer ID number.");
            }
        }

        for (int i = 0; i < assistantCount; i++) {
            if (ids[i] == id) {
                System.out.println("Lab assistant " + names[i] + " was removed from the system.");
                for (int j = i; j < assistantCount - 1; j++) {
                    ids[j] = ids[j + 1];
                    names[j] = names[j + 1];
                    credits[j] = credits[j + 1];
                    totalSalaries[j] = totalSalaries[j + 1];
                    sessionCounts[j] = sessionCounts[j + 1];
                    sessions[j] = sessions[j + 1];
                }
                assistantCount--;
                return assistantCount;
            }
        }
        System.out.println("There is no lab assistant registered with ID " + id + ".");
        return assistantCount;
    }

    static void registerWorkingHours(int[] ids, String[] names, int[] credits, int[] totalSalaries, int[] sessionCounts, String[][] sessions, int assistantCount) {
        System.out.print("> Enter lab assistant's ID number: ");
        int id = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("> Enter lab assistant's ID number: ");
                id = Integer.parseInt(scanner.nextLine());
                validInput = true; // If parsing succeeds, exit the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer ID number.");
            }
        }

        int assistantIndex = -1;
        for (int i = 0; i < assistantCount; i++) {
            if (ids[i] == id) {
                assistantIndex = i;
                break;
            }
        }
        if (assistantIndex == -1) {
            System.out.println("Lab assistant with ID " + id + " not found.");
            return;
        }

        System.out.print("> Enter start time of the session (HH:MM): ");
        String startTime = scanner.nextLine();
        System.out.print("> Enter end time of the session (HH:MM): ");
        String endTime = scanner.nextLine();
        System.out.print("> Enter date of the session (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        int startMinutes = timeToMinutes(startTime);
        int endMinutes = timeToMinutes(endTime);

        int duration = endMinutes - startMinutes;
        if (duration <= 0) {
            System.out.println("End time must be after start time.");
            return;
        }

        int periods = (duration + 29) / 30; // Round up to next half-hour
        int hourlyRate = getHourlyRate(credits[assistantIndex]);
        int salary = periods * hourlyRate / 2; // Half-hour pay

        sessions[assistantIndex][sessionCounts[assistantIndex]++] = date + " " + startTime + "-" + endTime + " " + salary + " kr";
        totalSalaries[assistantIndex] += salary;

        System.out.println("Lab assistant: " + names[assistantIndex]);
        System.out.println("Session time: " + (duration / 60) + " hour " + (duration % 60) + " minutes");
        System.out.println("Salary: " + salary + " kr");
    }

    static void printPaySlip(int[] ids, String[] names, int[] credits, int[] totalSalaries, int[] sessionCounts, String[][] sessions, int assistantCount) {
        System.out.print("> Enter lab assistant's ID number: ");
        int id = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("> Enter lab assistant's ID number: ");
                id = Integer.parseInt(scanner.nextLine());
                validInput = true; // If parsing succeeds, exit the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer ID number.");
            }
        }

        int assistantIndex = -1;
        for (int i = 0; i < assistantCount; i++) {
            if (ids[i] == id) {
                assistantIndex = i;
                break;
            }
        }
        if (assistantIndex == -1) {
            System.out.println("Lab assistant with ID " + id + " not found.");
            return;
        }

        System.out.println("Pay slip LTU");
        System.out.println("Name: " + names[assistantIndex] + " (" + ids[assistantIndex] + ")");
        System.out.println("Number of education credits: " + credits[assistantIndex]);
        System.out.println("Sessions:");
        for (int i = 0; i < sessionCounts[assistantIndex]; i++) {
            System.out.println(sessions[assistantIndex][i]);
        }
        System.out.println("Total number of sessions: " + sessionCounts[assistantIndex]);
        System.out.println("Total salary: " + totalSalaries[assistantIndex] + " kr");
    }

    static void printAssistantSummary(int[] ids, String[] names, int[] credits, int[] totalSalaries, int[] sessionCounts, int assistantCount) {
        System.out.println("LTU Lab Assistant Manager summary:");
        System.out.println("Lab assistants:");

        // Simple bubble sort by name
        for (int i = 0; i < assistantCount - 1; i++) {
            for (int j = i + 1; j < assistantCount; j++) {
                if (names[i].compareTo(names[j]) > 0) {
                    swap(ids, i, j);
                    swap(names, i, j);
                    swap(credits, i, j);
                    swap(totalSalaries, i, j);
                    swap(sessionCounts, i, j);
                }
            }
        }

        int totalSalary = 0;
        int totalSessions = 0;
        for (int i = 0; i < assistantCount; i++) {
            System.out.println(names[i] + " " + ids[i] + " " + credits[i] + " " + totalSalaries[i] + " kr");
            totalSalary += totalSalaries[i];
            totalSessions += sessionCounts[i];
        }
        System.out.println("Total number of sessions: " + totalSessions);
        System.out.println("Total salary: " + totalSalary + " kr");
    }

    static int timeToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    static int getHourlyRate(int credits) {
        if (credits < 100) return 120;
        else if (credits < 250) return 140;
        else return 160;
    }

    static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
