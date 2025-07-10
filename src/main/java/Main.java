public class Main {

    public static void main(String[] args) {
        CommandCalculator comm = new CommandCalculator();

        comm.input("log = 2 + 4");
        comm.input("y = 1 + 1");
        System.out.println(comm.input("2 + log10(2) * 2"));
    }
}
