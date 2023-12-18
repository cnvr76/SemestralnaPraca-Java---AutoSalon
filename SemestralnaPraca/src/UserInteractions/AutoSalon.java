package UserInteractions;

import AbstractClasses.ElectricVehicle;
import AbstractClasses.FuelVehicle;
import AbstractClasses.Vehicle;
import EVehicles.Ebike;
import EVehicles.Escooter;
import MotorVehicles.Car;
import MotorVehicles.Scooter;
import OtherVehicles.Bike;

import java.io.*;
import java.util.*;

public class AutoSalon implements AutoSalonFilters {

    private final Customer customer;
    public Vehicle[] poleVehicles;
    public List<Vehicle> poleRentedVehicles;
    // DB file & directory paths (Absolute path not depending on the project location on the PC)
    private final String dirPath = "src/DB/allVehicles.txt";
    // List of all vehicle packages names to loop over them in classPackage() and classExists() methods
    private final String[] packages = {"EVehicles", "MotorVehicles", "OtherVehicles"};

    public AutoSalon(Customer customer){
        this.customer = customer;
        this.poleVehicles = getVehiclesFromDB();
        this.poleRentedVehicles = new ArrayList<>();

        // Decreasing the fuel/battery level on salon initialization so the timer starts
        // when poleRentedVehicles != 0
        this.decreaseLevel();
    }

    /****
     * Checking if Class exists (by its name) in the project
     *
     * @param className
     * @param packages
     * @return
     */
    private boolean classExists(String className, String[] packages) {
        for (String packageName : packages) {
            try {
                Class.forName(packageName + "." + className);
                return true; // Class exists in this package
            } catch (ClassNotFoundException ignored) {
                // Try next package
            }
        }
        return false;
    }

    /***
     * Basically the same, but it returns the package name where the existing class is
     *
     * @param className
     * @return
     */
    private String classPackage(String className) {
        for (String packageName : packages) {
            try {
                Class.forName(packageName + "." + className);
                return packageName; // Class exists in this package
            } catch (ClassNotFoundException ignored) {
                // Try next package
            }
        }
        return "nope";
    }

    /***
     * Admin settings on adding vehicles
     *
     * @return
     */
    private String createNewVehicle() {
        Scanner scanner = new Scanner(System.in);
        boolean exists;
        String selectedPackage = "", selectedType = "";
        do {
            Main.clearConsole();
            System.out.println("Choose what you want to create: ");
            System.out.println("""
                    -> Bike
                    -> Car
                    -> EBike
                    -> EScooter
                    -> Scooter
                    (Text case doesn't matter)
                    """);
            System.out.print(">> ");
            String type = Main.title(checkInput(scanner.nextLine()));
            exists = classExists(type, packages);
            if (!exists) {
                System.out.println("Class not found in the project folder");
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    // pass
                }
            } else {
                selectedPackage = classPackage(type);
                selectedType = type;
            }
        } while (!exists);
        // Asking for main input parameters for any Vehicle
        System.out.printf("Name for the %s: ", selectedType);
        String brand = checkInput(scanner.nextLine());

        System.out.print("Model: ");
        String model = checkInput(scanner.nextLine());

        System.out.print("Color: ");
        String color = checkInput(scanner.nextLine());

        System.out.print("State (New/Used): ");
        String state = checkInput(scanner.nextLine());

        System.out.print("Year: ");
        int year = checkIntInput(scanner.nextLine());

        int seats = 1, power = 0;

