
public class Driver extends Person
{
    private String carModel, licencePlate;
    private Status status;

    public static enum Status {DRIVING, AVAILABLE};

    public Driver(String name, String accountId, String carModel, String licencePlate, String address)
    {
        super(name, accountId, 0, address);
        this.carModel = carModel;
        this.licencePlate = licencePlate;
        this.status = Status.AVAILABLE;
    }

    public Status getStatus()
    {
        return status;
    }

    public void switchStatus()
    {
        if (status == Status.DRIVING)
        {
            status = Status.AVAILABLE;
        }
        else
        {
            status = Status.DRIVING;
        }
    }

    public void getPaid(double amount)
    {
        //driver's wallet shall increase after a service request is fulfilled
        setWallet(getWallet() + amount);
    }

    public boolean equals(Person other)
    {
        if (other instanceof Driver)
        {
            Driver otherDriver = (Driver) other;
            return getName().equals(otherDriver.getName()) && licencePlate.equals(otherDriver.licencePlate);
        }
        return false;
    }

    public void display()
    {
        System.out.printf("Id: %-3s Name: %-15s Car Model: %-15s License Plate: %-15s Wallet: %-10.2f Status: %-10s", getAccountID(), getName(), carModel, licencePlate, getWallet(), status);
        System.out.println();
        System.out.print("Address: " + getAddress() + "  Zone: " + CityMap.getZone(getAddress()));
    }
}