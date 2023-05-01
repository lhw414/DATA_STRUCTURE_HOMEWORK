import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		String postfixExpression = infixToPostfix(input);
		System.out.println(postfixExpression);
		long sum = evaluatePostfix(postfixExpression);
		System.out.println(sum);
	}

	private static boolean isOperator(char c) {
        return (c == '+' 
			|| c == '-' 
			|| c == '*' 
			|| c == '/' 
			|| c == '%' 
			|| c == '^');
    }

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

	// ! REF : GPT - implement infix to postfix method using chat gpt except error checking
    // Method to convert infix expression to postfix
    private static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            // If the character is an operand, add it to the postfix expression with a space
            if (Character.isDigit(c)) {
                // If the current character is a digit, keep reading until a non-digit character is found
                StringBuilder operand = new StringBuilder();
                while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                    operand.append(infix.charAt(i));
                    i++;
                }
                postfix.append(operand).append(' ');
                i--; // Decrement i to compensate for the extra increment in the loop
            }
            // If the character is an opening parenthesis, push it onto the stack
            else if (c == '(') {
                stack.push(c);
            }
            // If the character is a closing parenthesis, pop operators from the stack and add them to the postfix expression until an opening parenthesis is reached
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(' ');
                }
                if (!stack.isEmpty() && stack.peek() != '(') {
                    return "Invalid expression"; // Invalid expression
                } else {
                    stack.pop(); // Remove the opening parenthesis from the stack
                }
            }
            // If the character is an operator, pop operators from the stack and add them to the postfix expression until an operator with lower precedence is reached, then push the current operator onto the stack
            else if (isOperator(c)) {
				if (c == '-') {
					if (i == 0 || isOperator(infix.charAt(i-1))) {
						stack.push('~');
						continue;
					}
				}
                while (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
					if (c == '^' && stack.peek() == '^') {
						break;
					}
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        // Pop any remaining operators from the stack and add them to the postfix expression with a space
        while (!stack.isEmpty()) {
            if (stack.peek() == '(' || stack.peek() == ')') {
                return "Invalid expression"; // Invalid expression
            }
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString();
    }
	// ! ~ REF : GPT - implement infix to postfix method using chat gpt except error checking

	// ! REF : GPT - implement evaluate postfix expression method using chat gpt except unary evaluate
	private static long evaluatePostfix(String postfix) {
        Stack<Long> stack = new Stack<>();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            // If the character is a digit, add it to the stack as an integer
            if (Character.isDigit(c)) {
                StringBuilder operand = new StringBuilder();
                while (i < postfix.length() && Character.isDigit(postfix.charAt(i))) {
                    operand.append(postfix.charAt(i));
                    i++;
                }
                stack.push(Long.parseLong(operand.toString()));
                i--; // Decrement i to compensate for the extra increment in the loop
            }
            // If the character is a tilde(~), pop the top element from the stack and negate it
            else if (c == '~') {
                long operand = stack.pop();
                stack.push(-operand);
            }
            // If the character is an operator, pop the top two elements from the stack, apply the operator and push the result back to the stack
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
                        stack.push(operand1 / operand2);
                        break;
                    case '%':
                        stack.push(operand1 % operand2);
                        break;
                    case '^':
                        stack.push((long) Math.pow(operand1, operand2));
                        break;
                }
            }
        }

        return stack.pop();
    }
	// ! ~ REF : GPT - implement evaluate postfix expression method using chat gpt except unary evaluate

}
