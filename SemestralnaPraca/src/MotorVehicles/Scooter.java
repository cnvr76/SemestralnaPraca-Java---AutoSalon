package MotorVehicles;

import AbstractClasses.FuelVehicle;
import AbstractClasses.Vehicle;

import java.util.Random;

public class Scooter extends Vehicle implements FuelVehicle {
    private double rangeOnFullTank, maxSpeed, maxFuelLevel, currentFuelLevel;
    private String fuelType, gearBox;

    // Define cost of selected gasoline type
    private double cenaZaLiter;

    public Scooter(String type, String znacka, String model, String color, String state, int rok, int numberOfSeats, double maxFuelLevel,
               double rangeOnFullTank, int horsePower, double maxSpeed, String fuelType, String gearBox) {
        super(type, znacka, model, color, state, rok, numberOfSeats, horsePower);
        this.rangeOnFullTank = rangeOnFullTank;
        this.maxSpeed = maxSpeed;
        this.fuelType = fuelType;
        this.gearBox = gearBox;

        // Cost depends on the fuel type
        this.cenaZaLiter = chooseFuelCost();
        // Vehicle always come with full tank
        this.maxFuelLevel = maxFuelLevel;
        this.currentFuelLevel = this.maxFuelLevel;

        this.costDay =  new Random().nextInt(20, 25);
        this.costHour =  new Random().nextInt(5, 7);
    }

    @Override
    public String getGearBox(){
        return gearBox;
    }

    @Override
    public double getFuelLevel() {
        return currentFuelLevel;
    }

    @Override
    public void setFuelLevel(double level) {
        this.currentFuelLevel = level;
    }

    @Override
    public double refuel(double toFuelLevel) {
        double filled = toFuelLevel - currentFuelLevel;
        if (toFuelLevel >= maxFuelLevel) {
            filled = maxFuelLevel - currentFuelLevel;
            currentFuelLevel = maxFuelLevel;
            return filled * cenaZaLiter;
        }
        if (toFuelLevel <= currentFuelLevel){
            return -1;
        }
        currentFuelLevel = filled;
        // Return how much money customer must pay for the refuel
        return filled * cenaZaLiter;
    }

    @Override
    public int calculateShortRentalCost(int hours) {
        // As a default for any car to rent = 5-7 euro/hour + tax
        int baseCost = costHour * hours;
        return (int) Math.ceil(baseCost + baseCost * tax);
    }

    @Override
    public int calculateRentalCost(int days) {
        // As a default for any car to rent = 20-25 euro/day + tax
        int baseCost = costDay * days;
        return (int) Math.ceil(baseCost + baseCost * tax);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Brand: %s, Model: %s, Color: %s, State: %s, Rok: %s, " +
                        "NumberOfSeats: %s, MaxFuelLevel: %s, RangeOnFullTank: %s, HorsePower: %s, " +
                        "MaxSpeed: %s, FuelType: %s, GearBox: %s;",
                this.getClass().getSimpleName(), super.getBrand(), super.getModel(), color, super.getState(), super.getRok(),
                numberOfSeats, maxFuelLevel, rangeOnFullTank, horsePower,
                maxSpeed, fuelType, gearBox);
    }

    private double chooseFuelCost(){
        switch (fuelType.toLowerCase()){
            case "benzin": return cenaZaLiterBenzin;
            case "gas": return cenaZaLiterGas;
            case "diesel": return cenaZaLiterDiesel;

            default: return cenaZaLiterBenzin;
        }
    }
}
