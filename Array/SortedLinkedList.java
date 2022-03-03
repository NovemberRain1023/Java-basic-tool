/**
 * HW3 for sort linkedList by recursion.
 * @author Xupeng Shi
 * andrew ID: xupengs
 */
public class SortedLinkedList implements MyListInterface {

    public static void main(String[] args) {
//        SortedLinkedList sortedLinkedList = new SortedLinkedList();
//        sortedLinkedList.add("b");
//        sortedLinkedList.add("c");
//        sortedLinkedList.add("a");
//        sortedLinkedList.display();
//        System.out.println(sortedLinkedList.size());
//        System.out.println(sortedLinkedList.contains("a"));
//        System.out.println(sortedLinkedList.isEmpty());
//        System.out.println(sortedLinkedList.removeFirst());
//        System.out.println(sortedLinkedList.removeAt(0));

        String[] input = new String[] {"the", "children", "selections", "from", "testaments", "arranged", "sherman"};

    }

    /**
     * Node class.
     */
    public static class Node {
        /**
         * Node's value.
         */
         private String val;

        /**
         * Node's next node.
         */
        private Node next;

        /**
         * get value function.
         * @return val of node
         */
        public String getVal() {
            return val;
        }

        /**
         * get next node.
         * @return the next node
         */
        public Node getNext() {
            return next;
        }

        /**
         * constructor.
         * @param input Node's value
         */
        Node(String input) {
            val = input;
        }

        /**
         * constructor.
         * @param input Node'value
         * @param nextIn node's next node
         */
        Node(String input, Node nextIn) {
            val = input;
            this.next = nextIn;
        }
    }

    /**
     * head reference.
     */
    private Node head;

    /**
     * default constructor.
     */
    public SortedLinkedList() {
        head = null;
    }

    /**
     * Constructor with unsorted array.
     * @param input unsorted array
     */
    public SortedLinkedList(String[] input) {
        sortArray(input, 0);
    }

    /**
     * sort array function.
     * @param input input array
     * @param i current index
     */
    private void sortArray(String[] input, int i) {
        // base case
        if (i == input.length) {
            return;
        }
        // current level job
        add(input[i]);
        // recursive role
        sortArray(input, i + 1);
    }

    /**
     * add a value to the list.
     * @param value String to be added.
     */
    @Override
    public void add(String value) {
        // check whether the input is valid.
        if (!isWord(value)) {
            return;
        }
        // corner case: if head is null, initialize the head
        if (head == null) {
            head = new Node(value, null);
            return;
        }
        // corner case: if value if smaller than head's value, make the value to be new head
        if (head.val.compareTo(value) > 0) {
            Node temp = new Node(value, head);
            head = temp;
            return;
        }
        // recursive add if there is no duplicate
        if (!contains(value)) {
            addRecurse(head, value);
        }
    }

    /**
     * helper function to add recursively.
     * @param node current node
     * @param value value to add
     */
    private void addRecurse(Node node, String value) {
        // base case 1: reach end of the node and the added value is larger than the list tail's value
        if (node.next == null) {
            if (value.compareTo(node.val) > 0) {
                Node temp = new Node(value, null);
                node.next = temp;
                return;
            }
        }
        // base case 2: find a position that the current value is smaller than the added node's value and the next value if larger than
        // the node's value, insert the node to current node's next
        if (value.compareTo(node.val) > 0 && value.compareTo(node.next.val) < 0) {
            Node temp = new Node(value, node.next);
            node.next = temp;
            return;
        }
        // recursive role
        addRecurse(node.next, value);
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
     * get the size of the list.
     * @return size of list
     */
    @Override
    public int size() {
        return getSize(head);
    }

    /**
     * helper function to get size.
     * @param headIn head node
     * @return size
     */
    private int getSize(Node headIn) {
        if (headIn == null) {
            return 0;
        }
        return 1 + getSize(headIn.next);
    }

    /**
     * display the list node value.
     */
    @Override
    public void display() {
        if (head == null) {
            System.out.print("");
            return;
        }

        System.out.print("[");
        recurseDis(head);
        System.out.print("]");
    }

    /**
     * helper function to display the list.
     * @param curNode list head
     */
    private void recurseDis(Node curNode) {
        // Corner case
        if (curNode == null) {
            System.out.print("");
            return;
        }
        // base case 1
        if (curNode.next == null) {
            System.out.print(curNode.val);
            System.out.print("");
            return;
        }
        // recursive role
        System.out.print(curNode.val + "," + " ");
        recurseDis(curNode.next);
    }

    /**
     * Whether there is existing the key in the list.
     * @param key String key to search
     * @return Whether there is existing the key in the list
     */
    @Override
    public boolean contains(String key) {
        // check input validation
        if (isWord(key)) {
            return containsRecurse(head, key.toLowerCase());
        } else {
            return false;
        }

    }

    /**
     * helper function to determine contains.
     * @param headIn list node head
     * @param key key to search
     * @return Whether there is existing the key in the list
     */
    private boolean containsRecurse(Node headIn, String key) {
        // base case
       if (headIn == null) {
           return false;
       }
       // current level job
       if (headIn.val.equals(key)) {
           return true;
       }
       // recursive role
       return containsRecurse(headIn.next, key);
    }

    /**
     * determine whether the list is empty.
     * @return whether the list is empty
     */
    @Override
    public boolean isEmpty() {
        return (head == null || head.val == null);
    }

    /**
     * remove the first element of the list.
     * @return the first element of the list
     */
    @Override
    public String removeFirst() {
        if (head == null) {
            return null;
        }
        String returnVal = head.val;
        head = head.next;
        return returnVal;
    }

    /**
     * remove the element at list index.
     * @param index index to remove String object
     * @return the element at list index
     */
    @Override
    public String removeAt(int index) {
        int size = size();
        if (index < 0 || index >= size) {
            throw new RuntimeException("index is out of bound!");
        }
        if (index == 0) {
            return removeFirst();
        } else {
            return removeRecurse(head, 0, index);
        }
    }

    /**
     * helper function to remove recursive.
     * @param headIn list node head
     * @param i current index
     * @param index index
     * @return String to remove
     */
    private String removeRecurse(Node headIn, int i, int index) {
        if (headIn == null || headIn.next == null) {
            return null;
        }
        if (index == i + 1) {
            String temp = headIn.next.val;
            headIn.next = headIn.next.next;
            return temp;
        }
        return removeRecurse(headIn.next, i + 1, index);
    }
}
