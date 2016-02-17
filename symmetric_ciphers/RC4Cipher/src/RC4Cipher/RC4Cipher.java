/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RC4Cipher;

import java.io.FileNotFoundException;

/**
 *
 * @author Jeff
 */
public class RC4Cipher {
    
    public static void main(String[] args) throws FileNotFoundException {
     
        String result = "";
        String key = "key";
        String cipher = "hello";
        
        
        RC4 encryptRC4 = new RC4(key, cipher);
        result = encryptRC4.encode();
        System.out.println(result);
        
        
        cipher = result;
        RC4 decryptRC4 = new RC4(key, cipher);
        result = decryptRC4.decode();
        System.out.println(result);
    
    }
    
}
