import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.spi.DirStateFactory.Result;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    public Byte[] bytes = new Byte[200];
    public int BigInteger_length;

    public int getBigInteger_length() {
        return BigInteger_length;
    }

    public void setBigInteger_length(int bigInteger_length) {
        BigInteger_length = bigInteger_length;
    }

    public BigInteger(int num)
    {
        Arrays.fill(bytes, (byte) 0);
        int i = 0;
        while(num != 0){
            bytes[i++] = (byte) (num % 10);
            num /= 10;
        }
        setBigInteger_length(i);
    }
  
    public BigInteger(int[] num1)
    {
        Arrays.fill(bytes, (byte) 0);
        for (int i=0; i<num1.length; i++) {
            bytes[i] = (byte) (num1[i]);
        }
        setBigInteger_length(num1.length + 1);
    }
  
    public BigInteger(String s)
    {
        Arrays.fill(bytes, (byte) 0);
        String trim_s = s.trim();
        int trim_s_length = trim_s.length();
        for (int i=trim_s_length-1; i>=0; i--) {
            bytes[trim_s_length-1-i] = (byte) (trim_s.charAt(i) - '0');
        }
        setBigInteger_length(trim_s_length);

    }
  
    public BigInteger add(BigInteger big)
    {
        int max_length = (BigInteger_length > big.getBigInteger_length()) ? BigInteger_length : big.getBigInteger_length();
        Byte[] result = new Byte[max_length];
        int num_sum, sum;
        int carry = 0;
        for (int i=0; i < max_length; i++) {
            num_sum = bytes[i] + big.bytes[i];
            carry = num_sum / 10;
            sum = num_sum % 10;
            result[i] = (byte) (sum);
        }

        StringBuilder sb = new StringBuilder();
        if (carry == 1) {
            sb.append(1);
        }
        for (int i=max_length-1; i>=0; i--) {
            sb.append(result[i]);
        }

        BigInteger result_BigInteger = new BigInteger(sb.toString());

        return result_BigInteger;
    }
  
    public BigInteger subtract(BigInteger big)
    {
    }
  
    public BigInteger multiply(BigInteger big)
    {
    }
  
    @Override
    public String toString()
    {
    }
  
    // static BigInteger evaluate(String input) throws IllegalArgumentException
    // {
    //     // implement here
    //     // parse input
    //     // using regex is allowed
  
    //     // One possible implementation
    //     // BigInteger num1 = new BigInteger(arg1);
    //     // BigInteger num2 = new BigInteger(arg2);
    //     // BigInteger result = num1.add(num2);
    //     // return result;
    // }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }

        //test
        BigInteger new1 = new BigInteger("15230");
        BigInteger new2 = new BigInteger("23000");
        BigInteger new3 = new1.add(new2);
    }
  
    // static boolean processInput(String input) throws IllegalArgumentException
    // {
    //     boolean quit = isQuitCmd(input);
  
    //     if (quit)
    //     {
    //         return true;
    //     }
    //     else
    //     {
    //         BigInteger result = evaluate(input);
    //         System.out.println(result.toString());
  
    //         return false;
    //     }
    // }
  
    // static boolean isQuitCmd(String input)
    // {
    //     return input.equalsIgnoreCase(QUIT_COMMAND);
    // }
}
