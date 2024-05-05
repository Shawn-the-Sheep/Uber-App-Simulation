
public class TMUberRide extends TMUberService
{
    public static final String TYPENAME = "RIDE";

    public TMUberRide(User user, Driver driver, String from, String to, int distance, double cost)
    {
        super(user, driver, from, to, TYPENAME, distance, cost);
    }

    public String getServiceType()
    {
        return TYPENAME;
    }

    public String toString()
    {
        return "RIDE FOR: " + super.toString();
    }
}