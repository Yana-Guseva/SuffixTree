package rosalind.ba9c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Iana_Guseva on 5/24/2016.
 */
public class SuffixTreeIndexes {

    private static TreeNode root;

    public SuffixTreeIndexes() {
        root = new TreeNode();
    }

    class TreeNode {
        Map<String, TreeNode> children = new ConcurrentHashMap<>();
        int position = 0;
        int length = 1;
    }

    public void suffixTrieConstruct(String text) {
        for (int i = 0; i < text.length(); i++) {
            TreeNode currentNode = root;
            for (int j = i; j < text.length(); j++) {
                String symbol = String.valueOf(text.charAt(j));
                if (!currentNode.children.containsKey(symbol)) {
                    currentNode.children.put(symbol, new TreeNode());
                    currentNode.children.get(symbol).position = j;
                }
                currentNode = currentNode.children.get(symbol);
            }
        }
    }

    public void suffixTreeConstruct(String text) {
        suffixTrieConstruct(text);
        consolidateTrie(root);
    }

    public void consolidateTrie(TreeNode node) {
        for (String str : node.children.keySet()) {
            TreeNode child = node.children.get(str);
            if (child.children.keySet().size() == 1) {
                for (String key : child.children.keySet()) {
                    int position = child.position;
                    int length = child.length;
                    node.children.put(str, child.children.remove(key));
                    node.children.get(str).length = ++length;
                    node.children.get(str).position = position;
                    child = node;
                }
            }
            consolidateTrie(child);
        }
    }

    public void printSuffixTree(TreeNode node, String text) {
        for (String str : node.children.keySet()) {
            TreeNode child = node.children.get(str);
            System.out.println(text.substring(child.position, child.position + child.length));
            printSuffixTree(child, text);
        }
    }

    public static String loadData(String fileName) {

        String text = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            text = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return text;
    }

    public static void main(String[] args) {
        final String fileName = "D:\\rosalind\\rosalind_ba9c_1.txt";
        String text = loadData(fileName);
        SuffixTreeIndexes tree = new SuffixTreeIndexes();
        tree.suffixTreeConstruct(text);
        tree.printSuffixTree(root, text);
    }
}
