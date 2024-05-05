//Container class for housing methods that will pertain to checking the validity of addresses, determining distances and so on

public class CityMap
{

    private static final String[] SUFFIX = {"st", "nd", "rd"};

    private static boolean allDigits(String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            if (!Character.isDigit(s.charAt(i)))
            {
                return false;
            }
        }

        return true;
    }

    private static boolean correctSuffix(int num, String suffix)
    {
        if (num <= 3)
        {
            return suffix.equals(SUFFIX[num - 1]);
        }
        return suffix.equals("th");
    }

    private static int[] getCityBlock(String address)
    {
        int[] cityBlock = new int[2];

        String ending = address.substring(7, address.length());
        Integer avenue = null, street = null;

        if (ending.equalsIgnoreCase("street"))
        {
            avenue = Integer.parseInt("" + address.charAt(0));
            street = Integer.parseInt("" + address.charAt(3));
        }
        else
        {
            street = Integer.parseInt("" + address.charAt(0));
            avenue = Integer.parseInt("" + address.charAt(3));
        }

        cityBlock[0] = street;
        cityBlock[1] = avenue;

        return cityBlock;
    }

    public static boolean validAddress(String address, boolean ignoreCase)
    {
        if (address.length() != 13)
        {
            return false;
        }
        String[] valid_endings = {"avenue", "Avenue", "street", "Street"};
        String digit_str = address.substring(0, 2) + address.charAt(3);
        String ending = address.substring(7, address.length());
        boolean valid_nums = allDigits(digit_str) && Integer.parseInt("" + address.charAt(0)) != 0 && Integer.parseInt("" + address.charAt(3)) != 0;
        boolean valid_spaces = address.charAt(2) == ' ' && address.charAt(6) == ' ';
        boolean valid_ending = false;

        if (ignoreCase)
        {
            for (int i = 0; i < valid_endings.length; i++)
            {
                if (ending.equals(valid_endings[i]))
                {
                    valid_ending = true;
                }
            }
        }
        else
        {
            valid_ending = ending.equals("Avenue") || ending.equals("Street");
        }

        return valid_nums && valid_spaces && correctSuffix(Integer.parseInt("" + address.charAt(3)), address.substring(4, 6)) && valid_ending;
    }

    public static int getDistance(String from, String to)
    {
        int[] fromBlock = getCityBlock(from);
        int[] toBlock = getCityBlock(to);

        return Math.abs(fromBlock[0] - toBlock[0]) + Math.abs(fromBlock[1] - toBlock[1]);
    }

    public static int getZone(String address)
    {
        int[] cityBlock = getCityBlock(address);

        int street = cityBlock[0];
        int avenue = cityBlock[1];

        if (street >= 6 && avenue <= 5)
        {
            return 0;
        }
        else if (street >= 6 && avenue >= 6)
        {
            return 1;
        }
        else if (street <= 5 && avenue >= 6)
        {
            return 2;
        }
        else
        {
            return 3;
        }
    }
}
