package edu.ttap.lootgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class LootGenerator {

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


    private void fillMonList() {
        Scanner monsterFile = new Scanner(DATA_SET + "/monstats.txt");
        while(monsterFile.hasNextLine()) {
            allMonsters.add(new monster(monsterFile));
        }
        monsterFile.close();
    } 

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
