package encryptdecrypt;

import java.io.*;
import     java.lang.Number;
import   java.io.Serializable;
import   java.lang.CharSequence;
import     java.util.Collection;


public class EncryptionDecryption {
    static String inputLine = " ";
    static String key = "0";
    private static String[] arguments;
    private static String mode = "enc";
    private static String data = " ";
    private static String in;
    private static String alg;
    static String out;
    static String filePath = "";


    public static void setArguments(String[] args) {
        arguments = args.clone();
    }

    public static void execute() throws IOException {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].matches("enc")) {
                mode = arguments[i];
            } else if (arguments[i].matches("dec")) {
                mode = arguments[i];
            }
        }
        switch (mode) {
            case "enc":
                input();
                if (alg.matches("unicode")) {
                    Unicdoe.encryption();
                } else if (alg.matches("shift")) {
                    Shift.encryption();
                }
                break;
            case "dec":
                input();
                if (alg.matches("unicode")) {
                    Unicdoe.decryption();
                } else if (alg.matches("shift")) {
                    Shift.decryption();
                }
                break;
        }
    }

    private static void input() throws IOException {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i].matches("-key")) {
                key = arguments[i + 1];
            } else if (arguments[i].matches("-data")) {
                inputLine = arguments[i + 1];
            } else if (arguments[i].matches("-in")) {
                in = arguments[i + 1];
            } else if (arguments[i].matches("-out")) {
                out = arguments[i + 1];
            } else if (arguments[i].matches("-alg")) {
                alg = arguments[i + 1];
            }
        }

        if (out != null) {
            filePath = out;
            File file = new File(filePath);
            file.createNewFile();

        }


        StringBuilder stringBuilder = new StringBuilder();
        if (in.length() > 0) {
            FileInputStream fileInputStream = new FileInputStream(in);
            int i;
            while ((i = fileInputStream.read()) != -1) {
                stringBuilder.append((char) i);
            }
            inputLine = stringBuilder.toString();
            fileInputStream.close();
        } else if (data.length() > 0) {
            inputLine = data;
        }
    }

}

class Unicdoe extends EncryptionDecryption {

    static void encryption() throws IOException {
        String encryptionLine;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (int s = 0; s < inputLine.length(); s++) {
            int temp = inputLine.charAt(s);
            while (count != Integer.parseInt(key)) {
                temp++;
                count++;
            }
            count = 0;
            stringBuilder.append((char) temp);
        }
        stringBuilder.append(" ");
        encryptionLine = stringBuilder.toString();

        byte[] result = encryptionLine.getBytes();
        if (out.length() > 0) {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(result);
            fileOutputStream.close();
        } else {
            System.out.print(encryptionLine + "\n");
        }
    }

    static void decryption() throws IOException {
        String decryptionLine;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (int s = 0; s < inputLine.length(); s++) {
            int temp = inputLine.charAt(s);
            while (count != Integer.parseInt(key)) {
                temp--;
                count++;
            }
            count = 0;
            stringBuilder.append((char) temp);
        }
        stringBuilder.append(" ");

        decryptionLine = stringBuilder.toString();
        byte[] result = decryptionLine.getBytes();
        if (out.length() > 0) {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(result);
            fileOutputStream.close();
        } else {
            System.out.print(decryptionLine + "\n");
        }
    }
}

class Shift extends EncryptionDecryption {

    static void encryption() throws IOException {
        String[] splitLine = inputLine.split(" ");
        String encryptionLine;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < splitLine.length; i++) {
            for (int s = 0; s < splitLine[i].length(); s++) {
                int temp = splitLine[i].charAt(s);
                while (count != Integer.parseInt(key)) {
                    temp++;
                    if (temp == 122) {
                        temp = 96;
                    }
                    count++;
                }
                count = 0;
                stringBuilder.append((char) temp);
            }
            stringBuilder.append(" ");
        }
        encryptionLine = stringBuilder.toString();

        byte[] result = encryptionLine.getBytes();
        if (out.length() > 0) {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(result);
            fileOutputStream.close();
        } else {
            System.out.print(encryptionLine + "\n");
        }
    }

    static void decryption() throws IOException {
        String[] splitLine = inputLine.split(" ");
        String decryptionLine;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < splitLine.length; i++) {
            for (int s = 0; s < splitLine[i].length(); s++) {
                int temp = splitLine[i].charAt(s);
                while (count != Integer.parseInt(key)) {
                    temp--;
                    if (temp == 96) {
                        temp = 122;
                    }
                    count++;
                }
                count = 0;
                stringBuilder.append((char) temp);
            }
            stringBuilder.append(" ");
        }

        decryptionLine = stringBuilder.toString();
        byte[] result = decryptionLine.getBytes();
        if (out.length() > 0) {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(result);
            fileOutputStream.close();
        } else {
            System.out.print(decryptionLine + "\n");
        }
    }

}
