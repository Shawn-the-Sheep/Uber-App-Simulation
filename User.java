
public class User extends Person
{
    private int deliveries, rides;

    public User(String name, String address, String accountId, double wallet)
    {
        super(name, accountId, wallet, address);
        this.deliveries = 0;
        this.rides = 0;
    }

    public void payForService(double amount)
    {
        //amount is amount to be deducted from wallet
        setWallet(getWallet() - amount);
    }

    public void display()
    {
        System.out.printf("Id: %-5s Name: %-15s Address: %-15s Wallet: %2.2f", getAccountID(), getName(), getAddress(), getWallet());
        System.out.println();
        System.out.print("Total Number of Rides: " + rides + "  Total Number of Deliveries: " + deliveries);
    }

    public void incrementRide()
    {
        rides++;
    }

    public void decrementRide()
    {
        rides--;
    }

    public void incrementDelivery()
    {
        deliveries++;
    }

    public void decrementDelivery()
    {
        deliveries--;
    }

    public boolean equals(Person person)
    {
        if (person instanceof User)
        {
            User otherUser = (User) person;
            return getAddress().equals(otherUser.getAddress()) && getName().equals(otherUser.getName());
        }
        return false;
    }
}