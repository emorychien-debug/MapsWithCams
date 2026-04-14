package edu.ttap.intmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class IntegerMaps {
    /**
     * @field letterMap which is an array of array lists that contain pairs
     * @field size is the total amount of pairs in letterMap
     */
    private class LetterCounter {
        private class Pair {
            private char key;
            private int value;

            public Pair (char key){
                this.key = key;
                value = 0;
            }

            public Pair (char key, int value){
                this.key = key;
                this.value = value;
            }

            public char getKey() {
                return key;
            }

            public int getValue() {
                return value;
            }

            public boolean hasKey(char key) {
                return this.key == key;
            }

            public int increment(){
                return ++value;
            }
        }
        
        private ArrayList<Pair>[] letterMap;
        private int size;

        
        /**
         * initalizes LetterCounter
         */
        @SuppressWarnings("unchecked")
        public LetterCounter() {
            // cannot create an array of a perameterized type, instead create an array of an unparameterized type and cast it
            letterMap = (ArrayList<Pair>[]) new ArrayList[10];
            size = 0;
        }

        /**
         * see return
         * @param key the value that we look for in the map
         * @return returns true if a pair in map contains the given key
         */
        public boolean hasKey(char key) {
            int index = key % letterMap.length;
            for (Pair i : letterMap[index]) {
                if (i.hasKey(key)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * If key is present in letterMap replaces existing pair otherwise adds pair to map
         * @param ch the charater key we look for in the map
         * @param v the value attached to the key
         */
        public void put(char ch, int v) {
            int index = ch % letterMap.length;
            Pair newPair = new Pair(ch, v);
            ArrayList<Pair> curList = letterMap[index];
            for (int i = 0; i < curList.size(); i++) {
                if (curList.get(i).hasKey(ch)) {
                    curList.set(i, newPair);
                    return;
                }
            }
            curList.add(newPair);
            size++;
        }

        /**
         * 
         * @param ch the key we search for in map
         * @return the value associated with given ch key, if key does not exist, throws error
         */
        public int get(char ch) {
            int index = ch % letterMap.length;
            for (Pair i : letterMap[index]) {
                if (i.hasKey(ch)) {
                    return i.getValue();
                }
            }
            throw new IllegalArgumentException();
        }
        
        /**
         * Increments the value at a given key. If the key is not in the map, return false.
         * @param key key we search for in map
         * @return true if was able to increment value
         */
        public boolean increment(char key) {
            int index = key % letterMap.length;
            ArrayList<Pair> curList = letterMap[index];
            for (int i = 0; i < curList.size(); i++) {
                if(curList.get(i).hasKey(key)) {
                    curList.get(i).increment();
                    return true;
                }
            } 
            return false;
        }

    }
    /**
     * Creates a scanner given a file name, or exits with an error message
     * @param path the given file to be read
     * @return A scanner that can read from the given file
     */
    public static Scanner makeScanner(String path) {
        try {
            File curFile = new File(path);
            Scanner text = new Scanner(curFile);
            return text;
        } catch(FileNotFoundException e) {
            System.err.println("File Not Found, please try again");
            System.exit(1);
        }
       return null;
    }

    /**
     * Returns the amount of each letter in the given string
     * @param s a string whose letters will be counted
     * @return an array containing the amount of each character in the string
     */
    private static void reportCountsH(String s, int[] counts) {
        char[] sArray = s.toCharArray();
        for (char i : sArray) {
            int check = Character.toLowerCase(i) - 'a';
            if (check < 26 && check >= 0){
                counts[check]++;
            }
        }
    }
    /**
     * Counts the quantity of the 26 alphabetical letters (case insensitive) in a given file. Then prints the results to stdout
     * @param path the file to be read from
     */
    public static void reportCounts(String path) {
        Scanner text = makeScanner(path);
        int[] count = new int[26];
        while (text.hasNextLine()){
            reportCountsH(text.nextLine(), count);
        }
            text.close();
        for (int i = 0; i < 26; i++) {
            System.out.println((char)('a' + i) + ": " + count[i]);
        }
    }

    /**
     * @param path is a file name to be read from
     * @return the number of unique characters in a file
     */
    public static int countChars(String path) {
        Scanner text = makeScanner(path);
        Set<Character> chars = new TreeSet<>();
        while (text.hasNext()){
            for (char i : text.next().toCharArray()) {
                chars.add(i);
            }
        }
        return chars.size();
    }

    public static void main(String args[]){
        if(args.length != 1) {
            System.err.println("Usage: java IntegerMaps <String path>");
            System.exit(1);
        }

        reportCounts(args[0]);
        System.out.println("Number of unique characters in " + args[0] + ": " + countChars(args[0]));
    }
}
