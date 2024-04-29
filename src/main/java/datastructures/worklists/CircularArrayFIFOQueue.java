package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {

    private E[] queue;
    int front;
    int back;
    int currentSize;

    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        queue = (E[])new Comparable[capacity];
        front = 0;
        back = 0;
        currentSize = 0;
    }

    @Override
    public void add(E work) {

        if(work == null) {
            throw new NoSuchElementException();
        }

        if(this.isFull()) {
            throw new IllegalStateException();
        }

       /* currentSize++;

        queue[back] = work;

        if(back >= (queue.length-1)) {
            back = 0;
        } else {
            back++;
        }*/
        queue[(front+currentSize) % queue.length] = work;
        currentSize++;
    }

    @Override
    public E peek() {
        if(queue[front] == null || currentSize == 0) {
            throw new NoSuchElementException();
        } else {
            return queue[front];
        }
    }

    @Override
    public E peek(int i) {

        if(queue[front] == null || currentSize == 0) {
            throw new NoSuchElementException();
        } else if(i >= currentSize || i < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            return queue[(front + i) % queue.length];
        }
    }


    @Override
    public E next() {

        if(queue[front] == null) {
            throw new NoSuchElementException();
        }


        currentSize--;
        E returnValue = queue[front];

        if(front >= (queue.length-1)) {
            front = 0;
        } else {
            front++;
        }


        return returnValue;

    }

    @Override
    public void update(int i, E value) {

        if(value == null || currentSize == 0) {
            throw new NoSuchElementException();
        } else if(i >= currentSize || i < 0) {
            throw new IndexOutOfBoundsException();
        }


        queue[(front+i)%queue.length] = value;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public void clear() {
        queue = (E[])new Comparable[capacity()];
        front = 0;
        back = 0;
        currentSize = 0;
    }




    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.

        FixedSizeFIFOWorkList<E> current = this;

        for(int i = 0; i < Math.min(other.size(), current.size()); i++) {

            if (current.peek(i) != other.peek(i)) {

                 E valOne = current.peek(i);
                 E valTwo = other.peek(i);


                 if(valOne != null && valTwo != null) {
                     return valOne.compareTo(valTwo);
                 } else if (valOne == null && valTwo != null) {
                     throw new NullPointerException();
                 }  else if (valTwo == null && valOne != null) {
                     throw new NullPointerException();
                 }
            }
        }

        if(current.size() != other.size()) {
            int temp = current.size() - other.size();
            if (temp > 0) {
                return 1;
            } else {
                return -1;
            }
        }

        return 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            // Uncomment the line below for p2 when you implement equals
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;

             if(this.compareTo(other) == 0) {
                 return true;
             } else {
                 return false;
             }
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in project 2. Leave this method unchanged for project 1.

        //s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]

        int result = 0;

        for(int i = 0; i < this.size(); i++) {
            result = 31 * (result + this.peek(i).hashCode());
        }

        return result;
    }
}
