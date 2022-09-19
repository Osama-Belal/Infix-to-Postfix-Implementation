

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


interface IExpressionEvaluator {

    /**
     * Takes a symbolic/numeric infix expression as input and converts it to
     * postfix notation. There is no assumption on spaces between terms or the
     * length of the term (e.g., two digits symbolic or numeric term)
     *
     * @param expression infix expression
     * @return postfix expression
     */

    public String infixToPostfix(String expression);


    /**
     * Evaluate a postfix numeric expression, with a single space separator
     * @param expression postfix expression
     * @return the expression evaluated value
     */

    public int evaluate(String expression);

}


public class Evaluator implements IExpressionEvaluator {


    public class MyStack{

        class myNode{
            Object item;
            myNode next;
            myNode prev;
        }

        int lenth = 0;
        myNode top = null;

        //---------------------------------------------------------------------------

        public void push (Object element){
            myNode input = new myNode();
            input.item = element;

            if (this.lenth == 0){
                this.top = input;
                //input.next = input.prev = null;
            }
            else {
                input.prev = this.top;
                this.top.next = input;
                this.top = input;
                input.next = null;
            }

            this.lenth++;
        }

        //-------------------------------------------------------------------------

        public void clear(){
            this.top = null;
        }
        //-------------------------------------------------------------------------

        public boolean isEmpty(){
            return this.size() == 0;
        }
        //-------------------------------------------------------------------------

        public int size(){
            return this.lenth;
        }
        //-------------------------------------------------------------------------


        public Object pop (){

            if(this.lenth == 0)
                return null;


            else if(this.lenth == 1){
                myNode popped = this.top;
                this.top = null;
                this.lenth--;
                return popped.item;
            }

            else {
                myNode popped = this.top;
                this.top = this.top.prev;
                this.top.next = null;
                this.lenth--;
                return popped.item;
            }

        }

        //-------------------------------------------------------------------------

        public Object peek(){
            if(this.isEmpty())
                return null;

            else
                return this.top.item;
        }
    }


    /**
     *
     * @param oprt
     * @return
     */
    public int oprtPrty(char oprt){
        if(oprt == '-' || oprt == '+')
            return 1;
        else if(oprt == '*' || oprt == '/' || oprt == '%')
            return 2;
        else if(oprt == '^')
            return 3;
        else
            return 0;
    }

    /**
     *
     * @param oprnd1
     * @param oprnd2
     * @param operation
     * @return
     */
    public int arthOpr(int oprnd1, int oprnd2, char operation){
        switch(operation){
            case '+':return oprnd1 + oprnd2;
            case '-':return oprnd1 - oprnd2;
            case '*':return oprnd1 * oprnd2;
            case '/':return oprnd1 / oprnd2;
            case '^':return (int) Math.pow(oprnd1, oprnd2);
            default: return 0;
        }
    }

    /**
     *
     * @param ch
     * @return
     */
    public boolean isOprd(char ch){return ch == 'a' || ch == 'b' || ch =='c';}

    /**
     *
     * @param ch
     * @return
     */
    public boolean isOprt(char ch){return ch == '-' || ch == '+' || ch =='*' || ch == '/' || ch == '^';}

    /**
     *
     * @param ch
     * @return
     */
    public boolean isOPrnth(char ch){return ch == '(' || ch == '[';}

    /**
     *
      * @param ch
     * @return
     */
    public boolean isCPrnth(char ch){return ch == ')' || ch == ']';}

    /**
     *
     * @param expression infix expression
     * @return
     */
    public String infixToPostfix(String expression){

        MyStack stack = new MyStack();
        String postFix = new String();

        // checking number of parentheses is even

        int prnth = 0;
        for(int i = 0; i < expression.length(); i++){
            if(isOPrnth(expression.charAt(i)))
                prnth++;
            else if(isCPrnth(expression.charAt(i)))
                prnth--;
        }
        if(prnth != 0)
            return "Error";
        if(isOprt(expression.charAt(0)) && oprtPrty(expression.charAt(0)) != 1 ||                                                                      isOprt(expression.charAt(expression.length() - 1)))
            return "Error";

        // looping over the expression

        for(int i = 0; i < expression.length(); i++){

            char curChar = expression.charAt(i);

            // if it's operand
            if(isOprd(curChar))
                postFix += curChar;


                // if it's operator
            else if(isOprt(curChar)){

                if(curChar == '-' && expression.charAt(i+1) == '-'){
                    if(i == 0){i++;continue;}
                    else if(isOprt(expression.charAt(i-1))){i++;continue;}
                    else if (isOprd(expression.charAt(i-1))){curChar = '+';i++;}
                }
                else if(curChar == '+' && i == 0)
                    continue;

                if(stack.isEmpty()){
                    stack.push(curChar);
                    continue;
                }

                while(!stack.isEmpty() && oprtPrty(curChar) <= oprtPrty(stack.peek().toString().charAt(0)))
                    postFix += stack.pop().toString().charAt(0);

                stack.push(curChar);
            }


            //opening parenthesis
            else if(isOPrnth(curChar))
                stack.push(curChar);

                // closing parenthesis
            else if(isCPrnth(curChar)){
                while(!isOPrnth(stack.peek().toString().charAt(0)))
                    postFix += stack.pop().toString().charAt(0);
                stack.pop();
            }

            else return "Error";

        }

        while(!stack.isEmpty())
            postFix += stack.pop().toString().charAt(0);

        return postFix;

    }

    //--------------------------------------------------------------------------

    /**
     * 
     * @param expression postfix expression
     * @return
     */
    public int evaluate(String expression){

        String[] str = expression.split(",");
        String postFix = str[0];
        int a = Integer.parseInt(str[1]);
        int b = Integer.parseInt(str[2]);
        int c = Integer.parseInt(str[3]);

        MyStack stack = new MyStack();
        int oprnd1, oprnd2, result;

        for(int i = 0; i < postFix.length(); i++){
            switch(postFix.charAt(i)){
                case 'a':stack.push(a);break;
                case 'b':stack.push(b);break;
                case 'c':stack.push(c);break;
                default:
                    if(stack.size() > 1){
                        oprnd2 = (Integer) stack.pop();
                        oprnd1 = (Integer) stack.pop();

                        result = arthOpr(oprnd1, oprnd2, postFix.charAt(i));
                        stack.push(result);break;
                    }

                    else{
                        oprnd1 = (Integer) stack.pop();
                        if(postFix.charAt(i) == '-')
                            stack.push(oprnd1 * -1);
                        else
                            stack.push(oprnd1);
                    }
            }
        }

        return (Integer) stack.pop();

    }

    //--------------------------------------------------------------------------

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String sin = sc.nextLine();

        Evaluator ev = new Evaluator();
        String postFix = ev.infixToPostfix(sin);

        //reading values of variables

        sin = sc.nextLine();
        int a = Integer.parseInt(sin.replaceAll("a=", ""));
        sin = sc.nextLine();
        int b = Integer.parseInt(sin.replaceAll("b=", ""));
        sin = sc.nextLine();
        int c = Integer.parseInt(sin.replaceAll("c=", ""));


        String evaluation = postFix;
        evaluation = evaluation + ',' + a;
        evaluation = evaluation + ',' + b;
        evaluation = evaluation + ',' + c;

        System.out.println(postFix);
        if(postFix.equalsIgnoreCase("Error")) return;
        System.out.println(ev.evaluate(evaluation));

    }
}

