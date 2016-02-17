/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimpleDESCipher;


/**
 *
 * @author Jeff
 */
public class CipherMethods {

    protected String asciiConvert(String message, int radix) {

        char[] chars = message.toCharArray();
        StringBuilder result = new StringBuilder();

        switch (radix) {
            case 2:
                for (int a = 0; a < chars.length; a++) {
                    result.append(String.format("%8s", Integer.toBinaryString((int) chars[a])).replace(' ', '0'));

                }
                break;
            case 16:

                for (int i = 0; i < chars.length; i++) {
                    result.append(String.format("%2s", Integer.toHexString((int) chars[i])).replace(' ', '0'));
                }

                break;
        }
        return result.toString();

    }

    protected String hexToAscii(String hex) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            result.append((char) Integer.parseInt(str, 16));
        }


        return result.toString();
    }

    protected String binaryToAscii(String binary) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String str = binary.substring(i, i + 8);
            result.append((char) Integer.parseInt(str, 2));
        }


        return result.toString();
    }


    protected String binaryToHex(String binary, int pad) {
        String temp = "";
        String result = "";
        if (binary.length() < pad) {
            binary = String.format("%" + pad + "s", binary).replace(' ', '0');
        }

        for (int i = 0; i < binary.length(); i++) {
            temp += binary.charAt(i);
            if (temp.length() % 8 == 0) {

                result += String.format("%2s", Integer.toString(Integer.parseInt(temp, 2), 16)).replace(' ', '0');
                temp = "";

            }
        }
        return result;
    }


    protected String hexToBinary(String hex, int pad) {
        String temp = "";
        String result = "";
        if (hex.length() < (pad / 4)) {
            hex = String.format("%" + (pad / 4) + "s", hex).replace(' ', '0');
        }

        for (int i = 0; i < hex.length(); i++) {
            temp += hex.charAt(i);
            if (temp.length() % 2 == 0) {

                result += String.format("%8s", Integer.toString(Integer.parseInt(temp, 16), 2)).replace(' ', '0');
                temp = "";

            }
        }
        return result;
    }

}