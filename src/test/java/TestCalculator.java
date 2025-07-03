import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCalculator {

    @Test
    public void testAdditionAndSubstraction(){
        Calculator calc = new Calculator();
        assertEquals(5.0, calc.input("2 + 3"), 0.0001);
        assertEquals(-3.0, calc.input("0 + -3"), 0.0001);
        assertEquals(6.0, calc.input("1 + 2 + 3"), 0.0001);
        assertEquals(0.0, calc.input("1 + 2 - 3"), 0.0001);
        assertEquals(1.0, calc.input("3 - 2"), 0.0001);
        assertEquals(0.0, calc.input("0 + 0"), 0.0001);
        assertEquals(-5.0, calc.input("-2 - 3"), 0.0001);
        assertEquals(1.0, calc.input("-2 + 3"), 0.0001);
        assertEquals(5.0, calc.input("   2    +    3   "), 0.0001);
        assertEquals(1.0, calc.input("   4  /  2 - 1   "), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> calc.input("+ 3 + 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 +2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 + 2 -"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 _ 2"));
    }

    @Test
    public void testMultiplicationAndDivision(){
        Calculator calc = new Calculator();
        assertEquals(6.0, calc.input("2 * 3"), 0.0001);
        assertEquals(0.0, calc.input("0 * 3"), 0.0001);
        assertEquals(6.0, calc.input("-2 * -3"), 0.0001);
        assertEquals(-6.0, calc.input("2 * -3"), 0.0001);
        assertEquals(1.0, calc.input("1 * 2 / 2"), 0.0001);
        assertEquals(9.0, calc.input("6 / 2 * 3"), 0.0001);
        assertEquals(1.0, calc.input("3 / 3"), 0.0001);
        assertEquals(1.5, calc.input("3 / 2"), 0.0001);
        assertEquals(0.0, calc.input("0 / 2"), 0.0001);
        assertEquals(6.0, calc.input("-2 * -3"), 0.0001);
        assertEquals(-1.0, calc.input("2 * -0.5"), 0.0001);
        assertEquals(6.0, calc.input("\t2\t*\t3\t"), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> calc.input("* 3 / 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 *2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 * 2 /"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 | 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 / 0"));
    }

    @Test
    public void testMultiOperator(){
        Calculator calc = new Calculator();
        assertEquals(6.0, calc.input("3 + 1 * 3"), 0.0001);
        assertEquals(9.0, calc.input("2 * 3 + 3"), 0.0001);
        assertEquals(3.0, calc.input("9 / 9 + 2"), 0.0001);
        assertEquals(3.0, calc.input("2 + 2 * 2 / 4"), 0.0001);
        assertEquals(1.0, calc.input("1 / 3 * 3"), 0.0001);
        assertEquals(-3.0, calc.input("0 + 3 * -1"), 0.0001);
        assertEquals(2.0, calc.input("2 + 2 * 2 - 4"), 0.0001);
        assertEquals(4.0, calc.input("4 / 2 + 1 * 2"), 0.0001);
        assertEquals(2.0, calc.input("2 * 3 - 4 / 2 * 2"), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> calc.input("9 / 0 + 1"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 * 3 / 0 * 2"));
    }

    @Test
    public void testBrackets(){
        Calculator calc = new Calculator();
        assertEquals(24.0, calc.input("(2 + 2) * (2 + (2 * 2))"));
        assertEquals(8.0, calc.input("(2 + (1 + 1)) * 2"));
        assertEquals(2.0, calc.input("(1 + 1)"));
        assertEquals(-1, calc.input("0 + (-1)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(2 + 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 + 2)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(3 + 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 + 2)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("()"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("( )"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(3 3)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("1 + (2 +)"));
    }

    @Test
    public void testVoidInput(){
        Calculator calc = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> calc.input(""));
        assertThrows(IllegalArgumentException.class, () -> calc.input(" "));
    }

    @Test
    public void testBracketsSimple() {
        Calculator calc = new Calculator();
        assertEquals(14.0, calc.input("2 * (3 + 4)"));
        assertEquals(45.0, calc.input("(2 + 3) * (4 + 5)"));
        assertEquals(0.0, calc.input("3 - (2 + 1)"));
    }

    @Test
    public void testBracketsNested() {
        Calculator calc = new Calculator();
        assertEquals(16.0, calc.input("2 * (3 + (2 + 3))"));
        assertEquals(7.0, calc.input("(1 + (2 + (1 + 3)))"));
        assertEquals(6.0, calc.input("((8 / 2) + (1 + 1))"));
        assertEquals(7.0, calc.input("1 + (2 + (1 + 3))"));
        assertEquals(16.0, calc.input("2 * (3 + (2 + 3))"));
        assertEquals(10.0, calc.input("(((2 + 3)) * (1 + 1))"));
        assertEquals(3.0, calc.input("((((3))))"));
    }

    @Test
    public void testBracketsComplex() {
        Calculator calc = new Calculator();
        assertEquals(6.0, calc.input("2 + 3 * (4 - 2) - 2"));
        assertEquals(3.0, calc.input("(3 + 3) / (2 + 0)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(2 + 3"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 + 3)"));
    }

    @Test
    public void testUnaryMinus(){
        Calculator calc = new Calculator();
        assertEquals(-5.0, calc.input("-(2 + 3)"));
        assertEquals(1.0, calc.input("3 - (1 + 1)"));
        assertEquals(0.0, calc.input("-(2 + 2) + 4"));
        assertEquals(-2.0, calc.input("-2"));
        assertEquals(-4.0, calc.input("-2 * 2"));
        assertEquals(4.0, calc.input("-2 * -2"));
        assertEquals(6.0, calc.input("2 + -2 * -2"));
        assertEquals(-1.0, calc.input("2 + -3"));
    }

    @Test
    public void testPow(){
        Calculator calc = new Calculator();
        assertEquals(4.0, calc.input("2 ^ 2"));
        assertEquals(4.0, calc.input("(-2) ^ 2"));
        assertEquals(-4.0, calc.input("-2 ^ 2"));
        assertEquals(10.0, calc.input("1 + 3 ^ 2"));
        assertEquals(512.0, calc.input("2 ^ 3 ^ 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 ^"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 ^^"));
    }

}