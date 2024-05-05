As stated in the About section, this project was about coding up an application that manages the actions and states of participants in an uber-like system. However, it must be noted that the application is not meant
to closely resemble the exact nature and functionality of the real uber app and instead simulates the processes that happen in a hypothetical uber system in a parallel universe. The UI supports 19 commands which
are

1. QUIT - quit the app
2. USERS - display all the users of uber
3. DRIVERS - displays all the drivers of uber
4. REQUESTS - displays all the concurrent service requests
5. LOADUSERS - load some preregistered users from a file (Note: can only be used once)
6. LOADDRIVERS - load some preregistered drivers from a file (Note: can only be used once)
7. SORTBYDIST - sorts the arraylist storing the service requests (keeping the change permanent) and displaying all the requests
8. REGUSER - registers a new user into the system given that the user doesn't already exist in the system
9. REGDRIVER - registers a new driver into the system given that the driver doesn't already exist in the system
10. DRIVETO - through this command, we are able to note which drivers are at which addresses
11. DIST - displays the distance in city blocks between two addresses
12. REQRIDE - we formalize a ride request by a user through this command
13. REQDLVY - we formalize a delivery request by a user through this command
14. DROPOFF - we simulate an in real life dropoff through this command
15. CANCELREQ - a request gets cancelled
16. SORTBYNAME - an arraylist holding user references gets sorted by name and then displayed (keeping the change permanent)
17. SORTBYWALLET - an arraylist holding user references gets sorted by wallet and then displayed (keeping the change permanent)
18. REVENUE - displays the total revenue earned from the company's perspective

We also note that for loadusers and loaddrivers, we are calling the methods in TMUberRegistered loadpreregistered users and loadpreregistered drivers which basically creates user and driver objects based on
information provided in the text file which the user of the app provides. These text files must be of a certain form. An example of the correct form would be in the users.txt and drivers.txt that are provided. For the loading of users, each 3 lines basically corresponds to one user where the first line is the name of the user, the second line is the address, and the third line is the wallet. The drivers file is of the same general format except I believe every 4 lines corresponds to a driver.
