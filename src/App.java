import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        final String password;
        final boolean encrypt;
        final File file;
        try (Scanner input = new Scanner(System.in);) {
            System.out.print("Введите зашифровать или расшифровать(y/n) --> ");
            encrypt = input.nextLine().equals("y");
            System.out.print("Введите ключ шифрования --> ");
            password = input.nextLine();
            System.out.print("Введите путь к файлу --> ");
            file = new File(input.nextLine());
        }
        if (encrypt) {
            Scheta.encryptFile(file, 2048, password);
        } else {
            Scheta.decryptFile(file, 2048, password);
        }
    }
}
