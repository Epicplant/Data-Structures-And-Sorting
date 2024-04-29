package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    private Node head;
    private Node tail;

    public MoveToFrontList() {
        head = new Node(null , null, null);
        tail = new Node(null, null, null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public V insert(K key, V value) {

        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        boolean exists = false;
        Node current = head.next;
        V returnValue = null;

        while(current.data != null) {
            if(current.data.key.equals(key)) {
                exists = true;
                returnValue = (V) current.data.value;
                current.data.value = value;

                Node prior = current.prev;
                Node post = current.next;

                prior.next = post;
                post.prev = prior;


                current.next = null;
                current.prev = null;

                current.next = head.next;
                current.prev = head;

                head.next.prev = current;
                head.next = current;

                break;
            }
            current = current.next;
        }

        if(!exists) {
            Item<K,V> temp = new Item<K,V>(key, value);
            Node addingNode = new Node(temp, head.next, head);
            head.next.prev = addingNode;
            head.next = addingNode;
            size++;
        }

        return returnValue;
    }


    @Override
    public V find(K key) {

        if(key == null) {
            throw new IllegalArgumentException();
        }

        Node current = head.next;

        while(current.data != null) {

            if(current.data.key.equals(key)) {

                Node prior = current.prev;
                Node post = current.next;

                prior.next = post;
                post.prev = prior;

                current.next = null;
                current.prev = null;

                current.next = head.next;
                current.prev = head;

                head.next.prev = current;
                head.next = current;
                return (V) current.data.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new listIterator();
    }

    private class listIterator extends SimpleIterator<Item<K,V>> {


        private final WorkList<Node> nodes;

        public listIterator() {
            this.nodes = new ListFIFOQueue<Node>();
            Node temp = head.next;
            while(temp != tail) {
                nodes.add(temp);
                temp = temp.next;
            }
        }

        @Override
        public boolean hasNext() { return (nodes.hasWork());}

        @Override
        public Item<K, V> next() {

            if(hasNext() == false) {
                throw new NoSuchElementException();
            }
            return nodes.next().data;

        }

    }



    public class Node {
        private Item data;
        private Node next;
        private Node prev;

        public Node(Item d, Node n, Node p) {
            data = d;
            next = n;
            prev = p;
        }
    }

}
