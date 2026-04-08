package edu.ttap.maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
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
                if (mapping.length() != 4){
                    System.err.println ("Cannot load cipher");
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
        for (int i = 0; i < 26; i++) {
            cipher.hasKey();
        }
        throw new UnsupportedOperationException("Unimplemented method 'isValidCipher'");
    }

    /**
     * Given a valid substitution cipher, produces the inverse mapping, which
     * can be used to decode the encoded massage. For example, if the cipher
     * maps 'a' to 'b', then the inverse mapping should map 'b' to 'a'.
     * @param cipher the cipher to invert
     * @return the inverse mapping of the given cipher
     */
    public static Map<Character, Character> invertCipher(Map<Character, Character> cipher) {
        // TODO: implement me!
        throw new UnsupportedOperationException("Unimplemented method 'invertCipher'");
    }

    /**
     * Translates the given string using the provided mapping.
     * @param s the string to translate
     * @param mapping the mapping to use
     * @return the translated string
     */
    public static String translate(String s, Map<Character, Character> mapping) {
        // TODO: implement me!
        throw new UnsupportedOperationException("Unimplemented method 'translate'");
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
        // TODO: finish implementing me!
    }
}
