/**
 * 17683 Data Structures for Application Programmers.
 * Homework Assignment 4: HashTable Implementation with linear probing.
 *
 * Andrew ID:xupengs
 * @author Xupeng Shi
 */
public class MyHashTable implements MyHTInterface {

    /**
     * deleted flag.
     */
    private static final DataItem DELETED = new DataItem("DELETED", -1);

    /**
     * init capacity.
     */
    private static final int INITCAPACITY = 10;

    /**
     * hash factor.
     */
    private static final int HASHFACTOR = 27;

    /**
     * The DataItem array of the table.
     */
    private DataItem[] hashArray;

    /**
     * item number of the hashMap.
     */
    private int size;

    /**
     * number of collision happen.
     */
    private int numOfCollision;

    /**
     * load factor of the hashMap.
     */
    private static final double LOADFACTOR = 0.5;

    /**
     * default constructor.
     */
    MyHashTable() {
        hashArray = new DataItem[INITCAPACITY];
        size = 0;
        numOfCollision = 0;

    }

    /**
     * Constructor with capacity.
     * @param initCapacity capacity of the hashMap
     */
    MyHashTable(int initCapacity) {
        if (initCapacity < 0) {
            throw new RuntimeException("init capacity should not less than 0");
        }
        hashArray = new DataItem[initCapacity];
        size = 0;
        numOfCollision = 0;

    }

    /**
     * Instead of using String's hashCode, you are to implement your own here.
     * You need to take the table length into your account in this method.
     *
     * In other words, you are to combine the following two steps into one step.
     * 1. converting Object into integer value
     * 2. compress into the table using modular hashing (division method)
     *
     * Helper method to hash a string for English lowercase alphabet and blank,
     * we have 27 total. But, you can assume that blank will not be added into
     * your table. Refer to the instructions for the definition of words.
     *
     * For example, "cats" : 3*27^3 + 1*27^2 + 20*27^1 + 19*27^0 = 60,337
     *
     * But, to make the hash process faster, Horner's method should be applied as follows;
     *
     * var4*n^4 + var3*n^3 + var2*n^2 + var1*n^1 + var0*n^0 can be rewritten as
     * (((var4*n + var3)*n + var2)*n + var1)*n + var0
     *
     * Note: You must use 27 for this homework.
     *
     * However, if you have time, I would encourage you to try with other
     * constant values than 27 and compare the results but it is not required.
     * @param input input string for which the hash value needs to be calculated
     * @return int hash value of the input string
     */
    private int hashFunc(String input) {
        if (!isWord(input)) {
            return -1;
        }
        int hashNum = hashValue(input);

        return hashNum % hashArray.length;
    }

    /**
     * doubles array length and rehash items whenever the load factor is reached.
     * Note: do not include the number of deleted spaces to check the load factor.
     * Remember that deleted spaces are available for insertion.
     */
    private void rehash() {

        numOfCollision = 0;

        int itemsToRehash = size;
        int oldLen = hashArray.length;
        int newLen = findPrime(oldLen * 2);
        DataItem[] newHashArray = new DataItem[newLen];
        DataItem[] oldHashArray = hashArray;
        hashArray = newHashArray;


        for (int i = 0; i < oldLen; i++) {
            if (oldHashArray[i] != null && oldHashArray[i] != DELETED) {
                String temp = oldHashArray[i].value;
                int hashVal = hashFunc(temp);
                boolean flag = false;
                while (hashArray[hashVal] != null) {
                    if (hashFunc(temp) == hashFunc(hashArray[hashVal].value)) {
                        flag = true;
                    }
                    hashVal++;
                    hashVal = hashVal % hashArray.length;
                }
                if (flag) {
                    numOfCollision++;
                }
                hashArray[hashVal] = oldHashArray[i];
            }

        }
        System.out.printf("Rehashing %d items, new length is %d\n", itemsToRehash, hashArray.length);
    }

