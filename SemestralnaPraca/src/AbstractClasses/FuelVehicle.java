package AbstractClasses;

public interface FuelVehicle {

    double getFuelLevel();
    void setFuelLevel(double level);

    /**
     *
     * @param fuelLevel - refuels up to this level
     * @return - returns the cost of the refuel depending on the gas type
     */
    double refuel(double fuelLevel);
    String getGearBox();

}
