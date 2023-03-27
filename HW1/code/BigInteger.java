import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\s*([-+])?\\s*(\\d+)\\s*([+\\-*])\\s*(?:([-+])?\\s*(\\d+))?\\s*");

    public byte[] bytes = new byte[200];
    public int BigIntegerLength;
    public boolean sign = true;

    public BigInteger(int num)
    {
        Arrays.fill(this.bytes, (byte) 0);
        int i = 0;
        while(num != 0){
            this.bytes[i++] = (byte) (num % 10);
            num /= 10;
        }
        this.BigIntegerLength = i;
    }
  
    public BigInteger(int[] num1)
    {
        Arrays.fill(bytes, (byte) 0);
        for (int i=0; i<num1.length; i++) {
            bytes[i] = (byte) (num1[i]);
        }
        this.BigIntegerLength = num1.length + 1;
    }
  
    public BigInteger(String s)
    {
        Arrays.fill(bytes, (byte) 0);
        String trim_s = s.trim();
        int trim_s_length = trim_s.length();
        for (int i=trim_s_length-1; i>=0; i--) {
            bytes[trim_s_length-1-i] = (byte) (trim_s.charAt(i) - '0');
        }
        this.BigIntegerLength = trim_s_length;

    }

    public BigInteger(String s, Boolean sign)
    {
        Arrays.fill(bytes, (byte) 0);
        String trim_s = s.trim();
        int trim_s_length = trim_s.length();
        for (int i=trim_s_length-1; i>=0; i--) {
            bytes[trim_s_length-1-i] = (byte) (trim_s.charAt(i) - '0');
        }
        this.BigIntegerLength = trim_s_length;
        this.sign = sign;
    }
  
    public BigInteger add(BigInteger big)
    {
        int max_length = (this.BigIntegerLength > big.BigIntegerLength) ? this.BigIntegerLength : big.BigIntegerLength;
        int[] result = new int[max_length+1];
        int numSum, sum;
        int carry = 0;

        for (int i=0; i<max_length; i++) {
            numSum = this.bytes[i] + big.bytes[i] + carry;
            carry = numSum / 10;
            sum = numSum % 10;
            result[i] = sum;
        }
        result[max_length] = carry;

        BigInteger result_BigInteger = new BigInteger(result);
        result_BigInteger.BigIntegerLength = max_length + carry;
        result_BigInteger.sign = this.sign;

        return result_BigInteger;
    }
  
    public BigInteger subtract(BigInteger big)
    {
        int max_length = this.BigIntegerLength;
        int[] result = new int[max_length];
        Arrays.fill(result, 0);
        int sub;
        int borrow = 0;

        for (int i=0; i<max_length; i++) {
            sub = this.bytes[i] - big.bytes[i] - borrow;
            if (sub < 0) {
                borrow = 1;
                sub += 10;
            }
            result[i] = sub;
        }

        BigInteger result_BigInteger = new BigInteger(result);
        if (result[max_length-1] == 0) {
            result_BigInteger.BigIntegerLength = max_length - 1;
        } else {
            result_BigInteger.BigIntegerLength = max_length;
        }
        result_BigInteger.sign = this.sign;

        return result_BigInteger;
    }
  
    public BigInteger multiply(BigInteger big)
    {
        int max_length = this.BigIntegerLength + big.BigIntegerLength;
        int[] result = new int[max_length];
        int carry = 0;

        for (int i=0; i<big.BigIntegerLength; i++) {
            for (int j=0; j<this.BigIntegerLength; j++) {
                result[i+j] += big.bytes[i] * this.bytes[j];
            }
        }

        for (int i=0; i<max_length; i++) {
            result[i] += carry;
            carry = result[i] / 10;
            result[i] = result[i] % 10;
        }

        BigInteger result_BigInteger = new BigInteger(result);
        if (result[max_length-1] == 0) {
            result_BigInteger.BigIntegerLength = max_length - 1;
        } else {
            result_BigInteger.BigIntegerLength = max_length;
        }
        result_BigInteger.sign = (this.sign == big.sign) ? true : false;
        return result_BigInteger;
    }
  
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(!sign) {sb.append("-");}
        for (int i=this.BigIntegerLength-1; i>=0; i--) {
            sb.append(bytes[i]);
        }

        return sb.toString();
    }

    public boolean isBiggerThan(BigInteger big)
    {
        if (this.BigIntegerLength > big.BigIntegerLength) {
            return true;
        } else if (this.BigIntegerLength < big.BigIntegerLength) {
            return false;
        } else {
            for (int i=this.BigIntegerLength-1;i>=0;i--) {
                if (this.bytes[i] > big.bytes[i]) {
                    return true;
                } else if (this.bytes[i] < big.bytes[i]) {
                    return false;
                }
            }
        }
        return false;
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        //Init value
        char bigInteger1Sign = '+';
        String bigIntegerStr1 = "";
        char operator = '+';
        char bigInteger2Sign = '+';
        String bigIntegerStr2 = "";

        //Using regex to matcher
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);

        //Matching sign, number, operator
        if (matcher.matches()) {
            if (matcher.group(1) != null) {
                bigInteger1Sign = matcher.group(1).charAt(0);
            }     
            bigIntegerStr1 = matcher.group(2);
            operator = matcher.group(3).charAt(0);
            if (matcher.group(4) != null) {
                bigInteger2Sign = matcher.group(4).charAt(0);
            }
            bigIntegerStr2 = matcher.group(5);
        }

        BigInteger bigInteger1 = new BigInteger(bigIntegerStr1, (bigInteger1Sign == '+') ? true : false);
        BigInteger bigInteger2 = new BigInteger(bigIntegerStr2, (bigInteger2Sign == '+') ? true : false);
        BigInteger result;

        if (operator == '+') {
            if (bigInteger1Sign == bigInteger2Sign) {
                result = bigInteger1.add(bigInteger2);
            } else {
                if (bigInteger1.isBiggerThan(bigInteger2)) {
                    result = bigInteger1.subtract(bigInteger2);
                } else {
                    result = bigInteger2.subtract(bigInteger1);
                }
            }
        } else if (operator == '-') {
            if (bigInteger1Sign != bigInteger2Sign) {
                result = bigInteger1.add(bigInteger2);
            } else {
                if (bigInteger1.isBiggerThan(bigInteger2)) {
                    result = bigInteger1.subtract(bigInteger2);
                } else {
                    result = bigInteger2.subtract(bigInteger1);
                }
            }
        } else {
            result = bigInteger1.multiply(bigInteger2);
        }
  
        return result;
    }
  
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
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
