package AbstractClasses;

import MotorVehicles.Scooter;
import UserInteractions.Customer;
import UserInteractions.Main;

import java.util.Random;

public abstract class Vehicle implements BorrowAble {
    // Current gasoline prices in SLovakia
    public final double cenaZaLiterBenzin = 1.49;
    public final double cenaZaLiterDiesel = 1.39;
    public final double cenaZaLiterGas = 0.79;
    // Current prices for kWh (to charge e-vehicles)
    public final double cenaZaKWh = new Random().nextDouble(0.193, 0.522);
    // Atributes for rental purposes (varies from the vehicle type)
    protected int costDay, costHour;
    // Tax constant value for rental purposes
    public final double tax = 0.20;
    // Basic parameters for every Vehicle
    protected int numberOfSeats;
    private int rok;
    protected int horsePower;
    protected String color, state;
    private String znacka, model;
    // Parameters for a rent
    private int cenaHodina, cenaDen;
    private boolean borrowed;
    private String borrower;

    // Assigning unique ID to every created Vehicle
//    private static int idCount = 0;
//    protected int id;

    /**
     * All the parameters that will contain all the vehicles no matter the certain type
     * @param type - is used to distinguish different types while reading from the database
     * @param znacka
     * @param model
     * @param color
     * @param state
     * @param rok
     * @param numberOfSeats
     * @param horsePower
     */
    public Vehicle(String type, String znacka, String model, String color, String state, int rok, int numberOfSeats, int horsePower){
        this.znacka = znacka;
        this.model = model;
        this.color = color;
        this.state = state;
        this.rok = rok;
        this.numberOfSeats = numberOfSeats;
        this.horsePower = horsePower;

//        this.id = idCount;
//        idCount++;
    }

    public String getState(){
        return state;
    }
    public int getHorsePower(){
        return horsePower;
    }

    /**
     * This should have been an overload method that will search for the requested horsepower,
     * but it was never used because of the sorting by horsepower, but, I guess, it would be nice additional filter
     * @param searchedPower - power we want to find
     * @return - returns a Vehicle with this horsepower (or null)
     */
    public Vehicle getHorsePower(int searchedPower){
        if (searchedPower == this.horsePower){
            return this;
        }
        return null;
    }

    public void setModel(String newModel){
        this.model = newModel;
    }
    public String getModel(){
        return model;
    }

    public void setBrand(String newZnacka){
        this.znacka = newZnacka;
    }
    public String getBrand(){
        return znacka;
    }

    public int getRok(){
        return rok;
    }
    public void setRok(int newRok){
        this.rok = newRok;
    }

    public int getCenaHodina(){
        return cenaHodina;
    }
    public void setCenaHodina(int newCena){
        this.cenaHodina = newCena;
    }

    public int getCenaDen(){
        return cenaDen;
    }
    public void setCenaDen(int newCena){
        this.cenaDen = newCena;
    }

    public abstract int calculateShortRentalCost(int hours);

    public abstract int calculateRentalCost(int days);

    /**
     * This method is used in AutoSalon method to decrease fuel/battery level for the refuel purposes
     * Method contains all the exception handling so in AutoSalon there was only need for a timer
     * @param amount - decreasing level by this param
     */
    public void consume(double amount) {
        if (this instanceof FuelVehicle) {
            // Won't go below 0
            FuelVehicle veh = (FuelVehicle) this;
            veh.setFuelLevel(Math.max(0, veh.getFuelLevel() - amount));
        } else if (this instanceof ElectricVehicle) {
            // Won't go below 0
            ElectricVehicle veh = (ElectricVehicle) this;
            veh.setBatteryPercentage((int)(Math.max(0, veh.getBatteryPercentage() - amount)));
        }
    }

    // Overriding methods of BorrowAble to check if the vehicle is available

    /**
     *
     * @param customer - using the current customer's info for rent
     * @return - returns true if item was borrowed or false if there was an error somewhere
     */
    @Override
    public boolean borrowItem(Customer customer) {
        if (this.borrower == null && customer.getDebt() == 0){
            this.borrower = customer.getName();
            this.borrowed = true;
            System.out.println(Main.GREEN + "Rented successfully" + Main.RESET);
            return true;
        }
        System.out.println(Main.RED + "Error in renting" + Main.RESET);
        return false;
    }

    @Override
    public boolean returnItem() {
        if (this.borrowed) {
            this.borrower = null;
            this.borrowed = false;
            System.out.println("Returned successfully: " + Main.title(this.znacka + " " + this.model));
            return true;
        }
        System.err.println("Error in renting this vehicle");
        return false;
    }

    @Override
    public boolean isAvailable() {
        return !borrowed;
    }

    // Display all the information about this type of vehicle
    public abstract String toString();

}
