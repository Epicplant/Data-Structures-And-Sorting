package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {

    private E[] contents;
    private int topOfStack;
    private static int DEFAULT_CAPACITY = 10;

    public ArrayStack() {
        contents = (E[])new Object[DEFAULT_CAPACITY];
        topOfStack = -1;
    }

    @Override
    public void add(E work) {

        topOfStack++;
        if(topOfStack >= contents.length) {
            E[] temp = (E[]) new Object[contents.length*2];
            for(int i = 0; i < contents.length; i++) {
                temp[i] = contents[i];
            }
            contents = temp;
        }

        contents[topOfStack] = work;
    }

    @Override
    public E peek() {

        if(topOfStack == -1) {
            throw new NoSuchElementException();
        }
        return contents[topOfStack];
    }

    @Override
    public E next() {

        if(topOfStack == -1) {
            throw new NoSuchElementException();
        }
        E val = contents[topOfStack];
        contents[topOfStack] = null;
        topOfStack--;
        return val;
    }

    @Override
    public int size() {
        return topOfStack+1;
    }

    @Override
    public void clear() {
        contents = (E[])new Object[contents.length];
        topOfStack = -1;
    }
}
