package edu.ttap.maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * A substitution cipher is a simple encryption scheme that associates each
 * letter of the alphabet with a different letter.
 */
public class SubstitutionCipher {

  
    /**
     * Creates a substitution cipher by reading a mapping of characters from the given
     * file. Each mapping of the file should be of the form "a b", where 'a' is mapped to
     * 'b' in the cipher. We require 
     * @param filename the name of the file containing the mapping
     * @return the cipher as a mapping between characters
     */
    public static Map<Character, Character> createCipher(String filename) {
        Map<Character, Character> cipherMap = new AssociationList<>();
        File cipherSource = new File(filename);

        try (Scanner cipherReader = new Scanner(cipherSource)) {
            while (cipherReader.hasNextLine()) {
                String mapping = cipherReader.nextLine();
                if (mapping.length() != 3){//this is not always true, on windows you might get a newline
                    System.err.println ("Cannot load cipher");
                    System.exit(1);
                }
                cipherMap.put (mapping.charAt(0), mapping.charAt(2));
            }
        } catch (FileNotFoundException b) {
            System.err.println ("File not found");
        }
        return cipherMap;
    }

    /**
     * Determines whether the given mapping is a valid substitution cipher. A cipher is
     * valid if (a) it maps every letter of the alphabet (a–z) and (b) it is a bijection,
     * i.e., no two letters map to the same letter (so that we can roundtrip encode/decode
     * a message without loss of fidelity).
     * @param cipher
     * @return true iff the given mapping is a valid substitution cipher
     */
    public static boolean isValidCipher(Map<Character, Character> cipher) {
        if (cipher.size() != 26) {
            return false;
        }
        boolean[] check = new boolean[26];
        Character value;
        for (char i = 'a'; i <= 'z'; i++) {
            value = cipher.get(i);
            if (value == null) { // if value is null, then the character we are checking is not in the map, return false
                return false;
            } else if (check[(int)(value - 'a')]){ // if check[value-a] is true, then we have already seen this character. We should only have one of each, so return false
                return false;
            } else { // if we have not seen this character before, mark that we have seen it in the check boolean array
                check[(int)(value - 'a')] = true;
            }
        }
        return true;
    }

    /**
     * Given a valid substitution cipher, produces the inverse mapping, which
     * can be used to decode the encoded massage. For example, if the cipher
     * maps 'a' to 'b', then the inverse mapping should map 'b' to 'a'.
     * @param cipher the cipher to invert
     * @return the inverse mapping of the given cipher
     */
    public static Map<Character, Character> invertCipher(Map<Character, Character> cipher) {
        Map<Character, Character> inverse = new AssociationList<>();
        for(Entry<Character, Character> pair : cipher.entrySet()){
            inverse.put(pair.getValue(), pair.getKey());
        }
        return inverse;
    }

    /**
     * Translates the given string using the provided mapping.
     * @param s the string to translate
     * @param mapping the mapping to use
     * @return the translated string
     */
    public static String translate(String s, Map<Character, Character> mapping) {
        char[] toTranslate = s.toCharArray();
        for(int i = 0; i < toTranslate.length; i++) {
            Character curchar = mapping.get(toTranslate[i]);
            if(curchar != null){
                toTranslate[i] = curchar;
            }
        }
        return new String(toTranslate);
    }

    /**
     * The main driver for the substitution cipher program.
     * @param args the driver's command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(
                "Usage: java SubstitutionCipher <encode|decode> <cipherfile> <filename>");
            System.exit(1);
        }

        Map<Character, Character> cipher;
        switch(args[0]) {
            case "encode":
                cipher = createCipher(args[1]);
                break;
            case "decode":
                cipher = createCipher(args[1]);
                cipher = invertCipher(cipher);
                break;
            default:
                System.err.println("Usage: java SubstitutionCipher <encode|decode> <cipherfile> <filename>");
                System.exit(1);
                return;
        }

        if(!isValidCipher(cipher)){
            System.err.println("Invalid cipher");
            System.exit(1);
        }
        System.out.println("check point");
        Scanner text = new Scanner(args[2]);
        System.out.println("check point 2");
        System.out.println(translate(text.toString(), cipher));
        text.close();
    }
}
