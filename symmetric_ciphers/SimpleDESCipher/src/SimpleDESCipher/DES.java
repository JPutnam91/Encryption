/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimpleDESCipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Jeff
 */
public class DES extends CipherMethods {


    private int counter = 0;
    private String cipherText;
    private String keyString;
    private String[] keyArray;
    private String finalMessage = "";
    //Binary array start at index 1 not 0;
    private int[] binaryArray64 = new int[65];
    //Step 2-3.2 arrays start at index 1 not 0
    private final int[] left = new int[33];
    private final int[] right = new int[33];
    private int[] rightExpanded = new int[49];
    //Step 3.2 array that holds the expansion table
    private final int[] binaryKey = new int[48];
    //2D array for step 3.3
    private final int[][] SArray = new int[4][16];
    //Resuting  array after un-expanding the 48 bit array
    private int[] rightUnExpanded = new int[33];//after the s tables

    public DES(String cipher, String[] key) throws FileNotFoundException {

        
        cipherText = cipher;
        keyArray = key;
        keyString = keyArray[0];
        keyToBinary(keyString);
    }

 

    //********************************************************************
    //  Converts input to binary.
    //********************************************************************
    private void messageToBinary(String cipherText) throws FileNotFoundException {

        String binaryString = asciiConvert(cipherText, 2);
        if(binaryString.length() <  64){
            binaryString = String.format("%64s", binaryString).replace(' ', '0');
        }
      

        for (int i = 1; i < binaryArray64.length; i++) {
            binaryArray64[i] = Integer.parseInt(binaryString.substring(i - 1, i));

        }

    }
    
    private void hexMessageToBinary(String cipherText) throws FileNotFoundException {

        String binaryString = hexToBinary(cipherText, 64);
        
      

        for (int i = 1; i < binaryArray64.length; i++) {
            binaryArray64[i] = Integer.parseInt(binaryString.substring(i - 1, i));

        }

    }

    //********************************************************************
    //  Converts input key to binary.
    //********************************************************************
    private void keyToBinary(String keyInput) throws FileNotFoundException {

        String binaryString = hexToBinary(keyInput, 0);

        for (int i = 0; i < binaryKey.length; i++) {
            binaryKey[i] = Integer.parseInt(binaryString.substring(i, i + 1));

        }

    }

    //********************************************************************
    //  Begins initial permutation.
    //********************************************************************
    public String beginEncrypt() throws FileNotFoundException {//encrypts given string and returns a hex string
        int b = 0;
        int[] keyArray64 = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54,
            46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43,
            35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

        
        messageToBinary(cipherText);
        int[] sortedArray64 = new int[65];

        binaryArray64 = permutateArray(binaryArray64, keyArray64, sortedArray64);

       
        splitArray();
        return binaryToHex(finalMessage, 16);

    }
    
    
    public String beginDecrypt() throws FileNotFoundException {//decrypts given hex string and returns a string
        int b = 0;
        int[] keyArray64 = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54,
            46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43,
            35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

        
        hexMessageToBinary(cipherText);
        int[] sortedArray64 = new int[65];

        binaryArray64 = permutateArray(binaryArray64, keyArray64, sortedArray64);

       
        splitArray();
           return binaryToAscii(finalMessage);
    }

    //********************************************************************
    //  Splits binary array into left and right.
    //********************************************************************
    private void splitArray() throws FileNotFoundException {

        int b = 1;
        for (int i = 1; i < binaryArray64.length; i++) {
            if (i <= 32) {
                left[i] = binaryArray64[i];
            } else if (i > 32) {
                right[b] = binaryArray64[i];
                b++;

            }

        }

       

        expandRight();
    }

    //********************************************************************
    //  Expands right array using 48 bit permutation table.
    //********************************************************************
    private void expandRight() throws FileNotFoundException {
        int b = 0;

        int[] expansionTable = new int[]{32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
        };

        rightExpanded = permutateArray(right, expansionTable, rightExpanded);

        

        exclusiveOr();
    }

    //********************************************************************
    //  Uses exclusive or to compare the expanded right array with 48 bit key.
    //********************************************************************
    private void exclusiveOr() throws FileNotFoundException {
        for (int i = 0; i < binaryKey.length; i++) {
            rightExpanded[i + 1] = rightExpanded[i + 1] ^ binaryKey[i];
        }

       
        Unexpand();
    }

    //********************************************************************
    //  Un-Expands right 48 bit array.
    //********************************************************************
    private void Unexpand() throws FileNotFoundException {

        int a = 0;
        int b = 1;

        int[] temp = new int[6];

        for (int i = 1; i < rightExpanded.length; i++) {
            temp[a] = rightExpanded[i];

            if (a == 5) {
                getCoords(temp, b);
                a = -1;
                b++;
            }
            a++;
        }
      
        secondPermutation();
    }

    //********************************************************************
    //  Fills the correct SArray for the sets of 6 bits.
    //********************************************************************
    private void fillSArray(int a) throws FileNotFoundException {

        Scanner scanS = new Scanner(new File("s" + a + ".txt"));

        for (int f = 0; f < 4; f++) {
            for (int g = 0; g < 16; g++) {
                SArray[f][g] = scanS.nextInt();

            }
        }

    }

