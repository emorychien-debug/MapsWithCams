package edu.ttap.lootgenerator;

/**
 * a class used for storing the fields associated with monsters.
 */
public class Monster {
    private String monClass;

    private String monType;
    
    private int monLevel;
    
    private String monTC;

    /**
     * constructs random monster object from monstats file in given directory
     * @param ourMonster string representation of one monster
     */
    public Monster(String ourMonster) {
        String[] tokens = ourMonster.split("\t");
        monClass = tokens[0];
        monType = tokens[1];
        monLevel = Integer.parseInt(tokens[2]);
        monTC = tokens[3];
    }

    /**
     * @return a string representation of the monster's name
     */
    public String getName() {
        return monClass;
    }

    /**
     * @return a string representation of the treasure class associated with this monster
     */
    public String getTC() {
        return monTC;
    }
}

