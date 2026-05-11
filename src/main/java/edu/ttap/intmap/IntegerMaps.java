package edu.ttap.intmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class for integer maps
 */
public class IntegerMaps {
    /**
     * @field letterMap which is an array of array lists that contain pairs
     * @field size is the total amount of pairs in letterMap
     */
    private static class LetterCounter {
        private class Pair {
            private char key;
            
            private int value;

            /**
             * creates a pair object with value one
             * @param key the character key associated with the value
             */
            public Pair(char key) {
                this.key = key;
                value = 1;
            }

            /**
             * creates a pair object with given key and value
             * @param key the character key associated with the value
             * @param value an integer value
             */
            public Pair(char key, int value) {
                this.key = key;
                this.value = value;
            }

            /**
             * @return character key from pair
             */

            public char getKey() {
                return key;
            }

            /**
             * @return integer value from pair 
             */
            public int getValue() {
                return value;
            }

            /**
             * @param key character that could be the key 
             * @return boolean, true if character is the key
             */
            public boolean hasKey(char key) {
                return this.key == key;
            }
            
            /**
             * @return incremented value
             */
            public int increment() {
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
            // cannot create an array of a perameterized type, instead create an array of an
            // unparameterized type and cast it
            letterMap = (ArrayList<Pair>[]) new ArrayList[10];
            size = 0;
        }

        /**
         * see return
         * 
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
         * This private function is called whenever a new element is added to the map.
         * If the load function of the map exceeds arbitrary 0.75 threshold, 
         * then the backing array is expanded, and elements are recategorized
         */
        @SuppressWarnings("unchecked")
        private void expandArray() {
            if ((((float) size) / letterMap.length) > 0.75) {
                ArrayList<Pair>[] x2size = (ArrayList<Pair>[]) new ArrayList[letterMap.length * 2];
                for (ArrayList<Pair> i : letterMap) {
                    if (i != null) {
                        for (Pair j : i) {
                            int index = j.getKey() % x2size.length;
                            if (x2size[index] == null) {
                                x2size[index] = new ArrayList<Pair>();
                            }
                            x2size[index].add(j);
                        }
                    }
                }
                letterMap = x2size;
            }
        }

        /**
         * If key is present in letterMap replaces existing pair otherwise adds pair to
         * map
         * 
         * @param ch the charater key we look for in the map
         * @param v  the value attached to the key
         */
        public void put(char ch, int v) {
            int index = ch % letterMap.length;
            Pair newPair = new Pair(ch, v);
            if (letterMap[index] == null) {
                letterMap[index] = new ArrayList<>();
            }
            ArrayList<Pair> curList = letterMap[index];
            for (int i = 0; i < curList.size(); i++) {
                if (curList.get(i).hasKey(ch)) {
                    curList.set(i, newPair);
                    return;
                }
            }
            curList.add(newPair);
            size++;
            expandArray();
        }

        /**
         * @param ch the key we search for in map
         * @return the value associated with given ch key, if key does not exist, throws
         *         error
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
         * Increments the value at a given key. If the key is not in the map, return
         * false.
         * 
         * @param key key we search for in map
         */
        public void increment(char key) {
            int index = key % letterMap.length;
            if (letterMap[index] == null) {
                letterMap[index] = new ArrayList<>();
            }
            ArrayList<Pair> curList = letterMap[index];
            for (int i = 0; i < curList.size(); i++) {
                if (curList.get(i).hasKey(key)) {
                    curList.get(i).increment();
                    return;
                }
            }
            letterMap[index].add(new Pair(key));
            size++;
            System.err.println("check 1.1: before expand array");
            expandArray();
            System.err.println("check 1.2: after expand array");
        }

        /**
         * @return set of all keys in this map
         */
        public Set<Character> keySet() {
            Set<Character> setofkeys = new TreeSet<>();
            for (ArrayList<Pair> i : letterMap) {
                if (i != null) {
                    for (Pair j : i) {
                        setofkeys.add(j.getKey());
                    }
                }
            }
            return setofkeys;
        }

    }

    /**
     * Creates a scanner given a file name, or exits with an error message
     * 
     * @param path the given file to be read
     * @return A scanner that can read from the given file
     */
    public static Scanner makeScanner(String path) {
        try {
            File curFile = new File(path);
            Scanner text = new Scanner(curFile);
            return text;
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found, please try again");
            System.exit(1);
        }
        return null;
    }

    /**
     * Returns the amount of each letter in the given string
     * 
     * @param s a string whose letters will be counted
     * @param counts is an instance of the LetterCounter class, 
     *               which acts as a map that counts letters
     */
    private static void reportCountsH(String s, LetterCounter counts) {
        char[] sArray = s.toCharArray();
        for (char i : sArray) {
            counts.increment(i);
        }
    }

    /**
     * Counts the quantity of the 26 alphabetical letters (case insensitive) in a
     * given file. Then prints the results to stdout
     * 
     * @param path the file to be read from
     */
    public static void reportCounts(String path) {
        Scanner text = makeScanner(path);
        LetterCounter recipt = new LetterCounter();
        System.err.println("check 1: after new letter counter");
        while (text.hasNextLine()) {
            reportCountsH(text.nextLine(), recipt);
        }
        System.err.println("check 2: after report countsH");
        text.close();

        Set<Character> ofKeys = recipt.keySet();
        for (char i : ofKeys) {
            System.out.println(i + ": " + recipt.get(i));
        }

    }

    /**
     * @param path is a file name to be read from
     * @return the number of unique characters in a file
     */
    public static int countChars(String path) {
        Scanner text = makeScanner(path);
        Set<Character> chars = new TreeSet<>();
        while (text.hasNext()) {
            for (char i : text.next().toCharArray()) {
                chars.add(i);
            }
        }
        return chars.size();
    }


    /**
     * The main driver for the IntegerMaps program.
     * @param args the driver's command-line arguments
     */
    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Usage: java IntegerMaps <String path>");
            System.exit(1);
        }
        System.err.println("check 0: before report counts");
        reportCounts(args[0]);
        System.out.println("Number of unique characters in " 
                            + args[0] 
                            + ": " 
                            + countChars(args[0]));
    }
}
