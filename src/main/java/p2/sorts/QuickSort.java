package p2.sorts;

import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.CircularArrayFIFOQueue;
import datastructures.worklists.ListFIFOQueue;

import java.util.Comparator;
import java.util.List;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        quickSort(array, 0, array.length-1, comparator);
    }


    private static <E> void quickSort(E[] array, int left, int right, Comparator<E> comparator) {

        if(left + 6 <= right) {

            E pivot = median(array, left, right, comparator);

            int i = left;
            int j = right - 1;

            for (; ; ) {
                while (comparator.compare(array[++i], pivot) < 0) {
                }
                while (comparator.compare(array[--j], pivot) > 0) {
                }
                if (i < j) {
                    swapReferences(array, i, j);
                } else {
                    break;
                }
            }

            swapReferences(array, i, right-1);

            quickSort(array, left, i - 1, comparator);
            quickSort(array, i + 1, right, comparator);
        } else {
            insertionSort(array, left, right, comparator);
        }

    }


    private static <E> void swapReferences(E[] array, int indexOne, int indexTwo) {
        E valOne = array[indexOne];
        E valTwo = array[indexTwo];
        array[indexTwo] = valOne;
        array[indexOne] = valTwo;
    }


    private static <E> E median(E[] array, int left, int right, Comparator<E> comparator) {
        int center = (left + right) / 2;

        if(comparator.compare(array[center], array[left]) < 0)
            swapReferences(array, left, center);
        if(comparator.compare(array[right], array[left]) < 0)
            swapReferences(array, left, right);
        if(comparator.compare(array[right], array[center]) < 0)
            swapReferences(array, center, right);


        swapReferences(array, center, right-1);
        return array[right-1];


    }

    private static <E> void insertionSort(E[] array, int left, int right, Comparator comparer) {
        int j;

        for(int p = left; p <= right; p++) {
            E tmp = array[p];
            for(j = p; j > 0 && comparer.compare(tmp, array[j-1]) < 0; j--) {
                array[j] = array[j-1];
            }
            array[j] = tmp;
        }
    }


}


