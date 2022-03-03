import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;

/**
 * this is the code for calculate the similarity of two files.
 * @author :Xupeng Shi. Andrew ID: xupengs
 */
public class Similarity {
    /**
     * map to store all thing.
     */
    private Map<String, BigInteger> wordCount;

    /**
     * number of lines.
     */
    private int numberOfLines;

    /**
     * number of words.
     */
    private BigInteger numberOfWords;

    /**
     * number of no duplication words.
     */
    private int numberOfWordsNoDups;

    /**
     * euclideanNorm.
     */
    private double euclideanNorm;

    /**
     * constructor with String input.
     * @param string input string
     */
    public Similarity(String string) {

        wordCount = new HashMap<>();
        numberOfLines = 1;
        numberOfWords = new BigInteger("0");
        numberOfWordsNoDups = 0;
        euclideanNorm = 0;
        if (string != null) {
            String[] wordsFromText = string.split("\\W");
            for (String word : wordsFromText) {
                if (!isWord(word)) {
                    continue;
                }
                word = word.toLowerCase();
                if (!wordCount.containsKey(word)) {
                    numberOfWordsNoDups++;
                }
                numberOfWords = numberOfWords.add(BigInteger.ONE);
                wordCount.put(word, wordCount.getOrDefault(word, BigInteger.ZERO).add(BigInteger.ONE));
                BigInteger freq = wordCount.get(word);
                if (freq != BigInteger.ONE) {
                    euclideanNorm = euclideanNorm * euclideanNorm - (freq.subtract(BigInteger.ONE).
                            multiply(freq.subtract(BigInteger.ONE))).doubleValue() + freq.multiply(freq).doubleValue();

                } else {
                    euclideanNorm = Math.sqrt(euclideanNorm * euclideanNorm + 1);
                }
            }
        }

    }

    /**
     * Constructor with file input.
     * @param file input file
     */
    public Similarity(File file) {
        wordCount = new HashMap<>();
        numberOfLines = 0;
        numberOfWords = new BigInteger("0");
        numberOfWordsNoDups = 0;
        euclideanNorm = 0;
        if (file != null) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(file, "latin1");
                while (scanner.hasNextLine()) {
                    numberOfLines++;
                    String line = scanner.nextLine();
                    String[] wordsFromText = line.split("\\W");
                    for (String word : wordsFromText) {
                        if (!isWord(word)) {
                            continue;
                        }
                        word = word.toLowerCase();
                        if (!wordCount.containsKey(word)) {
                            numberOfWordsNoDups++;
                        }
                        numberOfWords = numberOfWords.add(BigInteger.ONE);
                        wordCount.put(word, wordCount.getOrDefault(word, BigInteger.ZERO).add(BigInteger.ONE));
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("Cannot find the file");
            } finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }

    }

    /**
     * number of lines.
     * @return number of lines.
     */
    public int numOfLines() {
        return numberOfLines;
    }

    /**
     * number of words.
     * @return number of words.
     */
    public BigInteger numOfWords() {
        return numberOfWords;
    }

    /**
     * number of words no duplication.
     * @return number of words no duplication.
     */
    public int numOfWordsNoDups() {
        return numberOfWordsNoDups;
    }

    /**
     * euclideanNorm.
     * @return euclideanNorm
     */
    public double euclideanNorm() {
        if (wordCount == null || wordCount.size() == 0) {
            return 0.0;
        }
        return getEuclideanNorm();
    }

    /**
     * helper function to get Euclidean.
     * I use add, multiply, doubleValue in BigInteger.
     * @return value of Euclidean
     */
    private double getEuclideanNorm() {
        BigInteger distence = new BigInteger("0");
        for (Map.Entry<String, BigInteger> entry : wordCount.entrySet()) {
            distence = distence.add(entry.getValue().multiply(entry.getValue()));
        }
        return Math.sqrt(distence.doubleValue());
    }

    /**
     * function to calculate the dot product of two maps.
     * I use add, multiply, doubleValue in BigInteger.
     * @param map map to compare with this map
     * @return dot product.
     */
    public double dotProduct(Map<String, BigInteger> map) {
        if (map == null || map.size() == 0 || wordCount == null || wordCount.size() == 0) {
            return 0.0;
        }
        BigInteger product = new BigInteger("0");
        for (Map.Entry<String, BigInteger> entry : map.entrySet()) {
            String key = entry.getKey();
            if (wordCount.containsKey(key)) {
                product = product.add(wordCount.get(key).multiply(entry.getValue()));
            }
        }
        return product.doubleValue();
    }

    /**
     * function to calculate the distance of two maps.
     * I use add, multiply, doubleValue in BigInteger. sqrt, Pi and acos funciton in Math.
     * @param map map's to compare with
     * @return distance
     */
    public double distance(Map<String, BigInteger> map) {
        if (map == null || map.size() == 0 || wordCount == null || wordCount.size() == 0) {
            return Math.PI / 2;
        }
        if (map.equals(wordCount)) {
            return 0.0;
        }
        BigInteger eucliDis = new BigInteger("0");
        double product = dotProduct(map);
        for (Map.Entry<String, BigInteger> entry : map.entrySet()) {
            eucliDis = eucliDis.add(entry.getValue().multiply(entry.getValue()));
        }
        double eu1 = euclideanNorm();
        eu1 = eu1 * Math.sqrt(eucliDis.doubleValue());
        eu1 = product / eu1;
        return Math.acos(eu1);
    }

    /**
     * helper function to determine whether the word in legal.
     * @param text input string
     * @return whether the input is legal
     */
    private boolean isWord(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[a-zA-Z]+");
    }

    /**
     * get the current map object.
     * @return current map.
     */
    public Map<String, BigInteger> getMap() {
        return new HashMap<>(wordCount);
    }
}
