/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimpleDESCipher;

import java.io.FileNotFoundException;

/**
 *
 * @author Jeff
 */
public class SimpleDESCipher {
    
    public static void main(String[] args) throws FileNotFoundException {
   
        String[] key = new String[]{"e0be6e662267", "e0be6e762267"};
        String[] key2 = new String[]{"e0be6e762267", "e0be6e662267"};
        String result = "";
        
        DES encryptDES = new DES("hello", key);
      
    
        result = encryptDES.beginEncrypt();
        System.out.println("Encrypted hex: " + result);

        
        DES decryptDES = new DES("00001068656c6c6e", key2);
        
        
        result = decryptDES.beginDecrypt();
        System.out.println("Decrypted Message: " + result);
    
    }
    
}
