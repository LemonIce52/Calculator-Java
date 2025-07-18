/**
 *<p>
 * {@link CommandCalculator} this class determines whether you pass a command,
 * create a variable or pass an expression immediately.
 * </p>
 *
 * <p>
 * If you pass an expression immediately, the result of
 * the expression will be returned, in other cases, zero is returned as an indicator that everything went well.
 * A variable is created by passing a string of the type "name = value or expression" to the method {@link #input input},
 * after creating a variable, it can be used in expressions if the variables have not been cleared.
 * </p>
 * <p>
 * There are also commands of the type print() and clear()
 * print - this command displays a set of all created variables (if an expression was passed to the variable, then you will see the result in the list).
 * clear - this command clears the entire list of previously created variables without the ability to select a specific variable to delete
 * </p>
 *
 * <blockquote><pre>
 *     CommandCalculator calc = new CommandCalculator();
 *     System.out.println(calc.input("2 + 2")); // return 4
 *     System.out.println(calc.input("-2^2")); // return -4
 *     System.out.println(calc.input("(-2) ^ 2")); // return 4
 *     System.out.println(calc.input("x = 1 + 1")); // return 0 and x = 2
 *     calc.input("print()"); // return 0 and print "x = 2";
 *     System.out.println(calc.input("x + 3")); // return 5
 *     calc.input("clear()"); // return 0 and print "Clear successes"
 *     System.out.println(calc.input("x + 3")); // error variable x no longer exists because we deleted it
 *</blockquote></pre>
 * **/
public final class CommandCalculator {

    /**
     * <p>This method accepts a string with an expression or a command or variable to create
     * (how to create a variable and what commands are present are described in the class documentation).</p>
     *
     * @param excerpt expression represented as a string
     *
     * @return result type double (when creating a variable or using a command,
     *         0.0 is returned as an indicator that everything went well;
     *         in other cases, the result of calculating the expression is returned)
     *
     * @throws IllegalArgumentException if the command is entered incorrectly or a non-existent command is entered
     * @throws IllegalArgumentException if an incorrect variable name is entered (only letters can be used in the variable name)
     * @throws IllegalArgumentException if you use a variable in an expression that was not created or deleted
     * @throws IllegalArgumentException the string contains an extra bracket
     * @throws IllegalArgumentException the brackets are incorrectly positioned
     * @throws IllegalArgumentException if non-existent mathematical function is entered
     * @throws IllegalArgumentException if small or large number of parameters are passed to the function
     * @throws IllegalArgumentException if the function log, log10 and sqrt passes an argument less than zero
     * @throws IllegalArgumentException if the string is empty or contains one space
     * @throws IllegalArgumentException if the string ends with a sign or there are two signs in a row
     * (minus and plus coming under or at the beginning of the string are perceived as unary)
     * @throws IllegalArgumentException if there are 2 numbers with a remainder in a row
     * (if 2 numbers go in a row they are perceived as a single number)
     * **/
    public double input(String excerpt) {
        excerpt = excerpt.trim().toLowerCase();

        if (isCommand(excerpt)) {
            ApplyCommand applyCommand = new ApplyCommand();
            switch (excerpt) {
                case "print()" -> applyCommand.printList();
                case "clear()" -> applyCommand.clearList();
                default -> throw new IllegalArgumentException("Command not found!");
            }
        } else if (excerpt.contains("=")) {
            variables(excerpt);
        } else {
            Calculator calc = new Calculator();
            return calc.input(excerpt);
        }

        return 0;
    }

    private boolean isCommand(String excerpt) {
        return excerpt.equals("print()") || excerpt.equals("clear()");
    }

    //processes the variable by splitting it by the = sign
    // if an expression was assigned to the variable it will first be solved and then assigned to the variable
    private void variables(String excerpt) {
        CalcVariables var = new CalcVariables();
        double result;
        String[] splitExcerpt = excerpt.split("=");
        for (int i = 0; i < splitExcerpt.length; i++) {
            splitExcerpt[i] = splitExcerpt[i].trim();
        }
        validationNameVariables(splitExcerpt[0]);
        try {
            result = Double.parseDouble(splitExcerpt[1]);
        } catch (NumberFormatException e) {
            Calculator calc = new Calculator();
            result = calc.input(splitExcerpt[1]);
        }

        var.setVariables(splitExcerpt[0], result);
    }

    private void validationNameVariables(String name) {
        if (!name.matches("[a-z]+"))
            throw new IllegalArgumentException("The name cannot contain any characters except letters!");
    }

}