    /**
     * helper function to find the next prime number.
     * @param i number to find
     * @return next prime number of i
     */
    private int findPrime(int i) {
        int vNext = i;
        while (true) {
            double vNextSqrt = Math.sqrt(i);
            boolean isPrime = true;
            for (int j = 2; j < vNextSqrt + 1; j++) {
                if (vNext % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                return vNext;
            }
            vNext++;
        }
    }

    /**
     * helper function to rehash find the next possible position to put the item.
     * @param hashVal current position
     * @param input dataItem to be inputed
     */
    private void helperFind(int hashVal, DataItem input) {
        boolean flag = false;
        while (hashArray[hashVal] != null) {
            if (hashVal == hashFunc(hashArray[hashVal].value)) {
                flag = true;
            }
            hashVal++;
            hashVal = hashVal % hashArray.length;
        }
        if (flag) {
            numOfCollision++;
        }
        hashArray[hashVal] = input;
    }

    /**
     * insert a element into the hashTable.
     * @param value String value to add
     */
    @Override
    public void insert(String value) {
        if (!isWord(value)) {
            return;
        }
        int hashVal = hashFunc(value);
        DataItem item = new DataItem(value);
        boolean flag = false;
        int counter = 0;
        int posistion = -1;

        while (hashArray[hashVal] != null) {
            if (counter > hashArray.length) {
                break;
            } else if (hashArray[hashVal].value.equals(value)) {
                hashArray[hashVal].frequency++;
                return;
            } else if (hashArray[hashVal] == DELETED) {
                if (posistion == -1) {
                    posistion = hashVal;
                }
            } else if (hashFunc(hashArray[hashVal].value) == hashFunc(value)) {
                flag = true;
            }
            counter++;
            hashVal++;
            hashVal = hashVal % hashArray.length;
        }

        if (posistion == -1) {
            hashArray[hashVal] = item;
        } else {
            hashArray[posistion] = item;
        }

        if (flag) {
            numOfCollision++;
        }

        size++;

        if ((double) size / (double) hashArray.length > LOADFACTOR) {
            rehash();
        }
//        System.out.printf("item %s is inputed\n", value);
//        display();


    }


    /**
     * Returns the size, number of items, of the table.
     * @return the number of items in the table
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Displays the values of the table.
     * If an index is empty, it shows **
     * If previously existed data item got deleted, then it should show #DEL#
     */
    @Override
    public void display() {
        for (DataItem dataItem : hashArray) {
            if (dataItem == null) {
                System.out.print("**");
            } else if (dataItem == DELETED) {
                System.out.print("#DEL#");
            } else {
                System.out.print("[" + dataItem.value + "," + " " + dataItem.frequency + "]");
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    /**
     * Returns true if value is contained in the table.
     * @param key String key value to search
     * @return true if found, false if not found.
     */
    @Override
    public boolean contains(String key) {
        int counter = 0;
        if (!isWord(key)) {
            return false;
        }
        int hashVal = hashFunc(key);
        while (hashArray[hashVal] != null) {
            if (hashArray[hashVal].value.equals(key)) {
                return true;
            }
            hashVal++;
            hashVal = hashVal % hashArray.length;
            counter++;
            if (counter > hashArray.length) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns the number of collisions in relation to insert and rehash.
     * When rehashing process happens, the number of collisions should be properly updated.
     *
     * The definition of collision is "two different keys map to the same hash value."
     * Be careful with the situation where you could overcount.
     * Try to think as if you are using separate chaining.
     * "How would you count the number of collisions?" when using separate chaining.
     * @return number of collisions
     */
    @Override
    public int numOfCollisions() {
        return numOfCollision;
    }

    /**
     * Returns the hash value of a String.
     * Assume that String value is going to be a word with all lowercase letters.
     * @param value value for which the hash value should be calculated
     * @return int hash value of a String
     */
    @Override
    public int hashValue(String value) {
        int len = value.length();
        int hashNum = 0;
        for (int i = 0; i < len - 1; i++) {
            hashNum = (hashNum + (value.charAt(i) - 'a' + 1)) * HASHFACTOR % hashArray.length;
        }
        hashNum = (hashNum + (value.charAt(len - 1) - 'a' + 1)) % hashArray.length;
        return Math.abs(hashNum);
    }

    /**
     * Returns the frequency of a key String.
     * @param key string value to find its frequency
     * @return frequency value if found. If not found, return 0
     */
    @Override
    public int showFrequency(String key) {
        int counter = 0;
        if (!isWord(key)) {
            return 0;
        }
        int hashVal = hashFunc(key);
        while (hashArray[hashVal] != null) {
            if (counter > hashArray.length) {
                break;
            }
            if (hashArray[hashVal].value.equals(key)) {
                return hashArray[hashVal].frequency;
            }
            hashVal++;
            counter++;
            hashVal = hashVal % hashArray.length;
        }
        return 0;
    }

    /**
     * Removes and returns removed value.
     * @param key String to remove
     * @return value that is removed. If not found, return null
     */
    @Override
    public String remove(String key) {
        if (!isWord(key)) {
            return null;
        }

        int counter = 0;
        int hashVal = hashFunc(key);
        while (hashArray[hashVal] != null) {
            if (hashArray[hashVal].value.equals(key)) {
                hashArray[hashVal] = DELETED;
                size--;
                return key;
            }
            if (counter > hashArray.length) {
                return null;
            }
            counter++;
            hashVal++;
            hashVal = hashVal % hashArray.length;
        }
        return null;
    }

    /**
     * private static data item nested class.
     */
    private static class DataItem {
        /**
         * String value.
         */
        private final String value;
        /**
         * String value's frequency.
         */
        private int frequency;

        DataItem(String input) {
            value = input;
            frequency = 1;
        }
        DataItem(String input, int freq) {
            value = input;
            frequency = freq;
        }
    }

    /**
     * helper function to check whether there is a valid input.
     * @param text input to check whether it's legal
     * @return whether it's legal of the input
     */
    private boolean isWord(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[a-z]+");
    }
}
