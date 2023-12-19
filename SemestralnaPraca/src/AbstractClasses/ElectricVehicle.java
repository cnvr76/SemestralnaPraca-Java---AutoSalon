package AbstractClasses;

public interface ElectricVehicle {

    int getBatteryPercentage();
    void setBatteryPercentage(int amount);

    /**
     *
     * @param toBatteryPercentage - charges up to this level
     * @return - returns the cost of the charge
     */
    double charge(int toBatteryPercentage);

}
