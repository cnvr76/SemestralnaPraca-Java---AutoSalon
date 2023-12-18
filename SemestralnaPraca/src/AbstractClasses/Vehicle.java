package AbstractClasses;

import MotorVehicles.Scooter;
import UserInteractions.Customer;
import UserInteractions.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

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
    protected double vehicleWeight, loadCapacity;
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

    /***
     *
     * @param amount
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

    /***
     *
     * @param customer
     * @return
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
