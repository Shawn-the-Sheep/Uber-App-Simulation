
public class TMUberDelivery extends TMUberService
{
    private String restaurant, foodOrderID;
    public static final String TYPENAME = "DELIVERY";

    public TMUberDelivery(User user, Driver driver, String from, String to, int distance, double cost, String restaurant, String foodOrderID)
    {
        super(user, driver, from, to, TYPENAME, distance, cost);
        this.restaurant = restaurant;
        this.foodOrderID = foodOrderID;
    }

    public String getRestaurant()
    {
        return restaurant;
    }

    public String toString()
    {
        return "DELIVERY FOR: " + super.toString();
    }

    public String getServiceType()
    {
        return TYPENAME;
    }

    public boolean equals(TMUberService otherService)
    {
        if (otherService instanceof TMUberDelivery)
        {
            TMUberDelivery otherDelivery = (TMUberDelivery) otherService;
            return super.equals(otherDelivery) && restaurant.equals(otherDelivery.restaurant) && foodOrderID.equals(otherDelivery.foodOrderID);
        }

        return false;
    }

    public void display()
    {
        super.display();

        System.out.printf("\nRestaurant: %-9s Food Order #: %-3s", restaurant, foodOrderID); 
    }
}
