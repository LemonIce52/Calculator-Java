import calculator.CommandCalculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCalculator {

    CommandCalculator calc = new CommandCalculator();

    @Test
    public void testAdditionAndSubstraction(){
        assertEquals(5.0, calc.input("2 + 3"), 0.0001);
        assertEquals(-3.0, calc.input("0 + -3"), 0.0001);
        assertEquals(6.0, calc.input("1 + 2 + 3"), 0.0001);
        assertEquals(0.0, calc.input("1 + 2 - 3"), 0.0001);
        assertEquals(1.0, calc.input("3 - 2"), 0.0001);
        assertEquals(0.0, calc.input("0 + 0"), 0.0001);
        assertEquals(-5.0, calc.input("-2 - 3"), 0.0001);
        assertEquals(1.0, calc.input("-2 + 3"), 0.0001);
        assertEquals(5.0, calc.input("+2 + 3"), 0.0001);
        assertEquals(5.0, calc.input("   2    +    3   "), 0.0001);
        assertEquals(1.0, calc.input("   4  /  2 - 1   "), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 + 2 -"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 _ 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input(" "));
    }

    @Test
    public void testMultiplicationAndDivision(){
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
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 * 2 /"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 | 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("3 / 0"));
    }

    @Test
    public void testMultiOperator(){
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
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 * 3 & 0 * 2"));
    }

    @Test
    public void testBrackets(){
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
        assertThrows(IllegalArgumentException.class, () -> calc.input("1 + (2 +)"));
    }

    @Test
    public void testVoidInput(){
        assertThrows(IllegalArgumentException.class, () -> calc.input(""));
        assertThrows(IllegalArgumentException.class, () -> calc.input(" "));
    }

    @Test
    public void testBracketsSimple() {
        assertEquals(14.0, calc.input("2 * (3 + 4)"));
        assertEquals(45.0, calc.input("(2 + 3) * (4 + 5)"));
        assertEquals(0.0, calc.input("3 - (2 + 1)"));
    }

    @Test
    public void testBracketsNested() {
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
        CommandCalculator calc = new CommandCalculator();
        assertEquals(6.0, calc.input("2 + 3 * (4 - 2) - 2"));
        assertEquals(3.0, calc.input("(3 + 3) / (2 + 0)"));
        assertEquals(-2.0, calc.input("-(1 + 1)"));
        assertEquals(-2.0, calc.input("-(-1 - 1)"));
        assertEquals(2.0, calc.input("+(-1 - 1)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(2 + 3"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 + 3)"));
    }

    @Test
    public void testUnaryMinus(){
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
        assertEquals(4.0, calc.input("2 ^ 2"));
        assertEquals(4.0, calc.input("(-2) ^ 2"));
        assertEquals(-4.0, calc.input("-2 ^ 2"));
        assertEquals(10.0, calc.input("1 + 3 ^ 2"));
        assertEquals(512.0, calc.input("2 ^ 3 ^ 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 ^"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 ^^"));
    }

    @Test
    public void testSimpleAddition() {
        assertEquals(5.0, calc.input("2 + 3"), 1e-9);
    }

    @Test
    public void testSimpleSubtraction() {
        assertEquals(4.0, calc.input("7 - 3"), 1e-9);
    }

    @Test
    public void testMultiplication() {
        assertEquals(12.0, calc.input("3 * 4"), 1e-9);
    }

    @Test
    public void testDivision() {
        assertEquals(2.5, calc.input("5 / 2"), 1e-9);
    }

    @Test
    public void testPower() {
        assertEquals(8.0, calc.input("2 ^ 3"), 1e-9);
        assertEquals(16.0, calc.input("(-2) ^ 4"), 1e-9);
        assertEquals(-4.0, calc.input("-2 ^ 2"), 1e-9);
    }

    @Test
    public void testFunctions() {
        assertEquals(Math.sin(Math.PI / 2), calc.input("sin( pi / 2 )"), 1e-9);
        assertEquals(Math.log(10), calc.input("log( 10 )"), 1e-9);
        assertEquals(Math.log10(10), calc.input("log10( 10 )"), 1e-9);
        assertEquals(Math.tan(10), calc.input("tan( 10 )"), 1e-9);
        assertEquals(Math.sqrt(25), calc.input("sqrt( 25 )"), 1e-9);
        assertEquals(Math.exp(1), calc.input("exp( 1 )"), 1e-9);
        assertEquals(Math.sin(Math.cos(0)), calc.input("sin( cos( 0 ) )"), 1e-9);
        assertEquals(Math.sqrt(Math.abs(-9)), calc.input("sqrt( abs( -9 ) )"), 1e-9);
        assertEquals(8.0, calc.input("pow( 2 , 3 )"), 1e-9);
        assertEquals(5.0, calc.input("max( 2 , 5 )"), 1e-9);
        assertEquals(2.0, calc.input("min( 2 , 5 )"), 1e-9);
        assertEquals(4.0, calc.input("pow( 2 , min( 3 , 2 ) )"), 1e-9);
        assertEquals(8.0, calc.input("pow( 2 , 3 )"), 1e-9);
        assertEquals(3.0, calc.input("max( 1 , 3 )"), 1e-9);
        assertEquals(1.0, calc.input("min( 1 , 3 )"), 1e-9);
        assertEquals(100.0, calc.input("pow( 10 , 2 )"), 1e-9);
        assertEquals(2.0, calc.input("pow( min( 2 , 3 ) , 1 )"), 1e-9);
        assertEquals(5.0, calc.input("max( sin( pi / 2 ) , 5 )"), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> calc.input("log(-1)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("log10(-25)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("sqrt(-81)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("max(,)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("max(1, 2, 4)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("max()"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("test(9)"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("sqrt(9, 25)"));
    }

    @Test
    public void testComplexExpression() {
        assertEquals(8.0, calc.input("2 + 3 * 2"), 1e-9);
        assertEquals(10.0, calc.input("( 2 + 3 ) * 2"), 1e-9);
        assertThrows(IllegalArgumentException.class, () -> calc.input(")2 + 2("));
        assertThrows(IllegalArgumentException.class, () -> calc.input("(2 + 2"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("2 + 2)"));
    }

    @Test
    public void testNegativePowerBase() {
        assertEquals(16.0, calc.input("(-2) ^ 4"), 1e-9);
        assertEquals(-8.0, calc.input("-(2 ^ 3)"), 1e-9);
        assertEquals(-16.0, calc.input("-2 ^ 2 ^ 2"), 1e-9);
        assertEquals(-2.0, calc.input("-2 ^ 2 + 2"), 1e-9);
    }

    @Test
    public void testScientificNotation() {
        assertEquals(1000.0, calc.input("1e3"), 1e-9);
        assertEquals(1.2e3, calc.input("1.2e3"), 1e-9);
        assertEquals(1.2e-3, calc.input("1.2e-3"), 1e-9);
    }

    @Test
    public void testExpressionWithSpaces() {
        assertEquals(14.0, calc.input(" 2 + 3 * 4 "), 1e-9);
        assertEquals(4.0, calc.input(" 6 - 2 "), 1e-9);
    }

    @Test
    public void testFloorCeil() {
        assertEquals(Math.floor(3.7), calc.input("floor( 3.7 )"), 1e-9);
        assertEquals(Math.ceil(3.2), calc.input("ceil( 3.2 )"), 1e-9);
    }

    @Test
    public void testConstants() {
        assertEquals(Math.PI, calc.input("pi"), 1e-9);
        assertEquals(Math.E, calc.input("e"), 1e-9);
    }

    // Дополнительно можешь протестировать ошибки:
     @Test
     public void testInvalidSyntax() {
         assertThrows(IllegalArgumentException.class, () -> calc.input("5//2"));
         assertThrows(IllegalArgumentException.class, () -> calc.input("5^^2"));
     }

     @Test
     public void testCommands() {
        assertEquals(0, calc.input("p = 1 + 1"));
        assertEquals(3.0, calc.input("p + 1"));
        assertEquals(0, calc.input("print()"));
        assertEquals(0, calc.input("clear()"));
         assertThrows(IllegalArgumentException.class, () -> calc.input("print("));
         assertThrows(IllegalArgumentException.class, () -> calc.input("clear"));
         assertThrows(IllegalArgumentException.class, () -> calc.input("print"));
         assertThrows(IllegalArgumentException.class, () -> calc.input("clear(("));
         assertThrows(IllegalArgumentException.class, () -> calc.input("print)("));
         assertThrows(IllegalArgumentException.class, () -> calc.input("clear(909)"));
         assertThrows(IllegalArgumentException.class, () -> calc.input("test"));
     }

    @Test
    public void testVariables() {
        calc.input("x = 1");
        calc.input("y = 2 + 2");
        assertThrows(IllegalArgumentException.class, () -> calc.input("123 = 1"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("1 + 2 = 3"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("1 = 1"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("= 2 + 1"));
        assertThrows(IllegalArgumentException.class, () -> calc.input("() = 2"));
    }

}