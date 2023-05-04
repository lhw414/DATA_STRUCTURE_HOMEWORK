import java.io.*;
import java.util.Stack;

public class CalculatorTest {

  // Main Method
  public static void main(String args[]) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      try {
        String input = br.readLine();
        if (input.compareTo("q") == 0) break;

        command(input);
      } catch (Exception e) { // when error occured, print "ERROR"
        System.out.println("ERROR");
      }
    }
  }

  // Method : convert infix to postfix, and evaluate postfix expression
  private static void command(String input) {
    String postfixExpression = infixToPostfix(input);
    long sum = evaluatePostfix(postfixExpression);
    System.out.println(postfixExpression);
    System.out.println(sum);
  }

  // Method : check character is operator
  private static boolean isOperator(char c) {
    return (
      c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^'
    );
  }

  // Method : get precdence of operator
  private static int getPrecedence(char c) {
    if (c == '^') {
      return 4;
    } else if (c == '~') {
      return 3;
    } else if (c == '*' || c == '/' || c == '%') {
      return 2;
    } else if (c == '+' || c == '-') {
      return 1;
    } else {
      return -1; // Invalid operator
    }
  }

  /** Reference start ~
   * ! REFERENCE : GPT - implement infix to postfix method using chat gpt except error checking
   *
   * @reference content
   *   - Get skeleton code from GPT.
   *   - Get basic operation logic :
   *       using [+ / % + - ( )]'s precedence, append operator to postfix string
   *       digits control
   *       pop all operator at end of infix
   * ----------------------------------------------------------
   * @param    infix       an infix expression in string
   * @return   postfix     a postfix expression in string
   */
  private static String infixToPostfix(String infix) {
    Stack<Character> stack = new Stack<>();
    Stack<Integer> countAvgNum = new Stack<>();
    StringBuilder postfix = new StringBuilder();
    Stack<Boolean> avgModeStack = new Stack<>(); // Check current parenthesis is avg mode or calc mode
    boolean previousIsOperator = true; // Check previous is operator
    int parenthesisDepth = 0; // Check current parenthesis depth

    for (int i = 0; i < infix.length(); i++) {
      char c = infix.charAt(i);
      // If the character is an operand, add it to the postfix expression with a space (implemented by GPT)
      if (Character.isDigit(c)) {
        if (!previousIsOperator) {
          throw new ArithmeticException();
        }
        // If the current character is a digit, keep reading until a non-digit character is found
        StringBuilder operand = new StringBuilder();
        while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
          operand.append(infix.charAt(i));
          i++;
        }
        postfix.append(operand).append(' ');
        i--; // Decrement i to compensate for the extra increment in the loop
        previousIsOperator = false;
      }
      // If the character is an opening parenthesis, push it onto the stack
      else if (c == '(') {
        stack.push(c);
        avgModeStack.push(false);
        previousIsOperator = true;
        parenthesisDepth += 1;
      }
      // If the character is a closing parenthesis, pop operators from the stack and add them to the postfix expression
      // until an opening parenthesis is reached (implemented by GPT except avgMode)
      else if (c == ')') {
        if (previousIsOperator) {
          throw new ArithmeticException();
        }
        // When avgMode
        if (avgModeStack.peek()) {
          while (!stack.isEmpty() && stack.peek() != '(') {
            postfix.append(stack.pop()).append(' ');
          }
          if (!stack.isEmpty() && stack.peek() != '(') {
            throw new ArithmeticException();
          } else {
            stack.pop(); // Remove the opening parenthesis from the stack
          }
          postfix
            .append(countAvgNum.pop())
            .append(' ')
            .append("avg")
            .append(' ');
          avgModeStack.pop();
          parenthesisDepth -= 1;
          continue;
        }
        // When not avgMode
        while (!stack.isEmpty() && stack.peek() != '(') {
          postfix.append(stack.pop()).append(' ');
        }
        if (!stack.isEmpty() && stack.peek() != '(') {
          throw new ArithmeticException();
        } else {
          parenthesisDepth -= 1;
          avgModeStack.pop();
          stack.pop(); // Remove the opening parenthesis from the stack
        }
      }
      // If the operator is avg(','), count the number of expression and pop previous expression's operators
      else if (c == ',') {
        if (!avgModeStack.peek()) {
          countAvgNum.push(1);
        }
        if (parenthesisDepth <= 0) {
          throw new ArithmeticException();
        }
        avgModeStack.pop();
        avgModeStack.push(true);
        if (previousIsOperator) {
          throw new ArithmeticException();
        }
        while (!stack.isEmpty() && stack.peek() != '(') {
          postfix.append(stack.pop()).append(' ');
        }
        previousIsOperator = true;
        countAvgNum.push(countAvgNum.pop() + 1); // Increment avg operands number
      }
      // If the character is an operator, pop operators from the stack and add them to the postfix expression
      //until an operator with lower precedence is reached, then push the current operator onto the stack (implemented by GPT except unary - and ^ operation)
      else if (isOperator(c)) {
        if (c == '-') {
          if (i == 0 || previousIsOperator) { // Check unary -
            stack.push('~');
            continue;
          }
        }
        while (
          !stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())
        ) {
          if (c == '^' && stack.peek() == '^') { // If ^ is consecutive, break.
            break;
          }
          postfix.append(stack.pop()).append(' ');
        }
        previousIsOperator = true;
        stack.push(c);
      } else if (c != ' ' && c != '\t') { // If the character is not in the supported symbol set, throw error
        throw new ArithmeticException();
      }
    }

    // Pop any remaining operators from the stack and add them to the postfix expression with a space (implemented by GPT)
    while (!stack.isEmpty()) {
      if (stack.peek() == '(' || stack.peek() == ')') {
        throw new ArithmeticException();
      }
      postfix.append(stack.pop()).append(' ');
    }

    return postfix.toString().trim();
  }

  // ! ~ Reference end : GPT - implement infix to postfix method using chat gpt except error checking

  /** Reference start ~
   * ! REFERENCE : GPT - implement evaluate postfix expression method using chat gpt except unary evaluate, avg operation, error exception
   * @reference content
   *   - Get skeleton code from GPT.
   *   - Get basic operation logic :
   *       evaluate [+ / % + - ^]'s expression, and repush the result
   *       digits control
   * ----------------------------------------------------------
   * @param    postfix                             an postfix expression in string
   * @return   stack.pop() (=evaluate value)       a evaluate value in long type
   */
  private static long evaluatePostfix(String postfix) {
    Stack<Long> stack = new Stack<>();

    for (int i = 0; i < postfix.length(); i++) {
      char c = postfix.charAt(i);

      // If the character is a digit, add it to the stack as an integer (implemented by GPT)
      if (Character.isDigit(c)) {
        StringBuilder operand = new StringBuilder();
        while (i < postfix.length() && Character.isDigit(postfix.charAt(i))) {
          operand.append(postfix.charAt(i));
          i++;
        }
        stack.push(Long.parseLong(operand.toString()));
        i--; // Decrement i to compensate for the extra increment in the loop
      }
      // If the character is a unary -(~), pop the top element from the stack and negate it
      else if (c == '~') {
        long operand = stack.pop();
        stack.push(-operand);
      }
      // If the character is a 'a' of avg, pop the number of items, then pop numItems operands and evaluate average of them
      else if (c == 'a') {
        int numItems = stack.pop().intValue();
        long sum = 0;
        for (int j = 0; j < numItems; j++) {
          sum += stack.pop();
        }

        stack.push(sum / numItems);
      }
      // If the character is an operator, pop the top two elements from the stack,
      // apply the operator and push the result back to the stack (implemented by GPT except error exception)
      else if (isOperator(c)) {
        long operand2 = stack.pop();
        long operand1 = stack.pop();

        switch (c) {
          case '+':
            stack.push(operand1 + operand2);
            break;
          case '-':
            stack.push(operand1 - operand2);
            break;
          case '*':
            stack.push(operand1 * operand2);
            break;
          case '/':
            if (operand2 == 0) { // When operand2 is 0, occuring error
              throw new ArithmeticException();
            }
            stack.push(operand1 / operand2);
            break;
          case '%':
            if (operand2 == 0) { // When operand2 is 0, occuring error
              throw new ArithmeticException();
            }
            stack.push(operand1 % operand2);
            break;
          case '^':
            if (operand2 < 0) { // When operand2 is less than 0, occuring error
              throw new ArithmeticException();
            }
            stack.push((long) Math.pow(operand1, operand2));
            break;
        }
      }
    }

    return stack.pop();
  }
  // ! ~ Reference end : GPT - implement evaluate postfix expression method using chat gpt except unary evaluate

}
