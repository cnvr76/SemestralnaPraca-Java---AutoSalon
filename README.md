# SemestralnaPraca (Java) - AutoSalon

## Code Structure

### Class files

- `Main`: This file contains all the user interactions and calls neccessary methods from `AutoSalon` to filter data.
- `AutoSalon`: All of the filter methods, reading/writing from/to "database" (so basically all the hard work).
- `Customer`: This class represents customer with his name, balance and array of rented vehicles.
- `Vehicle`: Abstract class that is used by every vehicle, because it contains mandatory basic parameters and atributes like petrol cost etc. 
- `Car`, `Scooter`: Classes that represent fuel vehicles and therefore implement FuelVehicle interface
- `Bike`: There is only one such class, but this one represents vehicle that can't be refueled or charged like, well, bike, so it is only has basic parameters from Vehicle
- `EBike`, `EScooter`: Classes that represent electric vehicles, so they implement ElectricVehicle
- `BorrowAble`: This class is implemented by the abstract Vehicle class to implement methods such as borrow, return and availability check

### Saving and reading objects from file

- `allVehicles.txt`: Txt file that contains all the created Vehicles which can be targeted by the type (it basically contains objects' toString() methods that later in code is separated and inserted as input parameters)

### Reflection

This program (in the AutoSalon class) uses reflection to look at what classes it has and in which packages they are to make the process of adding new type to any of already provided much easier

### Functionality

Basic filters with Vehicles at the start

1. Show all vehicles
2. Show all available vehicles
3. Show all cars
4. Show all scooters
5. Show all E-bikes
6. Show all E-scooters
7. Show all bikes
8. Show from the least to most powerful (Sorting)

And commands for customer interactions

9. Add to your balance
10. Show your balance
11. Show your rented vehicles
12. Return every rented vehicle

If customer selected certain vehicle type -> more specific filters become available (filters can be layered and applied on each other)
Of course, not every vehicle has, for example, gearbox, so this filter won't show anything for the incorrect type

1. Get all (reset filters)
2. Get available
3. Get with Manual gearbox
4. Get with Automatic gearbox
5. Get Not used (state: new)
6. Get Used
7. Get Newest (from 2020) (checks the current year and substracts 3 from it)
8. Models that contain input
9. Get sorted by horsepower

And only there customer can borrow chosen vehicle

10. Borrow Car
11. Return Car
12. Refuel/Charge rented vehicle
13. Get current fuel/percentage level

### Admin controls

To prevent customer from contacting with database there are 2 admin commands that are not mentioned in the list and will allow to:

1. Remove from db by model with `admin rm`.
2. Create vehicle and add to db with `admin add`.

### Debt system

In the beginning, program asks for balance input apart from the customer's name, but it can be skipped, so the balance by default will be 0
Money are needed for refuel and borrowing function, but if there is not enough -> it will be marked as debt (the current needed amount will be shown in the main menu at the top with red font color) 
So, while debt is present, customer can't borrow any more vehicles (but still can refuel them), also can't return any and leave the salon:)
To pay off the debt, the client needs to add money to his balance, after which it will be written off automatically

### Fuel/battery consuming function

To make refuel system actually useful, after rent every vehicle (that has fuel or battery level) starts to decrease his current level by 3 every 30 seconds
Depending on the rented vehicle the gas prices will vary due to fuel type (petrol, gas, diesel or electricity)

## Made by

Kyrylo Kolot 3ZIG23