        switch (selectedPackage){
            case "EVehicles":
                System.out.print("Number of seats: ");
                seats = checkIntInput(scanner.nextLine());

                System.out.print("Horse power: ");
                power = checkIntInput(scanner.nextLine());

                break;
            case "MotorVehicles":
                System.out.print("Number of seats: ");
                seats = checkIntInput(scanner.nextLine());

                System.out.print("Horse power: ");
                power = checkIntInput(scanner.nextLine());

                System.out.print("Max fuel level: ");
                double maxLevel = checkDoubleInput(scanner.nextLine());

                System.out.print("Range on full tank: ");
                double range = checkDoubleInput(scanner.nextLine());

                System.out.print("Max speed: ");
                double speed = checkDoubleInput(scanner.nextLine());

                System.out.print("Fuel type (Petrol/Gas/Diesel): ");
                String fuel = checkInput(scanner.nextLine());

                System.out.print("Gear box (Automatic/Manual): ");
                String gearBox = checkInput(scanner.nextLine());

                return String.format("Type: %s, Brand: %s, Model: %s, Color: %s, State: %s, Year: %s, NumberOfSeats: %s, " +
                                "HorsePower: %s, MaxFuelLevel: %s, RangeOnFullTank: %s, MaxSpeed: %s, FuelType: %s, GearBox: %s",
                        selectedType, brand, model, color, state, year, seats, power, maxLevel, range, speed, fuel, gearBox);

            case "OtherVehicles":
                break;
            default:
                System.err.println("Package not found");
                break;
        }

