package com.example.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CalculatorTest {
    private final Calculator calculator = new Calculator();

    @ParameterizedTest
    @CsvSource({
            "2147483647, 1",          // Integer.MAX_VALUE + 1
            "-2147483648, -1",        // Integer.MIN_VALUE - 1
            "2147483647, -2147483648", // MAX + MIN
            "0, 0",
            "0, -0",
            "-0, -0"
    })
    @DisplayName("Addition overflow and zero cases")
    void additionEdgeCases(int a, int b) {
        if ((a == Integer.MAX_VALUE && b == 1) ||
                (a == Integer.MIN_VALUE && b == -1)) {
            assertThatThrownBy(() -> calculator.add(a, b))
                    .isInstanceOf(ArithmeticException.class)
                    .hasMessageContaining("overflow");
        } else {
            assertThat(calculator.add(a, b)).isEqualTo(a + b);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "-2147483648, 1",         // Integer.MIN_VALUE - 1
            "2147483647, -1",         // Integer.MAX_VALUE - (-1)
            "0, -2147483648",         // 0 - MIN_VALUE
            "-0, 0",
            "1, 1",
            "-1, -1"
    })
    @DisplayName("Subtraction underflow and identity cases")
    void subtractionEdgeCases(int a, int b) {
        if ((a == Integer.MIN_VALUE && b == 1) ||
                (a == Integer.MAX_VALUE && b == -1) ||
                    (a == 0 && b == Integer.MIN_VALUE))  {
            assertThatThrownBy(() -> calculator.subtract(a, b))
                    .isInstanceOf(ArithmeticException.class)
                    .hasMessageContaining("overflow");
        } else {
            assertThat(calculator.subtract(a, b)).isEqualTo(a - b);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2147483647, 2",          // MAX_VALUE * 2
            "-2147483648, 2",         // MIN_VALUE * 2
            "2147483647, -2147483648", // MAX * MIN
            "0, 2147483647",
            "1, -2147483648",
            "-1, -2147483648"
    })
    @DisplayName("Multiplication overflow and extreme cases")
    void multiplicationEdgeCases(int a, int b) {
        if ((a == Integer.MAX_VALUE && b == 2) ||
                (a == Integer.MIN_VALUE && b == 2) ||
                    (a == Integer.MAX_VALUE && b == Integer.MIN_VALUE) ||
                        (a == -1 && b == Integer.MIN_VALUE)) {
            assertThatThrownBy(() -> calculator.multiply(a, b))
                    .isInstanceOf(ArithmeticException.class);
        } else {
            assertThat(calculator.multiply(a, b)).isEqualTo(a * b);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0",                   // Division by zero
            "-2147483648, -1",        // MIN_VALUE / -1 (overflow)
            "2147483647, 1",          // MAX_VALUE / 1
            "0, 1",
            "1, 3",                   // Repeating decimal
            "-1, -1"
    })
    @DisplayName("Division edge cases and precision")
    void divisionEdgeCases(int a, int b) {
        if (b == 0) {
            assertThatThrownBy(() -> calculator.divide(a, b))
                    .isInstanceOf(ArithmeticException.class)
                    .hasMessage("Division by zero");
        } else if (a == Integer.MIN_VALUE && b == -1) {
            assertThatThrownBy(() -> calculator.divide(a, b))
                    .isInstanceOf(ArithmeticException.class);
        } else {
            assertThat(calculator.divide(a, b))
                    .isCloseTo((double)a / b, within(0.000000000000001));
        }
    }

    @Test
    @DisplayName("Chained operations with overflow risk")
    void chainedOperations() {
        // (MAX - 1) + 2 should overflow
        assertThatThrownBy(() -> {
            int intermediate = calculator.subtract(Integer.MAX_VALUE, 1);
            calculator.add(intermediate, 2);
        }).isInstanceOf(ArithmeticException.class);

        // (MIN / -1) should overflow
        assertThatThrownBy(() -> {
            calculator.divide(Integer.MIN_VALUE, -1);
        }).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void floatingPointPrecision() {
        double result = calculator.divide(1, 3);
        assertThat(result)
                .isEqualTo(0.3333333333333333) // Exact comparison fails
                .isCloseTo(0.3333333333333333, within(0.000000000000001));
    }

    @Test
    void negativeZeroHandling() {
        assertThat(calculator.divide(0, -1)).isEqualTo(-0.0);
        assertThat(calculator.divide(-0, 1)).isEqualTo(-0.0);
    }

    @Test
    void maxMinCombinations() {
        assertThat(calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE))
                .isEqualTo(-1);
        assertThat(calculator.multiply(Integer.MAX_VALUE, 1))
                .isEqualTo(Integer.MAX_VALUE);
    }
}