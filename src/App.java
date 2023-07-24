import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        final String password;
        try (Scanner input = new Scanner(System.in);) {
            System.out.print("Введите ключ шифрования --> ");
            password = input.nextLine();
        }
    }
}
