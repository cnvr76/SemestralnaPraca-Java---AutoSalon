package AbstractClasses;

import UserInteractions.Customer;

public interface BorrowAble {

    boolean borrowItem(Customer customer);
    boolean returnItem();
    boolean isAvailable();

}