package OtherVehicles;

import AbstractClasses.Vehicle;

import java.util.Random;

public class Bike extends Vehicle {

    /***
     *
     * @param type
     * @param znacka
     * @param model
     * @param color
     * @param state
     * @param rok
     */
    public Bike(String type, String znacka, String model, String color, String state, int rok) {
        super(type, znacka, model, color, state, rok, 1, 0);

        this.costDay =  new Random().nextInt(15, 20);
        this.costHour =  new Random().nextInt(2, 5);
    }

    /***
     *
     * @param hours
     * @return
     */
    @Override
    public int calculateShortRentalCost(int hours) {
        // As a default for any car to rent = 2-5 euro/hour + tax
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
        // As a default for any car to rent = 15-20 euro/day + tax
        int baseCost = costDay * days;
        return (int) Math.ceil(baseCost + baseCost * tax);
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Brand: %s, Model: %s, Color: %s, State: %s, Year: %s",
                this.getClass().getSimpleName(), super.getBrand(), super.getModel(), color, super.getState(), super.getRok());
    }
}
