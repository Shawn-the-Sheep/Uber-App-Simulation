import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class TMUberSystemManager
{
    private ArrayList<User> userList;
    private ArrayList<Driver> drivers;
    private Map<String, User> users;
    private ArrayList<TMUberService> serviceRequests;
    private double totalRevenue;

    private static final double RIDERATE = 1.5;
    private static final double DELIVERYRATE = 1.2;
    private static final double PAYRATE = 0.1;

    public TMUberSystemManager()
    {
        userList = new ArrayList<>();
        drivers = new ArrayList<>();
        users = new HashMap<>();
        serviceRequests = new ArrayList<>();
        totalRevenue = 0;
    }

    public void listAllUsers()
    {
        for (int i = 0; i < userList.size(); i++)
        {
            System.out.print((i + 1) + ". ");
            userList.get(i).display();
            System.out.println("\n");
        }
    }

    public void listAllDrivers()
    {
        for (int i = 0; i < drivers.size(); i++)
        {
            System.out.print((i + 1) + ". ");
            drivers.get(i).display();
            System.out.println("\n");
        }
    }

    public void listAllRequests()
    {
        for (int i = 0; i < serviceRequests.size(); i++)
        {
            System.out.printf("%-2s. ", i + 1);
            serviceRequests.get(i).display();
            System.out.println();
        }
        System.out.println();
    }

    public void setUserList(ArrayList<User> userList)
    {
        for (int i = 0; i < userList.size(); i++)
        {
            for (int j = i + 1; j < userList.size(); j++)
            {
                if (userList.get(i).equals(userList.get(j)))
                {
                    throw new DuplicateUsersException("Load Users Failed: Duplicate Users in File");
                }
            }
        }

        this.userList = userList;
    }

    public void setDrivers(ArrayList<Driver> drivers)
    {
        for (int i = 0; i < drivers.size(); i++)
        {
            for (int j = i + 1; j < drivers.size(); j++)
            {
                if (drivers.get(i).equals(drivers.get(j)))
                {
                    throw new DuplicateDriversException("Load Drivers Failed: Duplicate Drivers in File");
                }
            }
        }

        this.drivers = drivers;
    }
    
    public void setUsers(ArrayList<User> userList)
    {
        for (int i = 0; i < userList.size(); i++)
        {
            User current_user = userList.get(i);
            users.put(current_user.getAccountID(), current_user);
        }
    }

    private User getUser(String accountId)
    {
        return users.get(accountId);
    }

    private Driver getAvailableDriver(int zone)
    {
        for (Driver driver : drivers)
        {
            if (driver.getStatus() == Driver.Status.AVAILABLE && CityMap.getZone(driver.getAddress()) == zone)
            {
                return driver;
            }
        }
        return null;
    }

    private Driver getDriver(String accountId)
    {
        for (Driver driver : drivers)
        {
            if (accountId.equals(driver.getAccountID()))
            {
                return driver;
            }
        }
        return null;
    }

    private static double getRideCost(int distance)
    {
        return distance * RIDERATE;
    }

    private static double getDeliveryCost(int distance)
    {
        return distance * DELIVERYRATE;
    }

    public void sortServiceRequests()
    {
        Collections.sort(serviceRequests);
        listAllRequests();
    }

    public void registerNewUser(String name, String address, double wallet)
    {
        String accountId = TMUberRegistered.generateUserID(userList);
        User new_user = new User(name, address, accountId, wallet);

        for (User user : userList)
        {
            if (user.equals(new_user))
            {
                throw new DuplicateUsersException(new_user);
            }
        }

        userList.add(new_user);
        users.put(accountId, new_user);
    }

    public void registerNewDriver(String name, String carModel, String licencePlate, String address)
    {
        Driver new_driver = new Driver(name, TMUberRegistered.generateDriverID(drivers), carModel, licencePlate, address);

        for (Driver driver : drivers)
        {
            if (driver.equals(new_driver))
            {
                throw new DuplicateDriversException(new_driver);
            }
        }

        drivers.add(new_driver);
    }

    public void driveTo(String driverId, String to)
    {
        Driver the_driver = getDriver(driverId);

        if (the_driver == null)
        {
            throw new DriverNotFoundException(driverId);
        }

        if (the_driver.getStatus() == Driver.Status.DRIVING)
        {
            throw new InvalidDriverException(the_driver);
        }

        int previousZone = CityMap.getZone(the_driver.getAddress());
        int newZone = CityMap.getZone(to);

        the_driver.setAddress(to);

        if (previousZone != newZone)
        {
            System.out.println("Driver " + the_driver.getName() + " with Account ID " + driverId + " Now in zone " + newZone);
            System.out.println();
        }
    }

    public void requestNewRide(String userAccountId, String from, String to)
    {
        /*In this method, we want to create a ride request and potentially add it to the array of service requests if
         * we find that the user doesn't already have a ride request, the userAccountId does correspond to a user, there 
         * exists an available driver that can be associated to this new ride request, the distance is sufficient, and the
         * user's wallet can sufficiently pay for the ride. We must remember to switch the availability status of the driver 
         * upon formal acceptance of the newly created ride request. In addition, the driver would've driven to the from address
         * upon recognition of the ride request. Remember to also increment the rides instance variable of the user and print the
         * ride request as well.
        */

        double totalCost = 0;

        User the_user = getUser(userAccountId);

        if (the_user == null)
        {
            throw new UserNotFoundException(userAccountId);
        }

        int distance = CityMap.getDistance(from, to);
        int zone = CityMap.getZone(from);

        if (distance <= 1)
        {
            throw new InsufficientDistanceException("Insufficient Travel Distance");
        }

        for (TMUberService serviceRequest : serviceRequests)
        {
            if (serviceRequest.getServiceType().equals(TMUberRide.TYPENAME))
            {
                if (serviceRequest.getUser().equals(the_user))
                {
                    throw new InvalidUserException(the_user);
                }
            }
            else if (serviceRequest.getUser().equals(the_user))
            {
                totalCost += serviceRequest.getCost();
            }
        }
        
        double costOfRide = getRideCost(distance);
        totalCost += costOfRide;

        if (totalCost > the_user.getWallet())
        {
            throw new InsufficientFundsException("Insufficient Funds in User Wallet");
        }

        Driver the_driver = getAvailableDriver(zone);

        if (the_driver == null)
        {
            throw new NoAvailableDriverException("No Available Driver in zone " + zone);
        }

        driveTo(the_driver.getAccountID(), from);
        the_driver.switchStatus();
        TMUberRide new_ride = new TMUberRide(the_user, the_driver, from, to, distance, costOfRide);
        serviceRequests.add(new_ride);
        System.out.println(new_ride + "\n");
        the_user.incrementRide();
    }

    public void requestNewDelivery(String userAccountId, String from, String to, String restaurant, String foodOrderId)
    {
        /*A method for simulating the actions that take place when a particular user requests a delivery to be made. The point is
         * that an object representation of the delivery request is created and is appended to the arraylist of service requests.
         * First we must make sure the user account id is associated with a user in the system. Then we must ensure that the user
         * does not already have a delivery request in the system from the same restaurant and with the same food order id. Other
         * requirements after this include sufficient distance, availability of a driver in the zone of the from address, sufficient
         * funds in user wallet to cover this new request as well as other current requests of the user. We must remember that the
         * acceptance of a new delivery request requires that the driver will drive to the designated location, the deliveries instance
         * variable of the user is incremented. The delivery request is printed and the status of the driver switches to Driving.
        */

        double totalCost = 0;

        User the_user = getUser(userAccountId);

        if (the_user == null)
        {
            throw new UserNotFoundException(userAccountId);
        }

        int distance = CityMap.getDistance(from, to);
        int zone = CityMap.getZone(from);

        if (distance <= 1)
        {
            throw new InsufficientDistanceException("Insufficient Travel Distance");
        }

        Driver the_driver = getAvailableDriver(zone);

        if (the_driver == null)
        {
            throw new NoAvailableDriverException("No Available Driver in zone " + zone);
        }

        double costOfDelivery = getDeliveryCost(distance);

        TMUberDelivery currentDelivery = new TMUberDelivery(the_user, the_driver, from, to, distance, costOfDelivery, restaurant, foodOrderId);
        
        for (TMUberService serviceRequest : serviceRequests)
        {
            if (currentDelivery.equals(serviceRequest))
            {
                throw new InvalidUserException(the_user, restaurant);
            }

            if (the_user.equals(serviceRequest.getUser()))
            {
                totalCost += serviceRequest.getCost();
            }
        }

        totalCost += costOfDelivery;

        if (totalCost > the_user.getWallet())
        {
            throw new InsufficientFundsException("Insufficient Funds in User Wallet");
        }

        driveTo(the_driver.getAccountID(), from);
        the_driver.switchStatus();
        serviceRequests.add(currentDelivery);
        System.out.println(currentDelivery + "\n");
        the_user.incrementDelivery();
    }

    public void dropoff(int requestNum)
    {
        /*this method simulates the actions that takes place during a dropoff. First we want to ensure that the request number 
         * is valid. Then we want to remove the request for the array of requests and simultaneously store the request in a variable.
         * Using this variable, we can retrieve the driver and user to simulate these actions. First, the driver shall drive to the
         * dropoff location which is the to address of the request. Next, the given the cost of the request, the user will have their
         * wallet decrease by the cost. The revenue of the system shall be increased by (1 - PAYRATE) times this cost. The driver
         * will have their wallet increase by PAYRATE * the cost. The driver's status shall be switched to AVAILABLE. 
        */

        if (requestNum < 1 || requestNum > serviceRequests.size())
        {
            throw new InvalidRequestNumberException("Invalid Request Number");
        }

        TMUberService currentRequest = serviceRequests.remove(requestNum - 1);
        User currentUser = currentRequest.getUser();
        Driver currentDriver = currentRequest.getDriver();
        currentDriver.setAddress(currentRequest.getTo());
        double cost = currentRequest.getCost();
        currentUser.payForService(cost);
        currentDriver.getPaid(PAYRATE * cost);
        totalRevenue += (1 - PAYRATE) * cost;
        currentDriver.switchStatus();
    }

    public void cancelRequest(int requestNum)
    {
        /*In this method, we simulate the actions that take place when a request gets canceled. First we ensure that the request
         * number indicating which request to be canceled is valid. Next, we execute the following three actions in order. We
         * remove the service request from the array of service requests. Next we switch the status of the driver from driving
         * to available. Finally, we decrement the rides or deliveries of the user depending on which one the request is
         */

        if (requestNum < 1 || requestNum > serviceRequests.size())
        {
            throw new InvalidRequestNumberException("Invalid Request Number");
        }

        TMUberService currentRequest = serviceRequests.remove(requestNum - 1);
        User currentUser = currentRequest.getUser();
        currentRequest.getDriver().switchStatus();
        
        if (currentRequest.getServiceType().equals(TMUberRide.TYPENAME))
        {
            currentUser.decrementRide();
        }
        else
        {
            currentUser.decrementDelivery();
        }
    }

    public void sortByName()
    {
        Collections.sort(userList, new NameComparator());
        listAllUsers();
    }

    public void sortByWallet()
    {
        Collections.sort(userList, new WalletComparator());
        listAllUsers();
    }

    public double getRevenue()
    {
        return totalRevenue;
    }

    private class NameComparator implements Comparator<User>
    {
        public int compare(User a, User b)
        {
            return a.getName().compareTo(b.getName());
        }
    }

    private class WalletComparator implements Comparator<User>
    {
        public int compare(User a, User b)
        {
            if (a.getWallet() > b.getWallet())
            {
                return 1;
            }
            else if (a.getWallet() < b.getWallet())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}

class DuplicatePersonsException extends RuntimeException
{
    public DuplicatePersonsException() {}

    public DuplicatePersonsException(String message)
    {
        super(message);
    }
}

class DuplicateUsersException extends DuplicatePersonsException
{
    public DuplicateUsersException() {}

    public DuplicateUsersException(String message)
    {
        super(message);
    }

    public DuplicateUsersException(User user)
    {
        super("Reguser Failed: " + user.getName() + " Already Exists in the System");
    }
}

class DuplicateDriversException extends DuplicatePersonsException
{
    public DuplicateDriversException() {}

    public DuplicateDriversException(String message)
    {
        super(message);
    }

    public DuplicateDriversException(Driver driver)
    {
        super("Regdriver Failed: " + driver.getName() + " Already Exists in the System");
    }
}

class InsufficientDistanceException extends IllegalArgumentException
{
    public InsufficientDistanceException() {}

    public InsufficientDistanceException(String message)
    {
        super(message);
    }
}

class NoAvailableDriverException extends RuntimeException
{
    public NoAvailableDriverException() {}

    public NoAvailableDriverException(String message)
    {
        super(message);
    }
}

class PersonNotFoundException extends IllegalArgumentException
{
    private String actualMessage;

    public PersonNotFoundException()
    {
        actualMessage = "";
    }

    public PersonNotFoundException(String id)
    {
        actualMessage = " Not Found with ID: " + id;
    }

    public void clarifyMessage(String clarification)
    {
        actualMessage = clarification + actualMessage;
    }

    public String getMessage()
    {
        return actualMessage;
    }
}

class UserNotFoundException extends PersonNotFoundException
{
    private static final String CLARIFICATION = "User";

    public UserNotFoundException() {}

    public UserNotFoundException(String id)
    {
        super(id);
        clarifyMessage(CLARIFICATION);
    }
}

class DriverNotFoundException extends PersonNotFoundException
{
    private static final String CLARIFICATION = "Driver";

    public DriverNotFoundException() {}

    public DriverNotFoundException(String id)
    {
        super(id);
        clarifyMessage(CLARIFICATION);
    }
}

class InvalidPersonException extends IllegalArgumentException
{
    private String actualMessage;

    public InvalidPersonException()
    {
        actualMessage = "";
    }

    public InvalidPersonException(Person person)
    {
        actualMessage = person.getName() + " with Account ID " + person.getAccountID();
    }

    public void clarifyMessage(String clarification)
    {
        actualMessage += clarification;
    }

    public String getMessage()
    {
        return actualMessage;
    }
}

class InvalidUserException extends InvalidPersonException
{
    public InvalidUserException() {}

    public InvalidUserException(User user)
    {
        super(user);
        clarifyMessage(" Already has a Ride Request");
    }

    public InvalidUserException(User user, String restaurant)
    {
        super(user);
        clarifyMessage(" Already has a Delivery Request at " + restaurant + " with this Order ID");
    }
}

class InvalidDriverException extends InvalidPersonException
{
    public InvalidDriverException() {}

    public InvalidDriverException(Driver driver)
    {
        super(driver);
        clarifyMessage(" is Currently Driving and not Available");
    }
}

class InsufficientFundsException extends RuntimeException
{
    public InsufficientFundsException() {}

    public InsufficientFundsException(String message)
    {
        super(message);
    }
}

class InvalidRequestNumberException extends IllegalArgumentException
{
    public InvalidRequestNumberException() {}

    public InvalidRequestNumberException(String message)
    {
        super(message);
    }
}

