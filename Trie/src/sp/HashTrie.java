package sp;

import java.util.Hashtable;

/**
 * Created by dsavv on 22.02.2016.
 */

public class HashTrie implements sp.Trie{
    private class Node {
        public Node () {
            map = new Hashtable<>();
            isTerminal = false;
            sizeInSubtree = 0;
        }

        /**
         * returns next node if it exists, or null otherwise
         */
        public Node getNextNode (Character symbol) {
            return map.getOrDefault(symbol, null);
        }

        /**
         * returns next node and create it if necessary
         */
        public Node getOrAddNextNode (Character symbol) {
            Node nextNode = getNextNode(symbol);
            if (nextNode == null) {
                nextNode = new Node();
                map.put(symbol, nextNode);
            }
            return nextNode;
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public void setTerminal(boolean terminal) {
            isTerminal = terminal;
        }

        public int getSizeInSubtree() {
            return sizeInSubtree;
        }

        public void setSizeInSubtree(int sizeInSubtree) {
            this.sizeInSubtree = sizeInSubtree;
        }

        private int sizeInSubtree;

        private Hashtable<Character, Node> map;
        private boolean isTerminal;

    }

    private Node start;

    /**
     * return end-node of path in trie corresponding to string element
     * (or null if such path is not exists)
     */
    private Node walkOverString(String element) {
        Node curNode = start;
        int index = 0;
        while (index < element.length() &&
                curNode.map.containsKey(element.charAt(index))) {
            curNode = curNode.getNextNode(element.charAt(index));
            index += 1;
        }
        if (index == element.length()) {
            return curNode;
        }
        else {
            return null;
        }

    }

    private boolean recursiveAdd(String element, int index, Node curNode) {
        if (index == element.length()) {
            boolean res = !curNode.isTerminal;
            curNode.setTerminal(true);
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() + 1);
            return res;
        }
        else {
            Node nextNode = curNode.getOrAddNextNode(element.charAt(index));
            int oldSize = nextNode.getSizeInSubtree();
            boolean res = recursiveAdd(element, index + 1, nextNode);
            int newSize = nextNode.getSizeInSubtree();

            // add growth of size in next node to current node
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() + newSize - oldSize);
            return res;
        }
    }

    private boolean recursiveRemove(String element, int index, Node curNode) {
        if (index == element.length()) {
            boolean res = curNode.isTerminal;
            curNode.setTerminal(false);
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() - 1);
            return res;
        }
        else {
            Node nextNode = curNode.getNextNode(element.charAt(index));
            if (nextNode == null)
                return false;
            int oldSize = nextNode.getSizeInSubtree();
            boolean res = recursiveRemove(element, index + 1, nextNode);
            int newSize = nextNode.getSizeInSubtree();

            // add growth of size in next node to current node
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() + newSize - oldSize);
            return res;
        }
    }

    public HashTrie() {
        start = new Node();
    }

    public boolean remove(String element) {
        return recursiveRemove(element, 0, start);
    }

    public boolean add (String element) {
        return recursiveAdd(element, 0, start);
    }

    public boolean contains (String element) {
        Node resultOfWalk = walkOverString(element);
        return resultOfWalk != null && resultOfWalk.isTerminal();
    }

    public int size() {
        return start.getSizeInSubtree();
    }

    public int howManyStartsWithPrefix (String prefix) {
        Node resultOfWalk = walkOverString(prefix);
        if (resultOfWalk == null) {
            return 0;
        }
        else {
            return resultOfWalk.getSizeInSubtree();
        }
    }
}
