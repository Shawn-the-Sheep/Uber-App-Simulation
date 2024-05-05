
public abstract class TMUberService implements Comparable<TMUberService>
{
    private User user;
    private Driver driver;
    private String from, to;
    private String type;
    private int distance;
    private double cost;

    public TMUberService(User user, Driver driver, String from, String to, String type, int distance, double cost)
    {
        this.user = user;
        this.driver = driver;
        this.from = from;
        this.to = to;
        this.type = type;
        this.distance = distance;
        this.cost = cost;
    }

    public Driver getDriver()
    {
        return driver;
    }

    public User getUser()
    {
        return user;
    }

    public double getCost()
    {
        return cost;
    }

    public String getTo()
    {
        return to;
    }

    public abstract String getServiceType();

    public boolean equals(TMUberService otherService)
    {
        return user.equals(otherService.user);
    }

    public String toString()
    {
        return user.getName() + "  From: " + from + "  To: " + to;
    }

    public void display()
    {
        System.out.printf("\nType: %-9s From: %-15s To: %-15s", type, from, to);
        System.out.print("\nUser: ");
        user.display();
        System.out.print("\nDriver: ");
        driver.display();
    }

    public int compareTo(TMUberService otherService)
    {
        return distance - otherService.distance;
    }
}