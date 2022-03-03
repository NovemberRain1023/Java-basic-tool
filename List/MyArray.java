import java.util.HashSet;
import java.util.Set;



/**
 * @author Xupeng Shi andrew id:xupengs.
 * The code of HW1 in 17683
 */
public class MyArray {

    /**
     * data structure of myArray.
     */
    private String[] array;
    /**
     * size of the array.
     */
    private int size;
    /**
     * capacity of the array.
     */
    private int capacity;
    /**
     * Initialized default capacity is 10.
     */
    public static final int DEFAULT_INIT_CAPACITY = 10;

    /**
     * constructor of myArray to initilize the array with 0.
     */
    public MyArray() {
        array = new String[0];
        size = 0;
        capacity = DEFAULT_INIT_CAPACITY;
    };

    /**
     * constructor of myArray to initilize the array with capacity.
     * @param initialCapacity input capacity
     */
    public MyArray(int initialCapacity) {
        if (initialCapacity > 0) {
            array = new String[initialCapacity];
            capacity = initialCapacity;
            size = 0;
        }

    }

    /**
     * add the text to the array.
     * @param text input content
     * time complexity: O(k), k is the size of the array. Amortized time complexity is O(1).
     */
    public void add(String text) {

        if (text == null || text.length() == 0 || text.trim().equals("")) {
            return;
        }
        if (array == null) {
            array = new String[0];
        }
        if (size >= array.length) {
            int newSize = array.length * 2;
            if (newSize == 0) {
                newSize = 1;
            }
            String[] temp = new String[newSize];
            capacity = temp.length;
            for (int i = 0; i < size; i++) {
                temp[i] = array[i];
            }
            array = temp;
        }

        boolean isValid = true;
        for (int i = 0; i < text.length(); i++) {
            if (!(text.charAt(i) >= 'a' && text.charAt(i) <= 'z' || text.charAt(i) >= 'A' && text.charAt(i) <= 'Z')) {
                isValid = false;
                break;
            }
        }
        if (isValid) {
            array[size++] = text;
        }

    }

    /**
     * search the key in the array.
     * @param key key to search
     * @return whether the key exists in the array
     * time complexity: O(k), k is the size of the array
     */
    public boolean search(String key) {
        if (key == null && size() == 0) {
            return true;
        } else if (key == null || size() == 0) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (array[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * return the size of the array.
     * @return size of the array
     * time complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * return the capacity of the array.
     * @return the capacity of the array
     * time complexity: O(1)
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * print the content of my array.
     * time complexity: O(k), k is the size of the array
     */
    public void display() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i]).append(" ");
        }
        String ans = sb.substring(0, sb.length() - 1);
        System.out.println(ans);
    }

    /**
     * remove all the duplicate string in my array.
     * time complexity O(k), k is the size of the array
     */
    public void removeDups() {

        Set<String> set = new HashSet<>();
        String[] ans = new String[size];
        int j = 0;

        for (int i = 0; i < size; i++) {
            if (!set.contains(array[i])) {
                ans[j++] = array[i];
                set.add(array[i]);
            }
        }
        array = new String[j];
        System.arraycopy(ans, 0, array, 0, j);
//        for (int i = 0; i < j; i++) {
//            array[i] = ans[i];
//        }
        size = j;

    }
}