    //********************************************************************
    //  Gets the coordinates X and Y for the SArray.
    //********************************************************************
    private void getCoords(int[] temp, int b) throws FileNotFoundException {
        String x, y;
        int X, Y;
        int sCounter = b;
        int result;
        int indexCounter = 1;
        String tempStr;

        x = Integer.toString(temp[0]);
        y = Integer.toString(temp[1]);
        y += Integer.toString(temp[2]);
        y += Integer.toString(temp[3]);
        y += Integer.toString(temp[4]);
        x += Integer.toString(temp[5]);
        X = Integer.parseInt(x, 2);
        Y = Integer.parseInt(y, 2);

        switch (sCounter) {

            case 1:
                fillSArray(sCounter);
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 2:
                fillSArray(sCounter);
                indexCounter = 5;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 3:
                fillSArray(sCounter);
                indexCounter = 9;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 4:
                fillSArray(sCounter);
                indexCounter = 13;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 5:
                fillSArray(sCounter);
                indexCounter = 17;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 6:
                fillSArray(sCounter);
                indexCounter = 21;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 7:
                fillSArray(sCounter);
                indexCounter = 25;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
            case 8:
                fillSArray(sCounter);
                indexCounter = 29;
                result = SArray[X][Y];
                tempStr = Integer.toString(result, 2);
                fillArray(tempStr, indexCounter);

                break;
        }

    }

    //********************************************************************
    //  Converts SArry results to binary, and puts it in the 32 bit array.
    //********************************************************************
    private void fillArray(String value, int start) {

        int a, b, c, d;
        value = String.format("%4s", value).replace(' ', '0');
        a = Integer.parseInt(value.substring(0, 1));
        b = Integer.parseInt(value.substring(1, 2));
        c = Integer.parseInt(value.substring(2, 3));
        d = Integer.parseInt(value.substring(3, 4));

        rightUnExpanded[start] = a;
        start++;
        rightUnExpanded[start] = b;
        start++;
        rightUnExpanded[start] = c;
        start++;
        rightUnExpanded[start] = d;

    }

    //********************************************************************
    //  Runs the un-Expanded Right array through the permutation table.
    //********************************************************************
    private void secondPermutation() throws FileNotFoundException {
        int b = 0;
        int[] sortedArray32 = new int[33];
        int[] keyArray32 = new int[]{16, 7, 20, 21, 29, 12, 28, 17, 
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9, 
        19, 13, 30, 6, 22, 11, 4, 25};
      
        rightUnExpanded = permutateArray(rightUnExpanded, keyArray32, sortedArray32);

        

        exclusiveOrLeftRight();

    }

    //********************************************************************
    //  Compares left array and right un-expanded array using exclusive or.
    //********************************************************************
    private void exclusiveOrLeftRight() throws FileNotFoundException {
        for (int i = 1; i < rightUnExpanded.length; i++) {
            rightUnExpanded[i] = rightUnExpanded[i] ^ left[i];
        }

        
        combine32();
    }

    //********************************************************************
    //  Combines the right array with the right un-expanded array after the exclusive or into the 64 bit binary array.
    //********************************************************************
    private void combine32() throws FileNotFoundException {

        for (int i = 1; i < binaryArray64.length; i++) {

            if (i <= 32) {
                binaryArray64[i] = right[i];
            } else if (i > 32) {
                binaryArray64[i] = rightUnExpanded[i - 32];
            }
        }

       
        reArrange();
    }

    //********************************************************************
    //  Re-Arranges the top and bottom of the 64 bit binary array.
    //********************************************************************
    private void reArrange() throws FileNotFoundException {
        int temp;
        for (int i = 1; i < 33; i++) {
            temp = binaryArray64[i + 32];
            binaryArray64[i + 32] = binaryArray64[i];
            binaryArray64[i] = temp;
        }

       
        //********************************************************************
        //  Continues loop if there are still keys.
        //********************************************************************

        
        if(counter  == keyArray.length-1){
            inversePermutation();
        }else{
            counter++;
            keyToBinary(keyArray[counter]);
            splitArray();
        }

    }

    //********************************************************************
    //  Uses the inverse permutation table to prepare for ascii conversion.
    //********************************************************************
    private void inversePermutation() throws FileNotFoundException {
        int b = 0;
        int[] sortedInverseArray64 = new int[65];
        int[] inverseKeyArray64 = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46,
            14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19,
            59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
 


        binaryArray64 = permutateArray(binaryArray64, inverseKeyArray64, sortedInverseArray64);

        

        finalMessage = getMessage();
    }

    //********************************************************************
    //  Converts to ascii chars and prints to console.
    //********************************************************************
    private String getMessage() {

        String temp = "";

        for (int a = 1; a < binaryArray64.length; a++) {
            temp += binaryArray64[a];
        }

        return temp;
    }

    

    private int[] permutateArray(int[] original, int[] permutate, int[] temp) {
        int b;

        for (int i = 0; i < permutate.length; i++) {

            b = permutate[i];
            temp[i + 1] = original[b];
        }
        original = temp;
        return original;
    }

}
