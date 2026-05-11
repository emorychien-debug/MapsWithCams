package edu.ttap.lootgenerator.affix;

/**
 * abstract class that contains fields and methods useful for applying affixes
 */
public abstract class Affix {
    private String name; 
    
    private String effect;

    private int maxEffect;
    
    private int minEffect;

    /**
     * constructor for the affix class
     * @param affixLine
     */
    public Affix(String affixLine) {
        String[] info = affixLine.split("\t");
        if (info.length != 4) {
            System.err.println("Affix file has incorrect formatting."
                              + "All fields must be separated by a tab, stupid");
            System.err.println("Number of fields detected: " + info.length);
            System.exit(1);
        }
        name = info[0];
        effect = info[1];
        maxEffect = Integer.parseInt(info[3]);
        minEffect = Integer.parseInt(info[2]);
    }

    /**
     * @return a string representation of the name of the affix
     */
    public String getName() {
        return name;
    }

    /**
     * @return a string represenation of the effect given by the affix
     */
    public String getEffect() {
        return effect;
    } 

    /**
     * @return the min value of the effect given by the affix
     */
    public int getMin() {
        return minEffect;
    }

    /**
     * @return the max value of the effect given by the affix
     */
    public int getMax() {
        return maxEffect;
    }
}

