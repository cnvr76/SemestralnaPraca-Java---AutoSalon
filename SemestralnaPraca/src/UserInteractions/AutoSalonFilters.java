package UserInteractions;

import AbstractClasses.Vehicle;

import java.util.List;

public interface AutoSalonFilters {

    // Filters by vehicle type (Also use them in availableVehicles() to filter)
    <T extends Vehicle> List<T> getAllInstancesOf(Class<T> type);

    Vehicle[] allEVehicles();
    Vehicle[] allMotorVehicles();
    Vehicle[] allOtherVehicles();

    // Filters to show available vehicles
    Vehicle[] availableVehicles(Vehicle[] selection);

}
