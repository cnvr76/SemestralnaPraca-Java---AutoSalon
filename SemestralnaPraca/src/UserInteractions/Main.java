package UserInteractions;

import AbstractClasses.Vehicle;
import EVehicles.Ebike;
import EVehicles.Escooter;
import MotorVehicles.Car;
import MotorVehicles.Scooter;
import OtherVehicles.Bike;

import java.time.Year;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Start window for customer info
        clearConsole();
        System.out.println(GREEN + "Welcome to our AutoSalon ^-^" + RESET);
        System.out.print("Write down your name here: ");
        String customerName = scanner.nextLine();
        double customerBalance;
        try {
            System.out.print("Your balance: ");
            customerBalance = scanner.nextDouble();
        }catch (InputMismatchException e){
            System.out.println("Balanced field skipped, but you can add to it later...");
            customerBalance = 0;
        }
        scanner.nextLine();

        // Customer initialisation
        Customer customer = new Customer(AutoSalon.checkInput(customerName), customerBalance);
        customer.showBalance();
        Thread.sleep(1000);

        // Main program
        AutoSalon salon = new AutoSalon(customer);

        boolean show;
        while (true){
            clearConsole();
            // Always return to the default settings (all vehicles list)
            Vehicle[] selectedVehicles = salon.poleVehicles;
            show = false;

            // Reminder that customer has unpaid debt (if enough money -> pays automatically)
            if (customer.getDebt() > 0 && customer.getBalance() < customer.getDebt()){
                customer.setDebt(customer.getDebt() - customer.getBalance());
                customer.setBalance(0);
                System.out.println(RED + ">_<\nCurrent debt in Euro: " + customer.getDebt() + RESET);
            } else if (customer.getDebt() > 0 && customer.getBalance() >= customer.getDebt()) {
                System.out.println(GREEN + "---\nDebt has been payed ^-^\n" + RESET);
                customer.setBalance(customer.getBalance() - customer.getDebt());
                customer.setDebt(0);
            }
            System.out.println("Here is everything our AutoSalon can provide you with:\n");
            System.out.println("""
                    All - show all vehicles
                    Free - show all available vehicles
                    Cars - show all cars
                    Scooters - show all scooters
                    EBikes - show all E-bikes
                    EScooters - show all E-scooters
                    Bikes - show all bikes
                    Power - show from the least to most powerful
                    ---
                    Add - add to your balance
                    Show - show your balance
                    Rented - show your rented vehicles
                    Back - return every rented vehicle
                    ---
                    q - Quit
                    (Text case doesn't matter)
                    """);
            System.out.print(">>Action: ");
            String choice = scanner.nextLine().toLowerCase().trim();
            System.out.println();
            switch (choice) {
                case "all" ->
                    // Just gonna print all the vehicles by default
                        show = true;
                case "free" -> {
                    selectedVehicles = salon.availableVehicles(selectedVehicles);
                    show = true;
                }
                case "cars" -> switchFilters(salon, scanner, customer, Car.class);
                case "scooters" -> switchFilters(salon, scanner, customer, Scooter.class);
                case "ebikes" -> switchFilters(salon, scanner, customer, Ebike.class);
                case "escooters" -> switchFilters(salon, scanner, customer, Escooter.class);
                case "bikes" -> switchFilters(salon, scanner, customer, Bike.class);
                case "power" -> {
                    selectedVehicles = salon.getSortedByHorsepower(selectedVehicles);
                    show = true;
                }
                case "add" -> {
                    customer.showBalance();
                    try {
                        System.out.print("Sum to add: ");
                        customerBalance = scanner.nextDouble();
                        System.out.println("Added " + customerBalance + " Euro");
                    } catch (InputMismatchException e) {
                        System.out.println("Incorrect value..." +
                                "\nBalance: " + customer.getBalance());
                        customerBalance = 0;
                    }
                    scanner.nextLine();
                    customer.addToBalance(customerBalance);
                }
                case "show" -> customer.showBalance();
                case "rented" -> {
                    System.out.println("Your rented vehicles: ");
                    showArray(salon.poleRentedVehicles.toArray(new Vehicle[0]), true);
                }
                case "back" -> {
                    if (customer.getDebt() > 0 || salon.poleRentedVehicles.size() == 0){
                        System.out.println(RED + "Can't return any vehicles" + RESET);
                        break;
                    }
                    salon.returnAll();
                    System.out.println(GREEN + "All items returned successfully" + RESET);
                }
                case "admin add" -> salon.addVehicle();
                case "admin rm" -> {
                    System.out.print("Enter the model to remove: ");
                    salon.removeVehicle(scanner.nextLine());
                }
                case "q" -> {
                    clearConsole();
                    if (customer.getDebt() > 0){
                        System.out.println(YELLOW + "Nuh uh, debts first ^_-" + RESET);
                        break;
                    }
                    System.out.println("\nSee you next time, " + customer.getName() + ", in our salon:)\n");
                    System.exit(0);
                }
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
            if (show){
                showArray(selectedVehicles, false);
            }
            System.out.print("\nEnter to continue...");
            scanner.nextLine();
        }
    }
    // Switch function
    private static void switchFilters(AutoSalon salon, Scanner scanner, Customer customer, Class<? extends Vehicle> type){
        boolean continueFiltering = true;
        boolean show = false;

        String className = type.getSimpleName();

        Vehicle[] selectedVehicles = salon.getAllInstancesOf(type).toArray(new Vehicle[0]);

        while (continueFiltering) {
            clearConsole();
            System.out.println("More precise filter options:\n");
            System.out.printf("""
                    1 - Get all (reset filters)
                    2 - Get available
                    3 - Get with Manual gearbox
                    4 - Get with Automatic gearbox
                    5 - Get Not used (state: new)
                    6 - Get Used
                    7 - Get Newest (from %s)
                    8 - Models that contain input
                    9 - Get sorted by horsepower
                    ---
                    10 - Borrow %s
                    11 - Return %s
                    12 - Refuel/Charge rented vehicle
                    13 - Get current fuel/percentage level
                    ---
                    0 - Quit filtering
                    """, Year.now().getValue() - 3, className, className);
            System.out.print("\n>>Variant: ");
            String filterChoice = scanner.nextLine();
            switch (filterChoice) {
                case "1" -> {
                    selectedVehicles = salon.getAllInstancesOf(type).toArray(new Vehicle[0]);
                    show = true;
                }
                case "2" -> {
                    selectedVehicles = salon.availableVehicles(selectedVehicles);
                    show = true;
                }
                case "3" -> {
                    selectedVehicles = salon.gearBoxType("manual", selectedVehicles);
                    show = true;
                }
                case "4" -> {
                    selectedVehicles = salon.gearBoxType("automatic", selectedVehicles);
                    show = true;
                }
                case "5" -> {
                    selectedVehicles = salon.getUsedNew("new", selectedVehicles);
                    show = true;
                }
                case "6" -> {
                    selectedVehicles = salon.getUsedNew("used", selectedVehicles);
                    show = true;
                }
                case "7" -> {
                    selectedVehicles = salon.getFromYear(Year.now().getValue() - 3, selectedVehicles);
                    show = true;
                }
                case "8" -> {
                    System.out.print("Models that contain...: ");
                    selectedVehicles = salon.getVehiclesContainModel(scanner.nextLine(), selectedVehicles);
                    show = true;
                }
                case "9" -> {
                    selectedVehicles = salon.getSortedByHorsepower(selectedVehicles);
                    show = true;
                }
                case "10" -> {
                    System.out.print("Model you want to rent: ");
                    String model = scanner.nextLine();

                    System.out.print("*num* + hours/days (ex.: 5h; 2d): ");
                    String time = scanner.nextLine().toLowerCase();
                    // Exception handling inside borrowVehicle() method
                    double cost = salon.borrowVehicle(model, time);
                    // Deciding if it is enough money to pay for rent
                    if (cost != 0){
                        System.out.println("Rental cost: " + cost);
                        checkDebt(cost, customer);
                    }
                }
                case "11" -> {
                    System.out.println("Your rented vehicles: ");
                    showArray(salon.poleRentedVehicles.toArray(new Vehicle[0]), true);

                    if (salon.poleRentedVehicles.size() == 0) break;
                    if (customer.getDebt() > 0){
                        System.out.println(RED + "You can't return any vehicle until you pay the debt" + RESET);
                        break;
                    }

                    System.out.print("\nModel you want to return: ");
                    salon.returnVehicle(scanner.nextLine());
                    selectedVehicles = salon.getAllInstancesOf(type).toArray(new Vehicle[0]);
                    selectedVehicles = salon.availableVehicles(selectedVehicles);
                }
                case "12" -> {
                    System.out.println("Your rented vehicles: ");
                    showArray(salon.poleRentedVehicles.toArray(new Vehicle[0]), true);

                    if (salon.poleRentedVehicles.size() == 0) break;

                    System.out.print("\nModel you want to refuel/charge: ");
                    String model = scanner.nextLine();

                    System.out.print("Level you want to fill/charge: ");
                    double toLevel = AutoSalon.checkDoubleInput(scanner.nextLine());
                    // Exception handling inside RefuelOrCharge() method
                    double cost = salon.RefuelOrCharge(model, toLevel, type);
                    if (cost == 0) {
                        System.out.println(YELLOW + "You don't have to refill, the cost is 0" + RESET);
                        break;
                    }else if (cost == -1){
                        System.out.println(RED + "Error in input parameters" + RESET);
                        break;
                    }
                    System.out.println("Service cost: " + cost);
                    // Deciding if it is enough money to pay
                    checkDebt(cost, customer);
                }
                case "13" -> System.out.println(salon.getCurrentLevelOrBattery());
                case "0" -> continueFiltering = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
            if (show){
                showArray(selectedVehicles, true);
            }
            if (selectedVehicles.length == 0){
                selectedVehicles = salon.getAllInstancesOf(type).toArray(new Vehicle[0]);
            }
            // Checking if customer wants to apply several filters on the selection
            if (continueFiltering) {
                continueFiltering = continueCheck(scanner);
                show = false;
            }
            clearConsole();
        }
    }

    // Additional functions
    public static void checkDebt(double cost, Customer customer){
        if (cost > customer.getBalance()){
            customer.addDebt(cost - customer.getBalance());
            customer.setBalance(0);
            System.out.println(YELLOW + "You don't have enough money to pay for this service\n" +
                    "Your current debt is: " + RESET + RED + customer.getDebt() + " Euro" + RESET);
            return;
        }
        customer.setBalance(customer.getBalance() - cost);
        System.out.println(GREEN + "Successfully paid" + RESET);
        System.out.println("Current balance: " + customer.getBalance());
    }

    public static <T extends Vehicle> void showArray(T[] array, boolean sorry){
        if (array.length == 0 && sorry){
            System.out.println(RED + "\nSorry, but we don't have options satisfying your needs..." + RESET);
            return;
        }

        for (int i = 0; i < array.length; i++){
            System.out.println((i+1) + ") " + array[i].toString());
        }
    }

    public static boolean continueCheck(Scanner scanner){
        System.out.print("\nDo you want to apply more filters? (y/n): ");
        String decision = scanner.nextLine().trim().toLowerCase();
        System.out.println();
        return !"n".equals(decision);
    }

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Clears the console on Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (final Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    public static String title(String text){
        StringBuilder sb = new StringBuilder();
        for (String word : text.split(" ")){
            word = word.toLowerCase();
            String letter = word.split("")[0];
            sb.append(word.replaceFirst(letter, letter.toUpperCase())).append(" ");
        }
        return sb.toString().trim();
    }

}