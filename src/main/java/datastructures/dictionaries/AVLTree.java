package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;

import java.lang.reflect.Array;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    public class AVLNode extends BSTNode {
        private int height;

        public AVLNode(K key, V value) {
            super(key, value);
            height = 0;
        }

        public int nodeHeight() {
            return height;
        }


    }

    //Fields lastNodeValue = records the value of a previous field after an insert if a new value isn't added
    //Fields addedNotModified = if a key is just modified and a node isn't inserted it is false otherwise it is true
    private V lastNodeValue = null;
    private boolean addedNotModified = true;

    public AVLTree() {
        super();
        this.root = null;
    }



    @Override
    public V find(K key) {

        if (key == null) {
            throw new IllegalArgumentException();
        }

        AVLNode current = (AVLNode) root;

        while(current != null) {
            int direction = Integer.signum(key.compareTo(current.key));
            int child = Integer.signum(direction + 1);

            if (direction == 0) {
                return current.value;
            }
            else {
                current = (AVLNode) current.children[child];
            }

        }

        return null;
    }

    @Override
    public V insert(K key, V value) {

        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        root = recursiveInsert(key, value, (AVLNode) root);
        V returnerValue = lastNodeValue;
        lastNodeValue = null;
        addedNotModified = true;

        return returnerValue;
    }

    //Recursively travels through tree while following the path based on the key
    private AVLNode recursiveInsert(K key, V value, AVLNode current) {

        if(current == null) {
            this.size++;
            return new AVLNode(key, value);
        } else if(current.key.equals(key)) {
            lastNodeValue = current.value;
            current.value = value;
            addedNotModified = false;
            return current;
        }

        int comparison = key.compareTo(current.key);

        if(comparison < 0) {
            current.children[0] = recursiveInsert(key, value, (AVLNode) current.children[0]);
        } else if(comparison > 0) {
            current.children[1] = recursiveInsert(key, value, (AVLNode) current.children[1]);
        }

        return balance(current);
    }

    //the value that represents hte greatest difference in heights between two nodes
    private static final int ALLOWED_IMBALANCE = 1;


    //Rebalances the tree based on each of the four cases. Check 1 is left-left and then left-right. Check 2
    //is right-right and then right-left
    private AVLNode balance(AVLNode current) {
        if(current == null) {
            return current;
        }

        if(height((AVLNode) current.children[0])-height((AVLNode) current.children[1]) > ALLOWED_IMBALANCE) {

            if(height((AVLNode) current.children[0].children[0]) >= height((AVLNode) current.children[0].children[1])) {
                current = rotateWithLeftChild( current );
            } else {
                current = doubleWithLeftChild( current);
            }

        } else if(height( (AVLNode) current.children[1])-height((AVLNode) current.children[0]) > ALLOWED_IMBALANCE) {

            if(height((AVLNode) current.children[1].children[1]) >= height((AVLNode) current.children[1].children[0])) {
               current = rotateWithRightChild( current );
            } else {
                current = doubleWithRightChild( current );
            }
        }

        if(addedNotModified) {
            current.height = Math.max(height((AVLNode) current.children[0]), height((AVLNode) current.children[1])) + 1;
        }
        return current;
    }

    //returns height of a node
    private int height(AVLNode current) {
        return current == null ? -1 : current.height;
    }

    //follows the notation of rotating in the left-left case
    private AVLNode rotateWithLeftChild( AVLNode k2) {

        AVLNode k1 = (AVLNode) k2.children[0];
        k2.children[0] = k1.children[1];
        k1.children[1] = k2;
        k2.height = Math.max(height((AVLNode) k2.children[0]), height((AVLNode) k2.children[1])) +1;
        k1.height = Math.max(height((AVLNode) k1.children[0]), k2.height) +1;

        return k1;
    }

    //follows the notation of rotating in the right-right case.
    private AVLNode rotateWithRightChild( AVLNode k2) {

        AVLNode k1 = (AVLNode) k2.children[1];
        k2.children[1] = k1.children[0];
        k1.children[0] = k2;
        k2.height = Math.max(height((AVLNode) k2.children[1]), height((AVLNode) k2.children[0])) +1;
        k1.height = Math.max(height((AVLNode) k1.children[1]), k2.height) +1;

        return k1;
    }

    //follows the notation of rotating in the right-left case
    private AVLNode doubleWithLeftChild(AVLNode k3) {
        k3.children[0] = rotateWithRightChild((AVLNode) k3.children[0]);
        return rotateWithLeftChild(k3);
    }

    //follows the notation of rotating in the left-right case
    private AVLNode doubleWithRightChild(AVLNode k3) {
        k3.children[1] = rotateWithLeftChild((AVLNode) k3.children[1]);
        return rotateWithRightChild(k3);
    }
}