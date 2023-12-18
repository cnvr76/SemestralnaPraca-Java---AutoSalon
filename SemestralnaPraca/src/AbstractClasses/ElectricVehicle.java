package AbstractClasses;

public interface ElectricVehicle {

    int getBatteryPercentage();
    void setBatteryPercentage(int amount);
    double charge(int toBatteryPercentage);

}
