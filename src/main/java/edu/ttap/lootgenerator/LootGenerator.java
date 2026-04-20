package edu.ttap.lootgenerator;

import java.io.File;
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
        public monster (String ourMonster) {
            String[] tokens = ourMonster.split("\t");
            monClass = tokens[0];
            monType = tokens[1];
            monLevel = Integer.parseInt(tokens[2]);
            monTC = tokens[3];
        }

        public String getName() {return monClass;}

        public String getTC() {return monTC;}
    }

    private class armorStats {
        private int min;
        private int max;

        public armorStats(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {return min;}
        public int getMax() {return max;}
    }

    private class affix {
        private String name;
        private String effect;
        private int maxEffect;
        private int minEffect;

        public affix(String affixLine) {
            String[] info = affixLine.split("\t");
            if(info.length != 4) {
                System.err.println("Affix file has incorrect formatting. All fields must be separated by a tab, stupid");
                System.err.println("Number of fields detected: " + info.length);
                System.exit(1);
            }
            name = info[0];
            effect = info[1];
            maxEffect = Integer.parseInt(info[3]);
            minEffect = Integer.parseInt(info[2]);
        }

        public String getName() {return name;}
        public String getEffect() {return effect;} 
        public int getMin() {return minEffect;}
        public int getMax() {return maxEffect;}
    }

    private class prefix extends affix {
        public prefix(String prefixLine) {
            super(prefixLine);
        }
    }

    private class suffix extends affix{
        public suffix(String suffixLine) {
            super(suffixLine);
        }
    }

    
    /** The path to the dataset (either the small or large set). */
    private static final String DATA_SET = "data/large";

    List<monster> allMonsters = new ArrayList<>();
    HashMap<String, armorStats> allArmor = new HashMap<>();
    HashMap<String, ArrayList<String>> allTCs = new HashMap<>();
    List<prefix> allPrefixes = new ArrayList<>();
    List<suffix> allSuffixes = new ArrayList<>();
    
    
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
    /*
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
    */
   private void parseTCLine(String line) {
        String[] tokens = line.split("\t");
        ArrayList<String> subTCs = new ArrayList<>();
        for(int i = 1; i < tokens.length; i++) {
            subTCs.add(tokens[i]);
        }
        allTCs.put(tokens[0],subTCs);
   }

    /**
     * adds an entire file's worth of TCs into the allTCs hash map. 
     * TCs must only contain items found in the allArmor hash map or other TCs.
     * all armor in all TCs parsed by this function must be in the armor map.
     * ALL ARMOR MUST BE SPELLED THE SAME (CASE SENSITIVE)
     */
    private void parseTCs() {
        File TCfile = new File(DATA_SET + "/TreasureClassEx.txt");
        try {
            Scanner TCscanner = new Scanner(TCfile);
            while(TCscanner.hasNextLine()) {
                parseTCLine(TCscanner.nextLine());
            }
            TCscanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find treasure class file, stupid");
            System.exit(1);
        }
    }

    /**
     * checks the "monstats.txt" file in the DATA_SET directory.
     * Interperets the monster file and places data in the monster class.
     * constructs a list containing monster objects from the monster file.
     * 
     */
    private void fillMonList() {
        File monsterFile = new File(DATA_SET + "/monstats.txt");
        try {
            Scanner monsterScanner = new Scanner(monsterFile);
            while(monsterScanner.hasNextLine()) {
                allMonsters.add(new monster(monsterScanner.nextLine()));
            }
            monsterScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find monstats file, stupid");
            System.exit(1);
        }
    } 

    /**
     * checks "armor.txt" file in the DATA_SET directory
     * interprets min and max stats data into armorStats class
     * creates hash map containing all armor items and their corresponding armorStats objects.
     * armor items act as keys and are represented by strings.
     */
    /*
    private void fillArmorMap() {
        System.out.println("Checkpoint 3.1.1");
        while(armorFile.hasNextLine()) {
            String key = "";
            key = armorFile.next();
            System.out.println("Checkpoint 3.1.1.1");
            while(!armorFile.hasNextInt()) {
                System.out.println("Checkpoint 3.1.1.1.1: " + armorFile.hasNext());
                key += " " + armorFile.next();
            }
            System.out.println("Checkpoint 3.1.1.2");
            int min = armorFile.nextInt();
            allArmor.put(key, new armorStats(min, armorFile.nextInt()));
        }
        System.out.println("Checkpoint 3.1.2");
        armorFile.close();
    }
    */
    private void fillArmorMap() {
        File armorFile = new File(DATA_SET + "/armor.txt");
        try {
            Scanner armorScanner = new Scanner(armorFile);
            while(armorScanner.hasNextLine()) {
                String[] tokens = armorScanner.nextLine().split("\t");
                if(tokens.length != 3) {
                    System.err.println("Contents of one line of armor file formatted incorrectly:");
                    for(String s : tokens) {
                        System.err.println(s);
                    }
                    System.err.println("Incorrect formatting in armor file, stupid");
                    System.exit(1);
                }
                allArmor.put(tokens[0], new armorStats(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
            }
            armorScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find armor file, stupid");
            System.exit(1);
        }
    }

    /**
     * fills the list allSuffixes with suffix objects containing their respective data
     */
    private void fillSuffixList() {
        File suffixFile = new File(DATA_SET + "/MagicSuffix.txt");
        try {
            Scanner text = new Scanner(suffixFile);
            while(text.hasNextLine()) {
                allSuffixes.add(new suffix(text.nextLine()));
            }
            text.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find suffix file, stupid");
            System.exit(1);
        }
    }

    /**
     * fills the list allFrefixes with prefix objects containing their respective data
     */
    private void fillPrefixList() {
        File prefixFile = new File(DATA_SET + "/MagicPrefix.txt");
        try {
            Scanner text = new Scanner(prefixFile);
            while(text.hasNextLine()) {
                allPrefixes.add(new prefix(text.nextLine()));
            }
            text.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find prefix file, stupid");
            System.exit(1);
        }
    }
    /**
     * the allMonsters list should be initialized 
     * @return a random monster from the allMonsters list
     */
    public monster pickRandMon() {
        Random r = new Random();
        return allMonsters.get(r.nextInt(allMonsters.size()));
    }

    public String generateLoot(monster mon) {
        String TC = mon.getTC();
        if(!allTCs.containsKey(TC)) {
            System.err.println(TC);
            System.err.println(allTCs.keySet());
            System.err.println("You incorrectly parsed your monster, stupid");
            System.exit(1);
        }
        while(allTCs.containsKey(TC)) {
            Random r = new Random();
            TC = allTCs.get(TC).get(r.nextInt(3));
        }
        if(!allArmor.containsKey(TC)) {
            System.err.println("You incorrectly parsed your armor, stupid (either in allTCs or in all Armor)");
            System.exit(1);
        }
        return TC;
    }

    public int getArmorDefense(String armor) {
        armorStats range = allArmor.get(armor);
        Random r = new Random();
        return r.nextInt(range.getMax() + 1 - range.getMin()) + range.getMin();
    }

    public affix[] generateAffixes() {
        affix[] affixes = new affix[2];
        Random b = new Random();
        if(b.nextBoolean()) {
            affixes[0] = allPrefixes.get(b.nextInt(allPrefixes.size()));
        } else {
            affixes[0] = null;
        }
        if(b.nextBoolean()) {
            affixes[1] = allSuffixes.get(b.nextInt(allSuffixes.size()));
        } else {
            affixes[1] = null;
        }
        return affixes;
    }

    private void initializeFields() {
        fillArmorMap();
        parseTCs();
        fillMonList();
        fillPrefixList();
        fillSuffixList();
    }

    public void killMonster() {
        Random r = new Random();
        monster PeterMichealOsera = allMonsters.get(r.nextInt(allMonsters.size())); 
        System.out.println("Fighting " + PeterMichealOsera.getName() + " ...");
        System.out.println("You have slain " + PeterMichealOsera.getName() + "!");
        System.out.println(PeterMichealOsera.getName() + " dropped:");
        System.out.println();
        String lootItem = generateLoot(PeterMichealOsera);
        affix[] fixPeterMichealOsera = generateAffixes();
        String[] affixNames = new String[2];
        for(int i = 0; i < 2; i++) {
            if(fixPeterMichealOsera[i] == null) {
                affixNames[i] = "";
            }
            else {
                affixNames[i] = fixPeterMichealOsera[i].getName() + " ";
            }notify();
        }
        System.out.println(affixNames[0] + lootItem + " " + affixNames[1]);
        System.out.println("Defense: " + getArmorDefense(lootItem));
        for(int i = 0; i < 2; i++) {
            if(!(fixPeterMichealOsera[i] == null)) {
                int specialValue = r.nextInt(fixPeterMichealOsera[i].getMax() + 1 - fixPeterMichealOsera[i].getMin()) + fixPeterMichealOsera[i].getMin();
                System.out.println(specialValue + " " + fixPeterMichealOsera[i].getEffect());
            }
        }
    }
    
    public static void main(String[] args) {
        LootGenerator PMSthings = new LootGenerator();
        PMSthings.initializeFields();
        boolean continuePlay = true;
        Scanner play = new Scanner(System.in);
        while (continuePlay) {
            PMSthings.killMonster();
            boolean correctInput = false;
            while(!correctInput) {
                System.out.println("Continue fighting? [Y/N]");
                String userIn = play.next().toLowerCase();
                if(userIn.equals("no")||userIn.equals("n")) {
                    continuePlay = false;
                    correctInput = true;
                } else if(userIn.equals("yes") || userIn.equals("y")) {
                    correctInput = true;
                }
            }
        }
        play.close();
    }
}
