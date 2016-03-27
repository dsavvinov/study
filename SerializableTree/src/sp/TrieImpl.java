package sp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dsavv on 22.02.2016.
 */

public class TrieImpl implements Trie, StreamSerializable {
    private Node root;

    public TrieImpl() {
        root = new Node();
    }

    public boolean remove(String element) {
        return recursiveRemove(element, 0, root);
    }

    public boolean add(String element) {
        return recursiveAdd(element, 0, root);
    }

    public boolean contains(String element) {
        Node resultOfWalk = walkOverString(element);
        return resultOfWalk != null && resultOfWalk.isTerminal();
    }

    public int size() {
        return root.getSizeInSubtree();
    }

    public int howManyStartsWithPrefix(String prefix) {
        Node resultOfWalk = walkOverString(prefix);
        if (resultOfWalk == null) {
            return 0;
        } else {
            return resultOfWalk.getSizeInSubtree();
        }
    }

    public void serialize(OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeUTF("TRIE");
        ArrayList<String> dictionary = getAllStrings();
        dataOut.writeInt(dictionary.size());
        for (String s : dictionary) {
            dataOut.writeUTF(s);
        }
    }

    public void deserialize(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        ArrayList<String> dictionary = new ArrayList<>();

        String header = dataIn.readUTF();
        if (!header.equals("TRIE")) {
            throw new StreamCorruptedException("Header not found");
        }

        /* не должен ли я заворачивать все read'ы в try-catch, чтобы пробрасывать наверх
           более конкретное исключение (не IOException, а, например, StreamCorruptedException
           с комментарием "Can't read size of trie")?
        */
        long trieSize = dataIn.readInt();
        for (int i = 0; i < trieSize; i++) {
            String curWord = dataIn.readUTF();
            // we can't write straight to 'this'-trie as that could lead to inconsistent state
            dictionary.add(curWord);
        }

        // iff we read all words successfully then change current state to new one
        root = new Node();  // discard old state
        for (String word : dictionary) {
            add(word);
        }
    }

    public ArrayList<String> getAllStrings() {
        ArrayList<String> res = new ArrayList<>();
        StringBuilder curString = new StringBuilder();
        collectAllStrings(root, res, curString);
        return res;
    }

    private void collectAllStrings(Node curNode,
                                   ArrayList<String> res,
                                   StringBuilder curString) {
        if (curNode.isTerminal()) {
            res.add(curString.toString());
        }
        for (HashMap.Entry<Character, Node> entry : curNode.map.entrySet()) {
            Node nextNode = entry.getValue();
            curString.append(entry.getKey());
            collectAllStrings(nextNode, res, curString);
            curString.deleteCharAt(curString.length() - 1);
        }
    }

    /**
     * return end-node of path in trie corresponding to string element
     * (or null if such path is not exists)
     */
    private Node walkOverString(String element) {
        Node curNode = root;
        int index = 0;
        while (index < element.length() &&
                curNode.getNextNode(element.charAt(index)) != null) {
            curNode = curNode.getNextNode(element.charAt(index));
            index += 1;
        }
        if (index == element.length()) {
            return curNode;
        } else {
            return null;
        }
    }

    private boolean recursiveAdd(String element, int index, Node curNode) {
        if (index == element.length()) {
            boolean res = !curNode.isTerminal();
            if (!curNode.isTerminal()) {
                curNode.setSizeInSubtree(curNode.getSizeInSubtree() + 1);
                curNode.setTerminal(true);
            }
            return res;
        } else {
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
            boolean res = curNode.isTerminal();
            curNode.setTerminal(false);
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() - 1);
            return res;
        } else {
            char symbol = element.charAt(index);
            Node nextNode = curNode.getNextNode(symbol);

            if (nextNode == null) {
                return false;
            }

            int oldSize = nextNode.getSizeInSubtree();
            boolean res = recursiveRemove(element, index + 1, nextNode);
            int newSize = nextNode.getSizeInSubtree();

            // add growth of size in next node to current node
            curNode.setSizeInSubtree(curNode.getSizeInSubtree() + newSize - oldSize);

            // delete next node if it subtree doesn't contain terminal nodes
            if (nextNode.getSizeInSubtree() == 0) {
                curNode.removeNextNode(symbol);
            }
            return res;
        }
    }

    private static class Node {
        private int sizeInSubtree;
        private final HashMap<Character, Node> map;
        private boolean isTerminal;

        public Node() {
            map = new HashMap<>();
            isTerminal = false;
            sizeInSubtree = 0;
        }

        /**
         * returns next node if it exists, or null otherwise
         */
        public Node getNextNode(char symbol) {
            return map.getOrDefault(symbol, null);
        }

        /**
         * returns next node and create it if necessary
         */
        public Node getOrAddNextNode(char symbol) {
            Node nextNode = getNextNode(symbol);
            if (nextNode == null) {
                nextNode = new Node();
                map.put(symbol, nextNode);
            }
            return nextNode;
        }

        public void removeNextNode(char symbol) {
            map.remove(symbol);
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
    }
}
