/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RC4Cipher;

/**
 *
 * @author Jeff
 */
public class RC4 extends CipherMethods {

    private String message;
    private final int[] S = new int[256];
    private final int[] T = new int[256];
    private String key;
    private int[] K;

    public RC4(String key, String message) {
        this.key = key;
        this.message = message;
        K = new int[key.length()];

        for (int i = 0; i < K.length; i++) {
            K[i] = key.charAt(i);
        }

        genSbox();

    }

    private void genSbox() {
        int j = 0;
        int temp;
        int keyLength = key.length();

        for (int a = 0; a < S.length; a++) {
            S[a] = a;
            T[a] = Integer.parseInt(Integer.toHexString((char) K[a % (keyLength)]), 16);
        }

        for (int a = 0; a < S.length; a++) {
            j = (j + S[a] + T[a]) % 256;
            temp = S[a];
            S[a] = S[j];
            S[j] = temp;
        }

        
    }

    public String encode() {
        String result = "";
        
        int i = 0;
        int j = 0;
        String[] C = new String[message.length()];
        for (int a = 0; a < C.length; a++) {
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;
            arraySwap(S, i, j);
            int temp = (S[i] + S[j]) % 256;
            int test = message.charAt(a) ^ S[temp];
            C[a] = Integer.toHexString(message.charAt(a) ^ S[temp]);
            if(C[a].length()<2){
                C[a]="0"+C[a];
            }
            result += C[a];
        }
       return result;

    }

    public String decode() {
        message = hexToAscii(message);
        String result = "";
        int i = 0;
        int j = 0;
        char[] C = new char[message.length()];
        for (int a = 0; a < C.length; a++) {
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;
            arraySwap(S, i, j);
            int temp = (S[i] + S[j]) % 256;
            C[a] = (char) (message.charAt(a) ^ S[temp]);
            result += C[a];
        }
        return result;
    }


    private void arraySwap(int[] S, int i, int j) {
        int temp = S[i];
        S[i] = S[j];
        S[j] = temp;
    }

}
