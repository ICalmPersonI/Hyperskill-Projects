package correcter;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;


public class SymbolLevelErrorEmulator {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private static String input;

    public static void execute() throws IOException {
        readInput();
    }

    private static void readInput() throws IOException {
        input = bufferedReader.readLine();
        switch (input) {
            case "encode":
                encode();
                break;
            case "send":
                send();
                break;
            case "decode":
                decode();
                break;
        }

    }

    // Eat more of these french buns!
    private static void encode() {
        StringBuilder stringBuilder = new StringBuilder();
        String path = "send.txt";
        ArrayList<Integer> contentsFile = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                contentsFile.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] hex = new String[contentsFile.size()];
        for (int i = 0; i < contentsFile.size(); i++) {
            hex[i] = Integer.toHexString(contentsFile.get(i));
        }

        String[] binary = new String[hex.length];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = hexToBin(hex[i]);
        }

        String[] hammingCode = new String[binary.length * 2];
        for (int i = 0, s = 0, j = 1; i < binary.length; i++, s = s + 2, j = j + 2) {
            hammingCode[s] = binary[i].substring(0, 4);
            hammingCode[j] = binary[i].substring(4, 8);
        }

        for (int i = 0; i < hammingCode.length; i++) {
            String temp = "0" + "0"
                    + hammingCode[i].charAt(0) + "0"
                    + hammingCode[i].charAt(1)
                    + hammingCode[i].charAt(2)
                    + hammingCode[i].charAt(3);
            hammingCode[i] = temp;
        }

        checkBitComputation(hammingCode, "code");

        stringBuilder.setLength(0);
        for (String s : hammingCode) {
            stringBuilder.append(s);
        }

        byte[] result = hexStringToByteArray(binaryToHexadecimal(stringBuilder.toString()));

        try (FileOutputStream fileOutputStream = new FileOutputStream("encoded.txt")) {
            fileOutputStream.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send() {
        StringBuilder stringBuilder = new StringBuilder();
        String path = "encoded.txt";
        ArrayList<Integer> contentsFile = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                contentsFile.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] hex = new String[contentsFile.size()];
        for (int i = 0; i < contentsFile.size(); i++) {
            hex[i] = Integer.toHexString(contentsFile.get(i));
        }

        String[] binary = new String[hex.length];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = hexToBin(hex[i]);
        }

        for (int i = 0; i < binary.length; i++) {
            double position = Math.random() * (7 - 4) + 4;
            String temp = String.valueOf(binary[i].charAt((int)position));
            if (temp.matches("0")) {
                binary[i] = changeLetterInPosition((int)position, (char) 49, binary[i]);
            } else {
                binary[i] = changeLetterInPosition((int)position, (char) 48, binary[i]);
            }
            stringBuilder.append(binary[i]);
        }

        byte[] result = hexStringToByteArray(binaryToHexadecimal(stringBuilder.toString()));

        File outFile = new File("received.txt");
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void decode() {

        StringBuilder stringBuilder = new StringBuilder();
        String path = "received.txt";
        ArrayList<Integer> contentsFile = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                contentsFile.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] hex = new String[contentsFile.size()];
        for (int i = 0; i < contentsFile.size(); i++) {
            hex[i] = Integer.toHexString(contentsFile.get(i));
        }

        String[] binary = new String[hex.length];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = hexToBin(hex[i]);
        }

        String[] decodeHammingCode = new String[binary.length];
        for (int i = 0; i < decodeHammingCode.length; i++) {
            decodeHammingCode[i] = binary[i];
            decodeHammingCode[i] = changeLetterInPosition(0, (char) 48, decodeHammingCode[i]);
            decodeHammingCode[i] = changeLetterInPosition(1, (char) 48, decodeHammingCode[i]);
            decodeHammingCode[i] = changeLetterInPosition(3, (char) 48, decodeHammingCode[i]);
        }

        checkBitComputation(decodeHammingCode, "decode");

        int ch = 0;
        int chGet = 0;
        int position = 0;
        int addposition = 0;
        int countDecode = 0;
        for(int i = 0; i < decodeHammingCode.length; i++) {
            while (countDecode != 3) {
                switch (ch) {
                    case 0: chGet = 0; addposition = 1; break;
                    case 1: chGet = 1; addposition = 2; break;
                    case 2: chGet = 3; addposition = 4; break;
                }
                String tempBinary = String.valueOf(binary[i].charAt(chGet));
                String tempHamming = String.valueOf(decodeHammingCode[i].charAt(chGet));
                if (!tempBinary.matches(tempHamming)) {
                    position = position + addposition;
                }
                ch++;
                countDecode++;
                chGet = 0;

            }
            position--;
            String tempPosition = String.valueOf(binary[i].charAt(position));
            if (tempPosition.matches("0")) {
                binary[i] = changeLetterInPosition(position, (char) 49, binary[i]);
                decodeHammingCode[i] = binary[i];
            } else {
                binary[i] = changeLetterInPosition(position, (char) 48, binary[i]);
                decodeHammingCode[i] = binary[i];
            }
            countDecode = 0;
            position = 0;
            ch = 0;
        }

        for (int i = 0; i < decodeHammingCode.length; i++) {
            stringBuilder.append(decodeHammingCode[i].charAt(2));
            stringBuilder.append(decodeHammingCode[i].charAt(4));
            stringBuilder.append(decodeHammingCode[i].charAt(5));
            stringBuilder.append(decodeHammingCode[i].charAt(6));
            decodeHammingCode[i] = stringBuilder.toString();
            stringBuilder.setLength(0);
        }

        String[] binaryEncode = new String[decodeHammingCode.length / 2];
        for (int i = 0, s = 1, j = 0; j < binaryEncode.length; i = i + 2, s = s + 2, j++) {
            binaryEncode[j] = decodeHammingCode[i].concat(decodeHammingCode[s]);
        }


        for (int i = 0; i < binaryEncode.length; i++) {
            int decimal = Integer.parseInt(binaryEncode[i], 2);
            binaryEncode[i] = Integer.toString(decimal, 16);
            stringBuilder.append(binaryEncode[i]);
        }


        byte[] result = hexStringToByteArray(stringBuilder.toString());

        try (FileOutputStream fileOutputStream = new FileOutputStream("decoded.txt")) {
            fileOutputStream.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] checkBitComputation(String[] hammingCode, String state) {

        for (int i = 0; i < hammingCode.length; i++) {
            int temp = hammingCode[i].charAt(2) +
                    hammingCode[i].charAt(4) +
                    hammingCode[i].charAt(6);
            if (temp % 2 == 0) {
                hammingCode[i] = changeLetterInPosition(0, (char) 48, hammingCode[i]);
            } else {
                hammingCode[i] = changeLetterInPosition(0, (char) 49, hammingCode[i]);
            }
        }

        for (int i = 0; i < hammingCode.length; i++) {
            int temp = hammingCode[i].charAt(2) +
                    hammingCode[i].charAt(5) +
                    hammingCode[i].charAt(6);
            if (temp % 2 == 0) {
                hammingCode[i] = changeLetterInPosition(1, (char) 48, hammingCode[i]);
            } else {
                hammingCode[i] = changeLetterInPosition(1, (char) 49, hammingCode[i]);
            }
        }

        for (int i = 0; i < hammingCode.length; i++) {
            int temp = hammingCode[i].charAt(4) +
                    hammingCode[i].charAt(5) +
                    hammingCode[i].charAt(6);
            if (temp % 2 == 0) {
                hammingCode[i] = changeLetterInPosition(3, (char) 48, hammingCode[i]);
            } else {
                hammingCode[i] = changeLetterInPosition(3, (char) 49, hammingCode[i]);
            }
            if (state.matches("code")) {
                hammingCode[i] = hammingCode[i] + "0";
            }
        }
        return hammingCode;
    }


    private static byte[] hexStringToByteArray(String s) {
        byte[] data = new byte[s.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ((Character.digit(s.charAt(i * 2), 16) << 4)
                    + Character.digit(s.charAt(i * 2 + 1), 16));
        }
        return data;
    }

    private static String changeLetterInPosition(int position, char ch, String str) {
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    private static String hexToBin(String s) {
        String preBin = new BigInteger(s, 16).toString(2);
        Integer length = preBin.length();
        if (length < 8) {
            for (int i = 0; i < 8 - length; i++) {
                preBin = "0" + preBin;
            }
        }
        return preBin;
    }

    private static final String[] hexValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static String binaryToHexadecimal(String binary) {
        binary = leftPad(binary);
        return convertBinaryToHexadecimal(binary);

    }

    private static String convertBinaryToHexadecimal(String binary) {
        String hexadecimal = "";
        int sum = 0;
        int exp = 0;
        for (int i = 0; i < binary.length(); i++) {
            exp = 3 - i % 4;
            if ((i % 4) == 3) {
                sum = sum + Integer.parseInt(binary.charAt(i) + "") * (int) (Math.pow(2, exp));
                hexadecimal = hexadecimal + hexValues[sum];
                sum = 0;
            } else {
                sum = sum + Integer.parseInt(binary.charAt(i) + "") * (int) (Math.pow(2, exp));
            }
        }
        return hexadecimal;
    }

    private static String leftPad(String binary) {
        int paddingCount = 0;
        if ((binary.length() % 4) > 0)
            paddingCount = 4 - binary.length() % 4;

        while (paddingCount > 0) {
            binary = "0" + binary;
            paddingCount--;
        }
        return binary;
    }

}

