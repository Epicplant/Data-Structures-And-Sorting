package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }


    /**
     * Behaviour is undefined when k > array.length
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {

        MinFourHeap<E> heaper = new MinFourHeap<E>(comparator);

        if(k <= array.length) {
            for(int i = 0; i < array.length; i++) {
                if(heaper.size() < k) {
                    heaper.add(array[i]);
                } else {
                    if(comparator.compare(heaper.peek(), array[i]) < 0) {
                        heaper.next();
                        heaper.add(array[i]);
                    }
                }
            }


            for(int i = 0; i < array.length; i++) {
                if(i >= k) {
                    array[i] = null;
                } else {
                    array[i] = heaper.next();
                }
            }

        }

    }
}