        return String.format("Type: %s, Brand: %s, Model: %s, Color: %s, State: %s, Year: %s, NumberOfSeats: %s, HorsePower: %s",
                selectedType, brand, model, color, state, year, seats, power);
    }

    // Method for both: creating new vehicle & writing to DB
    public void addVehicle(){
        writeInfoToDB(createNewVehicle(), true, ";\n");
        System.out.println(Main.GREEN + "Cool for you, Vehicle added successfully" + Main.RESET);
        this.poleVehicles = getVehiclesFromDB();
    }

    /***
     * Removes only one at a time by model
     *
     * @param vehicle
     * @return
     */
    private boolean removeVehicleFromDB(Vehicle vehicle){
        // With this we read all the lines to append and write to file later
        String[] fileInfo = readFromDB(dirPath);
        if (vehicle != null) {
            String removeModel = vehicle.getModel();
            // With the line below we clear the file
            writeInfoToDB("", false, "");
            for (String line : fileInfo) {
                String[] separate = line.split(",");
                String foundModel = separate[2].substring(separate[2].indexOf(":")+1).trim();
                // Skipping vehicle with model we want to delete
                boolean correctItem = foundModel.equalsIgnoreCase(removeModel);
                if (correctItem) {
                    System.out.println(line);
                    continue;
                }
                writeInfoToDB(line, true, "\n");
            }
            return true;
        }else{
            System.out.println(Main.RED + "This Item doesn't exist" + Main.RESET);
            return false;
        }
    }

    /***
     * This method is used in Main class as an admin command
     * It is consists of other private methods (like getting the vehicle -> ...
     * ... -> removing from DB if exists -> updating list)
     *
     * @param model
     */
    public void removeVehicle(String model) {
        Vehicle vehicleToRemove = getVehicleByModel(Main.title(model));
        boolean removed = removeVehicleFromDB(vehicleToRemove);
        if (removed){
            System.out.println(Main.GREEN + "Removed successfully" + Main.RESET);
            this.poleVehicles = getVehiclesFromDB();
        }
    }

    /***
     *
     * @param model
     * @return
     */

    public Vehicle getVehicleByModel(String model){
        for (Vehicle vehicle : poleVehicles){
            if (vehicle.getModel().equalsIgnoreCase(model.trim())){
                return vehicle;
            }
        }
        return null;
    }

    /***
     *
     * @param str
     * @param selection
     * @return
     */
    public Vehicle[] getVehiclesContainModel(String str, Vehicle[] selection){
        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle v : selection){
            if (v.getModel().toLowerCase().contains(str.toLowerCase())){
                vehicles.add(v);
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    /***
     * Initialising different type Vehicles on info from DB
     * @return
     */
    public Vehicle[] getVehiclesFromDB(){
        String[] read = readFromDB(dirPath);
        List<Vehicle> vehicles = new ArrayList<>();
        for (String line : read){
            Vehicle vehicle = null;
            String[] separate = line.replace(";", "").split(",");
            // Reading basic parameters
            String type = separate[0].substring(separate[0].indexOf(":") + 1).trim();
            String brand = separate[1].substring(separate[1].indexOf(":") + 1).trim();
            String model = separate[2].substring(separate[2].indexOf(":") + 1).trim();
            String color = separate[3].substring(separate[3].indexOf(":") + 1).trim();
            String state = separate[4].substring(separate[4].indexOf(":") + 1).trim();
            int year = checkIntInput(separate[5].substring(separate[5].indexOf(":") + 1).trim());
            // Additional parameters depending on the type + initialisation
            int seats = 1, power = 0;
            double fuelLevel = 0, range = 0, speed = 0;
            String fuel = "unknown", gearBox = "unknown";
            // First switch to write parameters for different vehicles types depending on the package
            String Package = classPackage(type).toLowerCase();
            switch (Package){
                case "evehicles" -> {
                    seats = checkIntInput(separate[6].substring(separate[6].indexOf(":") + 1).trim());
                    power = checkIntInput(separate[7].substring(separate[7].indexOf(":") + 1).trim());
                }
                case "motorvehicles" -> {
                    seats = checkIntInput(separate[6].substring(separate[6].indexOf(":") + 1).trim());
                    fuelLevel = checkDoubleInput(separate[7].substring(separate[7].indexOf(":") + 1).trim());
                    range = checkDoubleInput(separate[8].substring(separate[8].indexOf(":") + 1).trim());
                    power = checkIntInput(separate[9].substring(separate[9].indexOf(":") + 1).trim());
                    speed = checkDoubleInput(separate[10].substring(separate[10].indexOf(":") + 1).trim());
                    fuel = separate[11].substring(separate[11].indexOf(":") + 1).trim();
                    gearBox = separate[12].substring(separate[12].indexOf(":") + 1).trim();
                }
                case "othervehicles" -> {
                    // pass (this is written just to make sure class exists)
                }
                default -> {
                    System.err.println("Class doesn't exist in any of the packages");
                    continue;
                }
            }
            // Second switch is to initialize an object depending on the class name with written parameters above
            switch (type.toLowerCase()) {
                case "ebike" -> vehicle = new Ebike(type, brand, model, color, state, year, seats, power);
                case "escooter" -> vehicle = new Escooter(type, brand, model, color, state, year, seats, power);
                case "car" -> vehicle = new Car(type, brand, model, color, state, year, seats, fuelLevel,
                        range, power, speed, fuel, gearBox);
                case "scooter" -> vehicle = new Scooter(type, brand, model, color, state, year, seats, fuelLevel,
                        range, power, speed, fuel, gearBox);
                case "bike" -> vehicle = new Bike(type, brand, model, color, state, year);
                default -> System.err.println("Problems with creating " + type);
            }
            if (vehicle != null) vehicles.add(vehicle);
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    /***
     * Writing (+ creating new Vehicle) to the DB
     *
     * @param newVehicle
     * @param append
     * @param sep
     */
    private void writeInfoToDB(String newVehicle, boolean append, String sep) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath, append));
            writer.write(newVehicle + sep);
            writer.close();
        } catch (IOException ioe){
            System.err.println("Error in writing this Item");
        }
    }

    /***
     * Reading from DB text (Returning each line in String[] array)
     *
     * @param filePath
     * @return
     */
    private String[] readFromDB(String filePath){
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }catch (IOException e){
            System.out.println("There is no such file");
            e.printStackTrace();
        }
        return lines.toArray(new String[0]);
    }

    /***
     * Null check -> if null => param = "unknown"
     *
     * @param input
     * @return
     */
    public static String checkInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "unknown";
        }
        return Main.title(input.trim());
    }

    /***
     * "unknown" && InputMismatchException check -> 0, else -> convert
     *
     * @param input
     * @return
     */
    public static int checkIntInput(String input){
        try {
            try {
                return Math.abs(Integer.valueOf(input));
            } catch (NumberFormatException e){
                return Math.abs((int) Math.ceil(Double.valueOf(input)));
            }
        } catch (NumberFormatException e){
            return 0;
        }
    }

    /***
     * "unknown" && NumberFormatException check -> 0, else -> convert
     *
     * @param input
     * @return
     */
    public static double checkDoubleInput(String input){
        try {
            return Math.abs(Double.valueOf(input));
        }catch (NumberFormatException e){
            return 0;
        }
    }

    /***
     * Filtering methods for customer
     * Method that returns the list filled with certain class name
     *
     * @param type
     * @return
     * @param <T>
     */
    @Override
    public <T extends Vehicle> List<T> getAllInstancesOf(Class<T> type){
        List<T> instances = new ArrayList<>();
        for (Vehicle vehicle : poleVehicles){
            if (type.isInstance(vehicle)){
                // type.cast(vehicle) is the same as (T) vehicle, but safer
                instances.add(type.cast(vehicle));
            }
        }
        return instances;
    }

    /***
     * Returns all the files from the input package name
     *
     * @param packageName
     * @return
     */
    private String[] getAllClassesFromPackage(String packageName) {
        List<String> classes = new ArrayList<>();
        for (Vehicle vehicle : poleVehicles) {
            Package vehiclePackage = vehicle.getClass().getPackage();
            if (vehiclePackage != null && vehiclePackage.getName().equals(packageName)) {
                // Adds the exact class name that are in the certain package
                classes.add(vehicle.getClass().getSimpleName());
            }
        }
        return classes.toArray(new String[0]);
    }

    /***
     * Since returned classes from the package come in String type -> convert them to Class type
     *
     * @param fullName
     * @return
     */
    private Class<? extends Vehicle> convertStringToClass(String fullName){
        try {
            // fullName = package.ClassName;
            // EVehicles.Ebike
            Class<?> converted = Class.forName(fullName);
            return converted.asSubclass(Vehicle.class);
        } catch (ClassNotFoundException e){
            return null;
        }
    }


    @Override
    public Vehicle[] allEVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String packageName = "EVehicles";
        for (String className : getAllClassesFromPackage(packageName)){
            Class<? extends Vehicle> clazz = convertStringToClass(packageName + className);
            if (clazz != null){
                vehicles.addAll(getAllInstancesOf(clazz));
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    @Override
    public Vehicle[] allMotorVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String packageName = "MotorVehicles";
        for (String className : getAllClassesFromPackage(packageName)){
            Class<? extends Vehicle> clazz = convertStringToClass(packageName + className);
            if (clazz != null){
                vehicles.addAll(getAllInstancesOf(clazz));
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    @Override
    public Vehicle[] allOtherVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String packageName = "OtherVehicles";
        for (String className : getAllClassesFromPackage(packageName)){
            Class<? extends Vehicle> clazz = convertStringToClass(packageName + className);
            if (clazz != null){
                vehicles.addAll(getAllInstancesOf(clazz));
            }
        }
        return vehicles.toArray(new Vehicle[0]);
    }

    @Override
    public Vehicle[] availableVehicles(Vehicle[] selection) {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle vehicle : selection){
            if (vehicle.isAvailable()){
                available.add(vehicle);
            }
        }
        return available.toArray(new Vehicle[0]);
    }

    // Specific filters

    /***
     *
     * @param type
     * @param selection
     * @return
     */
    public Vehicle[] gearBoxType(String type, Vehicle[] selection){
        List<Vehicle> filtered = new ArrayList<>();
        for (Vehicle vehicle : selection){
            if (vehicle instanceof FuelVehicle && type.equalsIgnoreCase(((FuelVehicle) vehicle).getGearBox())){
                filtered.add(vehicle);
            }
        }
        return filtered.toArray(new Vehicle[0]);
    }

    /***
     *
     * @param state
     * @param selection
     * @return
     */
    public Vehicle[] getUsedNew(String state, Vehicle[] selection){
        List<Vehicle> filtered = new ArrayList<>();
        for (Vehicle vehicle : selection){
            if (state.equalsIgnoreCase(vehicle.getState())){
                filtered.add(vehicle);
            }
        }
        return filtered.toArray(new Vehicle[0]);
    }

    /***
     *
     * @param year
     * @param selection
     * @return
     */
    public Vehicle[] getFromYear(int year, Vehicle[] selection){
        List<Vehicle> filtered = new ArrayList<>();
        for (Vehicle vehicle : selection){
            if (vehicle.getRok() >= year){
                filtered.add(vehicle);
            }
        }
        return filtered.toArray(new Vehicle[0]);
    }

    /***
     *
     * @param selection
     * @return
     */
    public Vehicle[] getSortedByHorsepower(Vehicle[] selection){
        // Converting input selection into List
        List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(selection));
        // Leaving only instances that can have proper horsepower attribute
        vehicles.removeIf(vehicle -> !(vehicle instanceof FuelVehicle || vehicle instanceof ElectricVehicle));
        // Sorting list of vehicles by object's horsepower
        vehicles.sort(Comparator.comparing(Vehicle::getHorsePower));
        return vehicles.toArray(new Vehicle[0]);
    }

    // Rent

    /***
     *
     * @param model
     * @param time
     * @return
     */
    public double borrowVehicle(String model, String time){
        Vehicle vehicle = getVehicleByModel(model);
        if (vehicle == null) {
            System.err.println("This model doesn't exist");
            return 0;
        }
        if (!time.matches("\\d+[hd]")){
            System.err.println("Incorrect time format");
            return 0;
        }
        if (!vehicle.borrowItem(customer)){
            return 0;
        }
        poleRentedVehicles.add(vehicle);

        int forHowLong = Integer.valueOf(time.substring(0, time.length()-1));
        char timeUnit = time.charAt(time.length()-1);

        return timeUnit == 'd' ? vehicle.calculateRentalCost(forHowLong) :
                vehicle.calculateShortRentalCost(forHowLong);
    }

    /***
     *
     * @param model
     */
    public void returnVehicle(String model){
        Vehicle vehicle = getVehicleByModel(model);
        if (vehicle != null){
            if (vehicle.returnItem()){
                poleRentedVehicles.remove(vehicle);
            }
        } else {
            System.err.println("This model doesn't exist");
        }
    }

    public void returnAll(){
        poleRentedVehicles.removeIf(Vehicle::returnItem);
    }

    /***
     * Refuel/charge system to rented vehicles
     *
     * @param selected
     * @param toLevel
     * @param type
     * @return
     */
    public double RefuelOrCharge(String selected, double toLevel, Class<? extends Vehicle> type){
        Vehicle vehicle = getVehicleByModel(selected);
        if (vehicle == null){
            System.err.println("This vehicle doesn't exist");
            return -1;
        }
        if (vehicle.isAvailable()){
            System.err.println("You didn't rent this vehicle");
            return -1;
        }
        if (Objects.equals(classPackage(type.getSimpleName()), "OtherVehicles")){
            System.err.println("You can't refill/charge this type of vehicle");
            return -1;
        }

        return vehicle instanceof FuelVehicle ? ((FuelVehicle) vehicle).refuel(toLevel) :
                ((ElectricVehicle) vehicle).charge((int) toLevel);
    }

    public String getCurrentLevelOrBattery(){
        StringBuilder sb = new StringBuilder();
        for (Vehicle vehicle : poleRentedVehicles){
            if (vehicle instanceof FuelVehicle || vehicle instanceof ElectricVehicle) {
                double level = vehicle instanceof FuelVehicle ? ((FuelVehicle) vehicle).getFuelLevel() :
                        ((ElectricVehicle) vehicle).getBatteryPercentage();
                sb.append(String.format("%s %s current level: %s\n",
                        vehicle.getBrand(), vehicle.getModel(), level));
            }
        }
        return sb.toString();
    }

    // Method that decreases the fuel/battery level to make refuel system not so useless yk:)
    private void decreaseLevel() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (poleRentedVehicles.size() == 0) return;
                for (Vehicle vehicle : poleRentedVehicles) {
                    // Will decrease current level by 3 every 30 seconds (after renting) from every car
                    vehicle.consume(3);
                }
            }
        }, 0, 30 * 1000);
    }

}