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

    private byte[] bytes = new byte[200];
    private int len;
    private boolean sign = true;

    // Getter, Setter : len
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    // Getter, Setter : Sign
    public boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    // Constructor
    public BigInteger(int num)
    {
        Arrays.fill(this.bytes, (byte) 0);
        int i = 0;
        while(num != 0){
            this.bytes[i++] = (byte) (num % 10);
            num /= 10;
        }
        this.len = i;
    }
  
    public BigInteger(int[] num1)
    {
        Arrays.fill(bytes, (byte) 0);
        for (int i=0; i<num1.length; i++) {
            bytes[i] = (byte) (num1[i]);
        }
        this.len = num1.length + 1;
    }
  
    public BigInteger(String s)
    {
        Arrays.fill(bytes, (byte) 0);
        String trimS = s.trim();
        int trimSLength = trimS.length();
        for (int i=trimSLength-1; i>=0; i--) {
            bytes[trimSLength-1-i] = (byte) (trimS.charAt(i) - '0');
        }
        this.len = trimSLength;

    }

    public BigInteger(String s, Boolean sign)
    {
        Arrays.fill(bytes, (byte) 0);
        String trimS = s.trim();
        int trimSLength = trimS.length();
        for (int i=trimSLength-1; i>=0; i--) {
            bytes[trimSLength-1-i] = (byte) (trimS.charAt(i) - '0');
        }
        this.len = trimSLength;
        this.sign = sign;
    }
  
    // Method : Add BigInteger
    public BigInteger add(BigInteger big)
    {
        int maxLength = (this.len > big.len) ? this.len : big.len;
        int[] result = new int[maxLength+1];
        int sum;
        int carry = 0;

        for (int i=0; i<maxLength; i++) {
            sum = this.bytes[i] + big.bytes[i] + carry;
            carry = sum / 10;
            sum %= 10;
            result[i] = sum;
        }
        result[maxLength] = carry;

        // Set result length, sign
        BigInteger resultBigInteger = new BigInteger(result);
        resultBigInteger.setLen(maxLength + carry);
        resultBigInteger.setSign(this.sign);

        return resultBigInteger;
    }
  
    // Method : Subtract BigInteger
    public BigInteger subtract(BigInteger big, Boolean resultSign)
    {
        int maxLength = this.len;
        int[] result = new int[maxLength];
        int sub;
        int borrow = 0;
        
        Arrays.fill(result, 0);
        for (int i=0; i<maxLength; i++) {
            sub = this.bytes[i] - big.bytes[i] - borrow;
            if (sub < 0) {
                borrow = 1;
                sub += 10;
            } else {
                borrow = 0;
            }
            result[i] = sub;
        }

        for (int i=maxLength-1;i>0;i--){
            if (result[i] == 0) {
                maxLength -= 1;
            } else {
                break;
            }
        } 

        //Set result length, sign
        BigInteger resultBigInteger = new BigInteger(result);
        resultBigInteger.setLen(maxLength);
        resultBigInteger.setSign(resultSign);

        return resultBigInteger;
    }
  
    // Method : Multiply BigInteger
    public BigInteger multiply(BigInteger big)
    {
        int maxLength = this.len + big.len;
        int[] result = new int[maxLength];
        int carry = 0;

        for (int i=0; i<big.len; i++) {
            for (int j=0; j<this.len; j++) {
                result[i+j] += big.bytes[i] * this.bytes[j];
            }
        }

        for (int i=0; i<maxLength; i++) {
            result[i] += carry;
            carry = result[i] / 10;
            result[i] = result[i] % 10;
        }

        for (int i=maxLength-1;i>0;i--){
            if (result[i] == 0) {
                maxLength -= 1;
            } else {
                break;
            }
        } 

        //Set result length, sign
        BigInteger resultBigInteger = new BigInteger(result);
        resultBigInteger.setLen(maxLength);
        resultBigInteger.setSign((this.sign == big.sign) ? true : false);

        return resultBigInteger;
    }
  
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(!sign) {sb.append("-");}
        for (int i=this.len-1; i>=0; i--) {
            sb.append(bytes[i]);
        }

        return sb.toString();
    }

    // Method : compare two bigInteger's abs value
    public boolean isBiggerThan(BigInteger big)
    {
        if (this.len > big.len) {
            return true;
        } else if (this.len < big.len) {
            return false;
        } else {
            for (int i=this.len-1;i>=0;i--) {
                if (this.bytes[i] > big.bytes[i]) {
                    return true;
                } else if (this.bytes[i] < big.bytes[i]) {
                    return false;
                }
            }
        }
        return false;
    }
  
    // Method : split input and evaluate bigInteger value
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

        // BigInteger Instance
        BigInteger bigInteger1 = new BigInteger(bigIntegerStr1, (bigInteger1Sign == '+') ? true : false);
        BigInteger bigInteger2 = new BigInteger(bigIntegerStr2, (bigInteger2Sign == '+') ? true : false);
        BigInteger result;

        // Calculate two bigInteger
        if (operator == '+') {
            if (bigInteger1Sign == bigInteger2Sign) {
                result = bigInteger1.add(bigInteger2);
            } else {
                if (bigInteger1.isBiggerThan(bigInteger2)) {
                    result = bigInteger1.subtract(bigInteger2, bigInteger1.getSign());
                } else {
                    result = bigInteger2.subtract(bigInteger1, bigInteger2.getSign());
                }
            }
        } else if (operator == '-') {
            if (bigInteger1Sign != bigInteger2Sign) {
                result = bigInteger1.add(bigInteger2);
            } else {
                if (bigInteger1.isBiggerThan(bigInteger2)) {
                    result = bigInteger1.subtract(bigInteger2, bigInteger1.getSign());
                } else {
                    result = bigInteger2.subtract(bigInteger1, !bigInteger2.getSign());
                }
            }
        } else {
            result = bigInteger1.multiply(bigInteger2);
        }
        
        //If zero, make sign plus
        if (result.len == 1 && result.bytes[0] == 0) {
            result.sign = true;
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
