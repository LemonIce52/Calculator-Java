public class CommandCalculator {

    public double input(String excerpt) {
        excerpt = excerpt.trim().toLowerCase();

        if (isCommand(excerpt)) {
            ApplyCommand applyCommand = new ApplyCommand();
            switch (excerpt) {
                case "print()" -> applyCommand.printList();
                case "clear()" -> applyCommand.clearList();
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
