/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AESCipher;

/**
 *
 * @author Jeff
 */
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AESCipher {

    public static void main(String[] args) throws FileNotFoundException {
        String[] messageArray;
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter some text: ");
       
            String message = scan.nextLine();
            AES cipher = new AES("4142434445464748494a4b4c4d4e4f50");

            if (message.length() > 16) {
                messageArray = messageBreakup(message);
                encryptArray(cipher, messageArray);
                decryptArray(cipher, messageArray);
                
            } else {
                message = cipher.runEncrypt(message);
                System.out.println("encrypted: " + message);
                message = cipher.runDecrypt(message);
                System.out.println("decrypted: " + message);
            }

        
        scan.close();
    }

    private static String[] messageBreakup(String message) {
        String[] messageArray;
        if (message.length() % 16 == 0) {
            messageArray = new String[message.length() / 16];
        } else {
            messageArray = new String[(message.length() / 16) + 1];
        }

        int b = 0;
        for (int a = 0; a < messageArray.length; a++) {

            if (b + 16 > message.length() - 1) {
                messageArray[a] = message.substring(b, b + (message.length() - b));
            } else {
                messageArray[a] = message.substring(b, b + 16);
                b = b + 16;
            }
        }
        return messageArray;
    }

    private static void encryptArray(AES cipher, String[] messageArray) {
        
        System.out.print("\nMessage encrypted: ");
        
           for (int a = 0; a < messageArray.length; a++) {
                    messageArray[a] = cipher.runEncrypt(messageArray[a]);
                    System.out.print(messageArray[a]);
                }
        
    }

    private static void decryptArray(AES cipher, String[] messageArray) {
        
        System.out.print("\nMessage decrypted: ");
                for (int a = 0; a < messageArray.length; a++) {
                    messageArray[a] = cipher.runDecrypt(messageArray[a]);
                    System.out.print(messageArray[a]);
                }
        
    }
}
