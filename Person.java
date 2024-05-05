
public abstract class Person
{
    private String name, accountId, address;
    private double wallet;

    public Person(String name, String accountId, double wallet, String address)
    {
        this.name = name;
        this.wallet = wallet;
        this.accountId = accountId;
        this.address = address;
    }

    //note that some of these setters may not be useful 
    public double getWallet()
    {
        return wallet;
    }

    public String getName()
    {
        return name;
    }

    public String getAccountID()
    {
        return accountId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String newAddress)
    {
        address = newAddress;
    }

    protected void setWallet(double amount)
    {
        //wallet is set to amount
        wallet = amount;
    }

    public abstract boolean equals(Person otherPerson);
}