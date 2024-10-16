import java.util.Scanner;

public class Program6 {
    public static void main(String args[]) {
        String filename = checkParameters(args);
        Encryption e = new Encryption();
        e.openFile(filename);
        if (args[0].equals("e"))
            e.encrypt(filename, args);
        else
            e.decrypt(filename, args);
    }

    // ensures that user has entered both e/d and a filename
    public static String checkParameters(String args[]) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("USAGE: java Program6 (e/d) filename");
            System.exit(0);
        } else if (!args[0].equals("e") && !args[0].equals("d")) {
            System.out.println("USAGE: java Program6 (e/d) filename");
            System.exit(0);
        }
        if (args.length == 1) {
            System.out.print("Enter filename for encryption: ");
            Scanner input = new Scanner(System.in);
            return input.next();
        }
        return args[1];
    }
}
