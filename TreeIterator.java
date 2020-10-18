/**
 *
 * @author Kevin Martin
 * Written in 2011
 */
import java.lang.*;
import java.io.*;
import java.util.*;

public class TreeIterator {

    final static int numLetters = 6;
    final static int treeSize = 32;
    final static int dictSize = 34976;
    static int[] window = {1, 2, 4, 5, 3, 6}; // Contains the indices of preOrder[] that make up the current tree shape
    static int[] pos = new int[numLetters]; // Contains the ints corresponding to the node numbers
    final static int[] preOrder = {-1, 1, 2, 4, 8, 16, 17, 9, 18, 19, 5, 10, 20, 21, 11, 22, 23, 3, 6, 12, 24, 25, 13, 26, 27, 7, 14, 28, 29, 15, 30, 31};
    final static int[] postOrder = {-1, 16, 17, 8, 18, 19, 9, 4, 20, 21, 10, 22, 23, 11, 5, 2, 24, 25, 12, 26, 27, 13, 6, 28, 29, 14, 30, 31, 15, 7, 3, 1};
    final static int[] inOrder = {-1, 16, 8, 17, 4, 18, 9, 19, 2, 20, 10, 21, 5, 22, 11, 23, 1, 24, 12, 25, 6, 26, 13, 27, 3, 28, 14, 29, 7, 30, 15, 31};

    private static char[] getIn(char[] s) {
        char in[] = new char[treeSize];

        for (int i = 1; i < treeSize; i++) {
            in[i] = s[inOrder[i]];
        }

        return in;
    }

    private static char[] getPost(char[] s) {
        char post[] = new char[treeSize];

        for (int i = 1; i < treeSize; i++) {
            post[i] = s[postOrder[i]];
        }

        return post;
    }

    private static void printChars(char[] s) {
        for (int i = 1; i < s.length; i++) {
            System.out.print(s[i]);
        }
        System.out.println();
    }

    private static String printInts(int[] k) {
        String ints = new String("");

        int j = 0;
        for (int i = 0; i < k.length; i++) {
            ints = new StringBuffer(ints).insert(j, "" + k[i] + ", ").toString();
            j = j + 3;
        }
        return ints;
    }

    /**
     * Returns the string of a character array without the blanks.
     * @param s
     * @return 
     */
    private static String crunch(char[] s) {
        String crunched = new String("");

        int j = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i] >= 'a' && s[i] <= 'z' || s[i] == '\'') {
                crunched = new StringBuffer(crunched).insert(j, s[i]).toString();
                j++;
            }
        }

        return crunched;
    }

    /**
     * Changes the tree shape.
     * @param pos
     * @return 
     */
    private static boolean changeShape(int[] pos) {
        do {
            if (window[5] < treeSize - 1) {
                window[5]++;
            } else if (window[4] < treeSize - 2) {
                window[4]++;
                window[5] = window[4] + 1;
            } else if (window[3] < treeSize - 3) {
                window[3]++;
                window[4] = window[3] + 1;
                window[5] = window[3] + 2;
            } else if (window[2] < treeSize - 4) {
                window[2]++;
                window[3] = window[2] + 1;
                window[4] = window[2] + 2;
                window[5] = window[2] + 3;
            } else if (window[1] < treeSize - 5) {
                window[1]++;
                window[2] = window[1] + 1;
                window[3] = window[1] + 2;
                window[4] = window[1] + 3;
                window[5] = window[1] + 4;
            } else {
                return false;
            }
            for (int i = 0; i < numLetters; i++) {
                pos[i] = preOrder[window[i]];
            }
        } while (!checkPos(pos));

        return true;
    }

    private static void resetPos() {
        int[] y = {1, 2, 4, 5, 3, 6};
        window = y;
        int[] z = {1, 2, 3, 4, 5, 6};
        pos = z;
    }

    // No duplicates indices
    // No illegal trees
    // True if legal / otherwise false
    private static boolean checkPos(int[] pos) {

        boolean hasParent = false;

        for (int i = 1; i < numLetters; i++) {
            hasParent = false;
            for (int j = 0; j < numLetters && i != j; j++) {
                if (pos[i] == pos[j]) {
                    //System.err.println("Duplicate Indices!\t" + printInts(pos));
                    return false;
                } else if ((Math.floor(pos[i] / 2)) == pos[j]) {
                    hasParent = true;
                }
            }
            if (!hasParent) {
                //System.err.println("Illegal Tree Shape\t" + printInts(pos));
                return false;
            }
        }
        //System.out.println("***Good Shape\t\t" + printInts(pos));
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "length6.txt";
        String dict[] = new String[dictSize];
        char inPos[] = new char[treeSize];

        String preO, postO, inO;

        Scanner fin = null;

        /* Open File */
        try {
            fin = new Scanner(new FileReader(filename));
        } catch (FileNotFoundException fnfe) {
            System.err.println("File " + filename + " not found!");
        }

        // Load dictionary file to array
        int p = 0;
        for (; p < dictSize; p++) {
            dict[p] = fin.nextLine();
        }
        System.out.println("Loaded " + p + " words successfully!");

        changeShape(pos);

        for (int i = 0; i < dictSize; i++) {
            preO = dict[i];

            do {
                for (int j = 0; j < numLetters; j++) {
                    inPos[pos[j]] = preO.charAt(j);
                }

                postO = crunch(getPost(inPos));
                inO = crunch(getIn(inPos));

                for (int k = 0; k < dictSize; k++) {
                    if (dict[k].compareTo(postO) == 0) {
                        //    System.out.println("CLOSE!! preo: " + preO + " postO: " + postO + " inO " + inO + " tree: " + printInts(pos));
                        for (int e = 0; e < dictSize; e++) {
                            if (dict[e].compareTo(inO) == 0) {
                                System.out.println("PreOrder: " + preO + " PostOrder: " + postO + " InOrder: " + inO + " Tree Shape: " + printInts(pos));
                            }
                        }
                    }
                }

                // Compare words to dictionary
                for (int k = 0; k < treeSize; k++) {
                    inPos[k] = ' ';
                }

            } while (changeShape(pos));

            resetPos();
        }
    }
}
