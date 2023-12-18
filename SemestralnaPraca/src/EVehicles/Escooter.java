package EVehicles;

import AbstractClasses.ElectricVehicle;
import AbstractClasses.Vehicle;

import java.util.Random;

public class Escooter extends Vehicle implements ElectricVehicle {

    private double currentPercentage, maxPercentage = 100;

    /***
     *
     * @param type
     * @param znacka
     * @param model
     * @param color
     * @param state
     * @param rok
     * @param numberOfSeats
     * @param horsePower
     */
    public Escooter(String type, String znacka, String model, String color, String state, int rok, int numberOfSeats, int horsePower) {
        super(type, znacka, model, color, state, rok, numberOfSeats, horsePower);
        // Always come fully charged
        this.currentPercentage = this.maxPercentage;

        this.costDay =  new Random().nextInt(17, 22);
        this.costHour =  new Random().nextInt(5, 7);
    }

    @Override
    public int getBatteryPercentage() {
        return (int) currentPercentage;
    }

    @Override
    public void setBatteryPercentage(int amount) {
        this.currentPercentage = amount;
    }

    /***
     *
     * @param toBatteryPercentage
     * @return
     */
    @Override
    public double charge(int toBatteryPercentage) {
        double filled = toBatteryPercentage - currentPercentage;
        if (toBatteryPercentage >= maxPercentage) {
            // If the input percentage >= max fuel level -> fill to the max level and return cost only for used fuel
            filled = maxPercentage - currentPercentage;
            currentPercentage = maxPercentage;
            return filled * cenaZaKWh;
        }
        if (toBatteryPercentage <= currentPercentage){
            return -1;
        }
        currentPercentage = toBatteryPercentage;
        // Return how much money customer must pay for the refuel
        return filled * cenaZaKWh;
    }

    /***
     *
     * @param hours
     * @return
     */
    @Override
    public int calculateShortRentalCost(int hours) {
        // As a default for any car to rent = 5-7 euro/hour + tax
        int baseCost = costHour * hours;
        return (int) Math.ceil(baseCost + baseCost * tax);
    }

    /***
     *
     * @param days
     * @return
     */
    @Override
    public int calculateRentalCost(int days) {
        // As a default for any car to rent = 17-22 euro/day + tax
        int baseCost = costDay * days;
        return (int) Math.ceil(baseCost + baseCost * tax);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Brand: %s, Model: %s, Color: %s, State: %s, Rok: %s, " +
                        "NumberOfSeats: %s, HorsePower: %s;",
                this.getClass().getSimpleName(), super.getBrand(), super.getModel(), color, super.getState(),
                super.getRok(), numberOfSeats, horsePower);
    }
}
