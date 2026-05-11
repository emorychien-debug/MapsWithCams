package edu.ttap.lootgenerator;

/**
 * This class is used to store values relating to the maximum and minimum defense an armor piece
 * can have.
 */
public class ArmorStats {
    private int min; 
                
    private int max;

    /**
     * Constructor for armorstats class
     * @param min the minimum defense that the armor can have
     * @param max the maximum defense that the armor can have
     */
    public ArmorStats(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return the minimum defense of the armor piece
     */
    public int getMin() {
        return min;
    }

    /**
     * @return the maximum defense of the armor piece.
     */
    public int getMax() {
        return max;
    }
}
