package edu.ttap.intmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IntegerMaps {

    /**
     * Returns the amount of each letter in the given string
     * @param s a string whose letters will be counted
     * @return an array containing the amount of each character in the string
     */
    public static void countChars(String s, int[] counts) {
        char[] sArray = s.toCharArray();
        for (char i : sArray) {
            int check = Character.toLowerCase(i) - 'a';
            if (check < 26 && check >= 0){
                counts[check]++;
            }
        }
    }

    public static void main(String args[]){
        if(args.length != 1) {
            System.err.println("Usage: java IntegerMaps <String path>");
            System.exit(1);
        }
        try {
            File curFile = new File(args[0]);
            Scanner text = new Scanner(curFile);
            int[] count = new int[26];
            while (text.hasNextLine()){
                countChars(text.nextLine(), count);
            }
            text.close();

            for (int i = 0; i < 26; i++) {
                System.out.println((char)('a' + i) + ": " + count[i]);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found, please try again");
            System.exit(1);
        }

    }
    
}
