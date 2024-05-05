import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TMUberUI
{
    public static void main(String[] args)
    {
        TMUberSystemManager tmuber = new TMUberSystemManager();
        Scanner in = new Scanner(System.in);

        while (true)
        {
            System.out.print(">");

            String action = in.nextLine();
            
            if (action.equalsIgnoreCase("QUIT") || action.equalsIgnoreCase("Q"))
            {
                in.close();
                return;
            }
            else if (action.equalsIgnoreCase("USERS"))
            {
                tmuber.listAllUsers();
            }
            else if (action.equalsIgnoreCase("DRIVERS"))
            {
                tmuber.listAllDrivers();
            }
            else if (action.equalsIgnoreCase("REQUESTS"))
            {
                tmuber.listAllRequests();
            }
            else if (action.equalsIgnoreCase("LOADUSERS"))
            {
                //in this block, we want to accomplish one thing in particular which is to append users to both the
                //map and userList of the manager object via the input file
                //may be necessary to put everything within a try block as some actions should not be executed if the ones before it
                //fails and thus based on how try and catch works, this can help ensure this condition.

                System.out.print("User File: ");
                String filename = in.nextLine();

                if (filename.equals("") || !filename.endsWith(".txt"))
                {
                    System.out.println("Invalid File Name\n");
                    continue;
                }

                try
                {
                    //create an arraylist object that stores user references and store the reference of said arraylist in a variable
                    ArrayList<User> users = TMUberRegistered.loadPreregisteredUsers(filename);
                    //very important that we set the user list first as this is the method that throws an exception if there are
                    //are duplicates and we know we don't want to change the map if there duplicates which is what this scenario
                    //guarantees as we will enter one of the catch blocks before setUsers is executed if an exception is thrown
                    tmuber.setUserList(users);
                    //execute set users to get put all the users into the map
                    tmuber.setUsers(users);
                    System.out.println("Users Successfully Loaded\n");
                }
                catch (FileNotFoundException e)
                {
                    System.out.println(filename + " Not Found\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("LOADDRIVERS"))
            {
                System.out.print("Driver File: ");
                String filename = in.nextLine();

                if (filename.equals("") || !filename.endsWith(".txt"))
                {
                    System.out.println("Invalid File Name\n");
                    continue;
                }

                try
                {
                    tmuber.setDrivers(TMUberRegistered.loadPreregisteredDrivers(filename));
                    System.out.println("Drivers Successfully Loaded\n");
                }
                catch (FileNotFoundException e)
                {
                    System.out.println(filename + " Not Found\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("SORTBYDIST"))
            {
                tmuber.sortServiceRequests();
            }
            else if (action.equalsIgnoreCase("REGUSER"))
            {
                //we need to prompt the user (user of the app) for some information about the new user
                //name, address, wallet
                //if any of the above parameters of the new user object given by the user of the app is invalid, we stop
                //prompting and tell the user what is wrong with the parameter they have given and then continue on to
                //the next iteration.
                //If all the parameters have been validated 

                System.out.print("Enter User Name: ");
                String name = in.nextLine();

                if (containDigits(name))
                {
                    System.out.println("Invalid Name: Names Cannot Contain Digits\n");
                    continue;
                }
                else if (name.equals(""))
                {
                    System.out.println("Invalid Name: A Name Has Not Been Provided\n");
                    continue;
                }
                
                System.out.print("Enter User Address: ");
                String address = in.nextLine();

                if (!CityMap.validAddress(address, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter Money in User Wallet: ");
                double wallet;

                try
                {
                    wallet = in.nextDouble();
                }
                catch (RuntimeException e)
                {
                    System.out.println("Cannot be Converted to a Decimal Value\n");
                    continue;
                }

                if (wallet < 0)
                {
                    System.out.println("Invalid Money in Wallet\n");
                    continue;
                }
                
                try
                {
                    tmuber.registerNewUser(name, address, wallet);
                    System.out.println("New User Successfully Registered\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("REGDRIVER"))
            {
                //approximately the same as reguser except we have more parameters this time
                //name, address, car model, licence plate
                //validity of name and address same as for regusers
                //validity of car model is just it isn't the empty string
                //validity of licence plate is just that it isn't empty string

                System.out.print("Enter Driver Name: ");
                String name = in.nextLine();

                if (containDigits(name))
                {
                    System.out.println("Invalid Name: Name Cannot Contain Digits\n");
                    continue;
                }
                else if (name.equals(""))
                {
                    System.out.println("Invalid Name: A Name Has Not Been Provided\n");
                    continue;
                }

                System.out.print("Enter Driver Address: ");
                String address = in.nextLine();

                if (!CityMap.validAddress(address, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter Car Model of Driver's Car: ");
                String carModel = in.nextLine();

                if (carModel.equals(""))
                {
                    System.out.println("A Car Model Has Not Been Provided\n");
                    continue;
                }

                System.out.print("Enter Driver's Licence Plate: ");
                String licencePlate = in.nextLine();

                if (licencePlate.equals(""))
                {
                    System.out.println("A Licence Plate Has Not Been Provided\n");
                    continue;
                }

                try
                {
                    tmuber.registerNewDriver(name, carModel, licencePlate, address);
                    System.out.println("New Driver Successfully Registered\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("DRIVETO"))
            {
                //takes a driver from one address to another, but we only need to know the to address to change the address
                //of the driver in the system
                //prompt the user for the id of the driver we want to change the address
                //check the validity of the id (id contains only digits and is not lower than 7000) and continue if invalid
                //prompt the user for a to address
                //check validity of the address and continue if it is invalid
                //use driveTo method and give the id and the address as arguments

                System.out.print("Driver ID: ");
                String accountId = in.nextLine();

                if (accountId.equals(""))
                {
                    System.out.println("Invalid Account ID: An ID Has Not Been Provided\n");
                    continue;
                }
                else if (!onlyDigits(accountId))
                {
                    System.out.println("Invalid Account ID: ID Must be All Digits\n");
                    continue;
                }
                else if (Integer.parseInt(accountId) < 7000)
                {
                    System.out.println("Invalid Account ID: Driver IDs are 7000 or Greater\n");
                    continue;
                }

                System.out.print("Enter 'to' Address: ");
                String to = in.nextLine();

                if (!CityMap.validAddress(to, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }
                
                try
                {
                    tmuber.driveTo(accountId, to);
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("DIST"))
            {
                System.out.print("Enter First Address: ");
                String addr1 = in.nextLine();

                if (!CityMap.validAddress(addr1, true))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter Second Address: ");
                String addr2 = in.nextLine();

                if (!CityMap.validAddress(addr2, true))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }
                
                System.out.println("The Distance Between " + addr1 + " and " + addr2 + " is " + CityMap.getDistance(addr1, addr2) + " City Blocks\n");
            }
            else if (action.equalsIgnoreCase("REQRIDE"))
            {
                //3 parameters we must prompt user for
                //user account id, from address, to address
                //check validity of all 3, if encounter an invalid, immediately continue
                //use tmuber.requestNewRide(accountId, from, to) to formalize the ride. Put inside try block and catch
                //run time exceptions

                System.out.print("User ID: ");
                String accountId = in.nextLine();

                if (accountId.equals(""))
                {
                    System.out.println("Invalid Account ID: An ID Has Not Been Provided\n");
                    continue;
                }
                else if (!onlyDigits(accountId))
                {
                    System.out.println("Invalid Account ID: ID Must be All Digits\n");
                    continue;
                }
                else if (Integer.parseInt(accountId) < 9000)
                {
                    System.out.println("Invalid Account ID: User IDs are 9000 or Greater\n");
                    continue;
                }

                System.out.print("Enter 'from' Address: ");
                String from = in.nextLine();

                if (!CityMap.validAddress(from, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter 'to' Address: ");
                String to = in.nextLine();

                if (!CityMap.validAddress(to, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                try
                {
                    tmuber.requestNewRide(accountId, from, to);
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("REQDLVY"))
            {
                //ids and addresses are handled same way above
                //in terms of restaurant and food order ids, to ensure validity of these 2
                //we understand that restaurant must not be "" and food order id must not be ""
                System.out.print("User ID: ");
                String accountId = in.nextLine();

                if (accountId.equals(""))
                {
                    System.out.println("Invalid Account ID: An ID Has Not Been Provided\n");
                    continue;
                }
                else if (!onlyDigits(accountId))
                {
                    System.out.println("Invalid Account ID: ID Must be All Digits\n");
                    continue;
                }
                else if (Integer.parseInt(accountId) < 9000)
                {
                    System.out.println("Invalid Account ID: User IDs are 9000 or Greater\n");
                    continue;
                }

                System.out.print("Enter 'from' Address: ");
                String from = in.nextLine();

                if (!CityMap.validAddress(from, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter 'to' Address: ");
                String to = in.nextLine();

                if (!CityMap.validAddress(to, false))
                {
                    System.out.println("Invalid Address\n");
                    continue;
                }

                System.out.print("Enter the Restaurant Name: ");
                String restaurant = in.nextLine();

                if (restaurant.equals(""))
                {
                    System.out.println("No Restaurant Name Provided\n");
                    continue;
                }

                System.out.print("Enter the Food Order ID: ");
                String foodOrderId = in.nextLine();

                if (foodOrderId.equals(""))
                {
                    System.out.println("No Food Order ID Provided\n");
                    continue;
                }

                try
                {
                    tmuber.requestNewDelivery(accountId, from, to, restaurant, foodOrderId);
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("DROPOFF"))
            {
                System.out.print("Enter Request Number: ");
                int requestNum;

                try
                {
                    requestNum = in.nextInt();
                }
                catch (RuntimeException e)
                {
                    System.out.println("Cannot be Converted into an Integer\n");
                    continue;
                }

                try
                {
                    tmuber.dropoff(requestNum);
                    System.out.println("Dropoff Successful\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("CANCELREQ"))
            {
                System.out.print("Enter Request Number: ");
                int requestNum;

                try
                {
                    requestNum = in.nextInt();
                }
                catch (RuntimeException e)
                {
                    System.out.println("Cannot be Converted into an Integer\n");
                    continue;
                }

                try
                {
                    tmuber.cancelRequest(requestNum);
                    System.out.println("Request Successfully Cancelled\n");
                }
                catch (RuntimeException e)
                {
                    System.out.println(e.getMessage() + "\n");
                }
            }
            else if (action.equalsIgnoreCase("SORTBYNAME"))
            {
                tmuber.sortByName();
            }
            else if (action.equalsIgnoreCase("SORTBYWALLET"))
            {
                tmuber.sortByWallet();
            }
            else if (action.equalsIgnoreCase("REVENUE"))
            {
                System.out.printf("Total Revenue: %.2f",tmuber.getRevenue());
                System.out.print("\n\n");
            }
        }
    }

    public static boolean containDigits(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean onlyDigits(String str)
    {
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
}
