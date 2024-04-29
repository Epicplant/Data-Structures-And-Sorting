package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private static final int DEFAULT_CAPACITY = 10;
    private int currentSize = 0;
    private Comparator<E> comparer;

    public MinFourHeap(Comparator<E> c) {

        comparer = c;
        data = (E[]) new Object[DEFAULT_CAPACITY];
        currentSize = 0;

    }

    @Override
    public boolean hasWork() {
        return (currentSize > 0);
    }

    @Override
    public void add(E work) {

        if(work == null) {
            throw new IllegalArgumentException();
        }

        if(currentSize == (data.length-1)) {
            E[] temp = (E[]) new Object[data.length*2+1];
            for(int i = 0; i < data.length; i++) {
                temp[i] = data[i];
            }
            data = temp;
        }


        int newPosition = percolateUp(currentSize, work);
        currentSize++;
        data[newPosition] = work;
    }

    private int percolateUp(int hole, E val) {
        while (hole > 0 && comparer.compare(val, data[(hole-1)/4]) < 0) {
            data[hole] = data[(hole-1)/4];
            hole = (hole-1)/4;
        }

        return hole;
    }

    @Override
    public E peek() {
        if(hasWork() == false) {
            throw new NoSuchElementException();
        }
        return data[0];
    }

    @Override
    public E next() {

        if(hasWork() == false) {
            throw new NoSuchElementException();
        }

        E returnVal = data[0];
        currentSize--;
        int newPos = percolateDown(0, data[currentSize]);
        data[newPos] = data[currentSize];
        data[currentSize] = null;
        return returnVal;
    }

    private int percolateDown (int hole, E val) {

        int target = 0;
        while( 4 * hole <= currentSize) {
            int firstChild = (4*hole) + (0+1);
            int secondChild = firstChild+1;
            int thirdChild = secondChild+1;
            int fourthChild = thirdChild+1;

            if(fourthChild <= currentSize &&  comparer.compare(data[fourthChild], data[thirdChild]) < 0 && comparer.compare(data[fourthChild], data[secondChild]) < 0
                    && comparer.compare(data[fourthChild], data[firstChild]) < 0) {
                target = fourthChild;
            } else if(thirdChild <= currentSize && comparer.compare(data[thirdChild], data[secondChild]) < 0
                    && comparer.compare(data[thirdChild], data[firstChild]) < 0) {
                target = thirdChild;
            } else if(secondChild <= currentSize
                    && comparer.compare(data[secondChild], data[firstChild]) < 0) {
                target = secondChild;
            } else {
                target = firstChild;
            }

            if(data[target] == null) {
                break;
            } else if(comparer.compare(data[target], val) < 0) {
                data[hole] = data[target];
                hole = target;
            } else {
                break;
            }
        }
        return hole;
    }


    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        currentSize = 0;
    }
}

