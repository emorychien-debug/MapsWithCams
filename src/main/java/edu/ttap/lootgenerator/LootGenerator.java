package edu.ttap.lootgenerator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {
    /* list of things to fix:
     *   have error with useful messages instead of indexoutofbounds exceptions for TC processing
     *   have useful error messages for armor item processing
     *   have useful error messages for monster processing
     */

    private class monster {
        private String monClass;
        private String monType;
        private int monLevel;
        private String monTC;

        /**
         * constructs random monster object from monstats file in given directory
         * @param DATA_SET string representation of directory
         */
        public monster (Scanner ourMonster) {
            //initializes monster fields
            this.monClass = ourMonster.next();
            this.monType = "";
            while(!ourMonster.hasNextInt()) {
                this.monType += ourMonster.next(); 
            }
            this.monLevel = ourMonster.nextInt();
            this.monTC = ourMonster.nextLine();

            ourMonster.close();
        }

    }

    private class armorStats {
        private int min;
        private int max;

        public armorStats(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

    }

    
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/small";

    List<monster> allMonsters = new ArrayList<>();
    HashMap<String, armorStats> allArmor = new HashMap<>();
    HashMap<String, ArrayList<String>> allTCs = new HashMap<>();
    
    /**
     * determines if a token is the start of a treasure class or if it is not
     * @param word the token to be checked to see if it is the start of another treasure class
     * @return 0 if the token is not the start of another treasure class. 
     *         1 if the token is the start of a treasure class containing only other treasure classes.
     *         2 if the token is the start of a treasure class containing only items
     */
    private static int startsTC(String word) {
        switch(word) {
            case "Act":
            case "Quill":
            case "Diablo":
            case "Swarm":
            case "Trapped":
                return 1;
            default:
                if(word.length() >= 5 && word.substring(0,4).equals("armo")) {
                    return 2; //Return true if it starts with armo, useful for determining if the TC contains TCs or items
                } else {
                    return 0;
                }
        }
    }

    /**
     * creates a list of treasure classes to be stored in a hash map
     * @param tokens a series of strings to be interpreted as treasure classes
     * @param curIndex since not all tokens belong to treasure classes that should be stored in this list, 
     *                 curIndex indeicates the correct starting location to begin examining strings
     * @return a list of treasure classes represented as larger strings 
     */
    private ArrayList<String> makeTClist(String[] tokens, int curIndex) {
        ArrayList<String> subTCs = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            String TCbuilding = tokens[curIndex++];
            while(startsTC(tokens[curIndex]) == 0) {
                TCbuilding += " " + tokens[curIndex++];
            }
            subTCs.add(TCbuilding);
        }
        return subTCs;
    }

    /**
     * Checks for a series of items whose names are part of other items. 
     * @param itemName an item (represented as a string) to be checked
     * @return if this item has been flagged by the programmer as problematic
     */
    private boolean isProblematicItem(String itemName) {
        switch(itemName) {
            case "Wyrmhide":
            case "Boneweave":
            case "Crown":
                return true;
            default:
                return false;
        }
    }

    /**
     * Function specific to parsing TCs.
     * Interprets a series of tokens as armor items to be stored in the allTCs hash map.
     * @param tokens a series of strings which will be interpreted as items
     * @param curIndex Since not all indicies contain item-related tokens (as compared to key-related tokens), 
     *                 this is an index for the starting point of the useful tokens
     * @return
     */
    private ArrayList<String> makeArmorList(String[] tokens, int curIndex) {
        ArrayList<String> items = new ArrayList<>();
        String item = tokens[curIndex];
        for(int i = 0; i < 3; i++) {
            while(!allArmor.containsKey(item)) {
                item += " " + tokens[curIndex++];
            }
            if(isProblematicItem(item) && (tokens[curIndex].equals("Shield") || tokens[curIndex].equals("Boots"))) {
                item += tokens[curIndex++];
            }
            items.add(item);
        }
        return items;
    }
    /**
     * Adds TC in given line to the allTCs hash map.
     * must have a made the armor map before calling this function.
     * all armor in all TCs parsed by this function must be in the armor map.
     * ALL ARMOR MUST BE SPELLED THE SAME (CASE SENSITIVE)
     * @param line A string containing a complete treasure class (probably from a "TreasureClassEx.txt" file)
     */
    private void parseTCLine(String line) {
        // identify the key
        String[] tokens = line.split(" ");
        String keyTC = tokens[0];
        int curIndex = 1;
        while(startsTC(tokens[curIndex]) == 0) {
            keyTC += " " + tokens[curIndex++]; // incrment curIndex after we pull a new character
        }
        // create an ArrayList of TCs
        if(startsTC(tokens[0]) == 1) {
            allTCs.put(keyTC, makeTClist(tokens, curIndex));  
        } else if(startsTC(tokens[0]) == 2) {
            allTCs.put(keyTC, makeArmorList(tokens, curIndex));
        } else {
            System.err.println("Inproper treasure class file formatting");
            System.exit(1);
        }
    }

    /**
     * adds an entire file's worth of TCs into the allTCs hash map. 
     * TCs must only contain items found in the allArmor hash map or other TCs.
     * all armor in all TCs parsed by this function must be in the armor map.
     * ALL ARMOR MUST BE SPELLED THE SAME (CASE SENSITIVE)
     */
    private void parseTCs() {
        Scanner TCfile = new Scanner(DATA_SET + "/TreasureClassEx.txt");
        while(TCfile.hasNextLine()) {
            parseTCLine(TCfile.nextLine());
        }
    }

    /**
     * checks the "monstats.txt" file in the DATA_SET directory.
     * Interperets the monster file and places data in the monster class.
     * constructs a list containing monster objects from the monster file.
     * 
     */
    private void fillMonList() {
        Scanner monsterFile = new Scanner(DATA_SET + "/monstats.txt");
        while(monsterFile.hasNextLine()) {
            allMonsters.add(new monster(monsterFile));
        }
        monsterFile.close();
    } 

    /**
     * checks "armor.txt" file in the DATA_SET directory
     * interprets min and max stats data into armorStats class
     * creates hash map containing all armor items and their corresponding armorStats objects.
     * armor items act as keys and are represented by strings.
     */
    private void fillArmorMap() {
        Scanner armorFile = new Scanner(DATA_SET + "/armor.txt");
        while(armorFile.hasNextLine()) {
            String key = "";
            key = armorFile.next();
            while(!armorFile.hasNextInt()) {
                key += " " + armorFile.next();
            }
            int min = armorFile.nextInt();
            allArmor.put(key, new armorStats(min, armorFile.nextInt()));
        }
    }
    
    public static void main(String[] args) {
        System.out.println("This program kills monsters and generates loot!");
        // TOOD: Implement me!
    }
}
