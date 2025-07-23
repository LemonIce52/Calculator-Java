# 🧮 Java Expression Calculator

Advanced Java calculator with support for:

- 📐 mathematical functions (`sin`, `cos`, `log`, `sqrt`, `pow`, `abs`, `floor`, `ceil`, `exp`, etc.)
- 🧮 arithmetic operations with priorities (`+`, `-`, `*`, `/`, `^`)
- 📚 variables
- 📦 nested expressions and brackets
- 📌 constants (`pi`, `e`)
- ✅ commands (`print`, `clear`)
- 🔁 unary operators `+` and `-`
- 🧪 unit tests with full syntax and calculations checking

---

## 🔧 Technologies

- Java 17+
- Regular expressions (`Pattern`, `Matcher`)
- Exceptions (`IllegalArgumentException`)
- JUnit 5
- Gradle (for tests and build)
- Test coverage: JaCoCo

---

## 🗂️ Project structure

```
src/
├── calculator/ ← calculator logic
│ ├── Calculator.java ← main calculation engine
│ ├── CommandCalculator.java ← variable and command support
│ ├── Tokenize.java ← expression tokenization
│ ├── FunctionResolver.java ← function and constant handling
│ ├── BracketsResolution.java ← nested brackets expansion
│ ├── TokenConvert.java ← token conversion
│ ├── CalcVariables.java ← variable storage and management
│ └── MathFunctionsAndOperatorExecutor.java ← calculation of operations and functions
└── test/
└── CalculatorTest.java ← unit tests (JUnit 5)
```

---

## 🚀 How to use

### In code:

```java
CommandCalculator calc = new CommandCalculator();

// simple expressions
System.out.println(calc.input("2 + 2")); // → 4.0
System.out.println(calc.input("(-2)^2")); // → 4.0
System.out.println(calc.input("-2^2")); // → -4.0

// functions and nested parentheses
System.out.println(calc.input("sqrt(25) + log10(100)")); // → 7.0

// working with variables
calc.input("x = 2");
calc.input("y = x ^ 3");
System.out.println(calc.input("y + 1")); // → 9.0

// commands
calc.input("print(x)"); // → 2.0
calc.input("clear()"); // clears all variables
```

---

## 📌 Features

- 📐 Operation precedence is taken into account (e.g. `^` is performed from right to left).
- ✅ Handling of unary minus (`-2^2` → `-4`, `(-2)^2` → `4`).
- 🔢 Support for scientific notation (`1.2e3`, `3E-4`, etc.).
- 💥 Generate clear exceptions on errors:
- Unbalanced parentheses
- Invalid functions
- Invalid arguments (`sqrt(-1)`)
- Double operators (`2 ++ 3`)
- Division by zero, etc.

---

## 🧪 Testing

```bash
./gradlew test
```

- Coverage of all key scenarios
- Check expressions, nested functions, variables, and errors
- Check for correct behavior of `print()` and `clear()`

---

## ⏱ Performance

- Simple expressions: ~0.02–0.05 ms after JVM warm-up
- Complex expressions (with 4+ nesting levels and multiple functions): ~0.4–1 ms

---

## 📁 Example expressions for a test

```text
- log10(1000) + pow(2, 3 ^ 2) + abs(-10) * sin(1)
- abs(floor(ceil(sqrt(exp(2 + log(100)))))) + max(1, min(3, 2))
- 2 + (3 * (4 - 1) + (1 + 2)) ^ 2 - (log(10) / 2)
```
