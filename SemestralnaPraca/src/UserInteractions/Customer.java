package UserInteractions;

import AbstractClasses.Vehicle;

import java.util.List;

public class Customer {
    private String name;
    public List<Vehicle> allRentedVehicles;
    public Vehicle rentedVehicle;
    private double balance, debt = 0;

    public Customer(String name, double balance){
        this.name = name;
        this.balance = balance;
    }

    public void showBalance(){
        System.out.println("Current balance: " + balance + " Euro");
    }
    public double getBalance(){
        return balance;
    }
    public void addToBalance(double money){
        this.balance += money;
    }
    public void setBalance(double money) {
        this.balance = money;
    }
    public void setDebt(double debt){
        this.debt = debt;
    }

    public void addDebt(double debt){
        this.debt += debt;
    }
    public double getDebt(){
        return debt;
    }

    public String getName(){
        return name;
    }

}
