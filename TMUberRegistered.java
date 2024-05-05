import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class TMUberRegistered
{
    private static final int FIRST_USER_ID = 9000;
    private static final int FIRST_DRIVER_ID = 7000;
    
    public static String generateUserID(ArrayList<User> list)
    {
        return (list.size() + FIRST_USER_ID) + "";
    }

    public static String generateDriverID(ArrayList<Driver> list)
    {
        return (list.size() + FIRST_DRIVER_ID) + "";
    }

    public static ArrayList<User> loadPreregisteredUsers(String filename) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File(filename));
        ArrayList<User> users = new ArrayList<>();
        String name = null, address = null;
        double wallet;
        int counter = -1;

        while (in.hasNextLine())
        {
            counter++;

            if (counter % 3 == 0)
            {
                name = in.nextLine();
            }
            else if ((counter - 1) % 3 == 0)
            {
                address = in.nextLine();
            }
            else
            {
                wallet = Double.parseDouble(in.nextLine());
                users.add(new User(name, address, generateUserID(users), wallet));
            }
        }

        in.close();

        return users;
    }

    public static ArrayList<Driver> loadPreregisteredDrivers(String filename) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File(filename));
        ArrayList<Driver> drivers = new ArrayList<>();
        String name = null, carModel = null, licencePlate = null, address;
        int counter = -1;

        while (in.hasNextLine())
        {
            counter++;

            if (counter % 4 == 0)
            {
                name = in.nextLine();
            }
            else if ((counter - 1) % 4 == 0)
            {
                carModel = in.nextLine();
            }
            else if ((counter - 2) % 4 == 0)
            {
                licencePlate = in.nextLine();
            }
            else
            {
                address = in.nextLine();
                drivers.add(new Driver(name, generateDriverID(drivers), carModel, licencePlate, address));
            }
        }

        in.close();

        return drivers;
    }
}