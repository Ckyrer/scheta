import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Scheta {
    private static int fitInRange(int lastVal, int n, boolean encrypt) {
        int locker = ((n+128)*100/255);
        if (!encrypt) {locker*=-1;}
        int res = lastVal+locker;
        if (res>127) {
            return (byte) (res-256);
        } else if (res<-128) {
            return (byte) (res+256);
        }
        return res;
    }

    private static Stream<Byte> baseCrypt(boolean encrypt, InputStream input, String code, int buffer_size) throws IOException  {
        final int[] key = new int[code.length()];
        for (int i=0; i<code.length(); i++) {
            key[i] = (int)(code.charAt(i));
        }

        ArrayList<Byte> NEW_CONTENT = new ArrayList<Byte>();

        int Lcount = 0;
        byte[] buffer = new byte[buffer_size];
        int avail;

        while ( (avail = input.read(buffer))!=-1 ) {
            for (int i=0; i<avail; i++) {
                NEW_CONTENT.add((byte) fitInRange(buffer[i], key[Lcount], encrypt));
                Lcount++;
                if (Lcount==key.length) {Lcount=0;}
            }
        }

        return NEW_CONTENT.stream();

    }

    public static Stream<Byte> encryptStream(InputStream input, int buffer_size, String code) {
        try {
            return baseCrypt(true, input, code, buffer_size);
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public static Stream<Byte> decryptStream(InputStream input, int buffer_size, String code) {
        try {
            return baseCrypt(false, input, code, buffer_size);
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public static Stream<Byte> encryptFileContent(File file, int buffer_size, String code) {
        try {
            try (FileInputStream f = new FileInputStream(file)) {
                return baseCrypt(true, f, code, buffer_size);
            }
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public static Stream<Byte> decryptFileContent(File file, int buffer_size, String code) {
        try {
            try (FileInputStream f = new FileInputStream(file)) {
                return baseCrypt(false, f, code, buffer_size);
            }
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

    public static void encryptFile(File file, int buffer_size, String code) {
        try {
            // Чтение
            final Stream<Byte> encryptedStream;
            try (FileInputStream f = new FileInputStream(file)) {
                encryptedStream = baseCrypt(true, f, code, buffer_size);
            }
            // Запись
            try (FileOutputStream f = new FileOutputStream(file)) {
                encryptedStream.forEach((Byte b) -> {
                    try {
                        f.write(b);
                    } catch (IOException e) {e.printStackTrace();}
                });
                f.flush();
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void decryptFile(File file, int buffer_size, String code) {
        try {
            // Чтение
            final Stream<Byte> encryptedStream;
            try (FileInputStream f = new FileInputStream(file)) {
                encryptedStream = baseCrypt(false, f, code, buffer_size);
            }
            // Запись
            try (FileOutputStream f = new FileOutputStream(file)) {
                encryptedStream.forEach((Byte b) -> {
                    try {
                        f.write(b);
                    } catch (IOException e) {e.printStackTrace();}
                });
                f.flush();
            }
        } catch (IOException e) {e.printStackTrace();}
    }

}
