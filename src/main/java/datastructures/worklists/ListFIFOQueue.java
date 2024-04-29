package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private Node head;
    private Node tail;
    int size = 0;

    public ListFIFOQueue() {
        head = new Node(null , null, null);
        tail = new Node(null, null, null);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void add(E work) {

        if(work == null) {
            throw new NoSuchElementException();
        }

        Node addingNode = new Node(work, head.next, head);
        head.next.prev = addingNode;
        head.next = addingNode;
        size++;
    }

    @Override
    public E peek() {
        if(tail.prev.data == null) {
            throw new NoSuchElementException();
        } else {
            return tail.prev.data;
        }
    }

    @Override
    public E next() {
        if(size == 0) {
            throw new NoSuchElementException();
        } else {
            E value = tail.prev.data;
            tail.prev.prev.next = tail;
            tail.prev = tail.prev.prev;
            size--;
            return value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = new Node(null , null, null);
        tail = new Node(null, null, null);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public class Node {
        private E data;
        private Node next;
        private Node prev;

        public Node(E d, Node n, Node p) {
            data = d;
            next = n;
            prev = p;
        }
    }
}
