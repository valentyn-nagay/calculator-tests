package com.example.calculator;

public class Calculator {

    // Addition with overflow protection
    public int add(int a, int b) {
        long result = (long)a + (long)b;
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow in addition");
        }
        return a + b;
    }

    // Subtraction with overflow protection
    public int subtract(int a, int b) {
        long result = (long)a - (long)b;
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow in subtraction");
        }
        return a - b;
    }

    // Multiplication with overflow protection
    public int multiply(int a, int b) {
        long result = (long)a * (long)b;
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer overflow in multiplication");
        }
        return a * b;
    }

    // Division with edge case handling
    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }

        // Special case: MIN_VALUE / -1 causes overflow
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new ArithmeticException("Integer overflow in division");
        }

        return (double)a / b;
    }

    // Additional helper to check operations
    public void checkOperation(int a, int b, String operation) {
        switch (operation) {
            case "add":
                add(a, b);
                break;
            case "subtract":
                subtract(a, b);
                break;
            case "multiply":
                multiply(a, b);
                break;
            case "divide":
                divide(a, b);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation");
        }
    }
}