package models

import scala.collection.mutable.Stack
import scala.collection.mutable.Queue
import scala.collection.mutable.Map
import util.control.Breaks._
import play.api.Logger

object Calculator {

    // Convert Infix Into RPN and return it as Queue[String]
    /** FLOW:
    * if ) pop from stack to que until (
    * if ( push to stack
    * if value, push to que
    * if operand, check importance and push to stack accordingly
    */
    def infixToRPN(infix: String): Queue[String] = {

      val queue = new Queue[String]()
      val stack = new Stack[String]()

      // Map values to the operands so we can compare which needs to be done first
      val operandMappings = Map[String, Int](
          "/" -> 5,
          "*" -> 5,
          "+" -> 4,
          "-" -> 4,
          "(" -> 0 
      )

      var x = 0;
      while(x <= infix.length-1){

        Logger.info(x.toString)

        val token = infix.charAt(x).toString;

        if(token == "(") {
            stack.push(token) 
            x += 1;
        }
                
        else if(token == ")") {
            while("(" != stack.top){
                queue += stack.pop()
            }
            stack.pop()
            x += 1;
        }

        else if(operandMappings.contains(token)) {
            while(!stack.isEmpty && operandMappings(token) <= operandMappings(stack.top)){
                queue += stack.pop()
            }
            stack.push(token)
            x += 1;
        }
            
        else if(isNumber(token)){ 

            var value = "";

            try {
                while(isNumber(infix.charAt(x).toString)){
                    value = value.concat(infix.charAt(x).toString);
                    x += 1;
                } 
                queue += value
            } catch {
                case e:Exception => queue += value 
            }

        }

        else {
            Logger.info("input= " + infix)
            throw new IllegalArgumentException("The input is invalid");
        }
    }

    while(!stack.isEmpty){
        queue += stack.pop()
    }

    Logger.info("" + queue);

    return queue

    }

    def isNumber(str: String): Boolean = {
      try {
        str.toDouble
        return true;
      } catch {
        case e:Exception => false;
      }
    }
      
    /**
    * Iterate trough each entry
    * if operation, pop 2 values from the stack, reduce with the operation, and push the result to stack
    * if value, just push to stack
    * the last entry in the stack is the result we want
    */ 
    def evalRPN(rpn: Queue[String]): Double = {
      
        val stack = new Stack[Double]()

        val operations = Map(
            "+" -> ((_: Double) + (_: Double)),
            "*" -> ((_: Double) * (_: Double)),
            "-" -> ((x: Double, y: Double) => y - x),
            "/" -> ((x: Double, y: Double) => y / x)
        )

        rpn.foreach(entry =>
            if (operations.contains(entry)){
                val first = stack.pop
                val second = stack.pop
                stack.push(operations(entry)(first, second))
            }
            else stack.push(entry.toDouble)
        )

        stack.pop

    }
    

}