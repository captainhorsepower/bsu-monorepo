package edu.varabei.artsiom.calculator.brain;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiPredicate;

@Component
public class ExprToNotation {

    public String toPostfixNotation(String expr) {
        val math = "()+-*/";
        BiPredicate<String, String> hasHigherPriority = (op1, op2) -> {
            if (op2.equals("(")) return false;
            val ind1 = math.indexOf(op1) / 2;
            val ind2 = math.indexOf(op2) / 2;
            return ind1 >= ind2;
        };

        Deque<String> stack = new ArrayDeque<>();
        Deque<String> result = new ArrayDeque<>();
        for (String item : expr.split(" ")) {
            if (!math.contains(item))
                result.add(item);
            else if (stack.isEmpty())
                stack.addFirst(item);
            else {
                if (hasHigherPriority.test(stack.getFirst(), item))
                    while (!stack.isEmpty()) {
                        val popped = stack.pop();
                        if (popped.equals("(")) break;
                        result.add(popped);
                    }
                if (!item.equals(")"))
                    stack.addFirst(item);
            }
        }
        while (!stack.isEmpty())
            result.add(stack.pop());
        return String.join(" ", result);
    }
}
