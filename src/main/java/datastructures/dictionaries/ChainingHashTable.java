package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.CircularArrayFIFOQueue;
import datastructures.worklists.ListFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 * restrict the size of the input domain (i.e., it must accept
 * any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 * shown in class!).
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 * than 200,000 elements. After more than 200,000 elements, it should
 * continue to resize using some other mechanism.
 * 6. We suggest you hard code some prime numbers. You can use this
 * list: http://primes.utm.edu/lists/small/100000.txt
 * NOTE: Do NOT copy the whole list!
 * 7. When implementing your iterator, you should NOT copy every item to another
 * dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;

    private ListFIFOQueue<Integer> primeNumbers = new ListFIFOQueue<>();
    private static final int DEFAULT_CAPACITY = 3;
    private Dictionary<K, V>[] table;

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;

        primeNumbers.add(7);
        primeNumbers.add(13);
        primeNumbers.add(29);
        primeNumbers.add(59);
        primeNumbers.add(127);
        primeNumbers.add(257);
        primeNumbers.add(521);
        primeNumbers.add(1049);
        primeNumbers.add(2089);
        primeNumbers.add(4177);
        primeNumbers.add(8419);
        primeNumbers.add(16843);
        primeNumbers.add(33713);
        primeNumbers.add(67153);
        primeNumbers.add(125429);
        primeNumbers.add(201011);

        table = new Dictionary[DEFAULT_CAPACITY];

        for(int i = 0; i < table.length; i++) {
            table[i] = newChain.get();
        }
    }

    @Override
    public V insert(K key, V value) {

       if(key == null || value == null ) {
           throw new IllegalArgumentException();
       }

        Dictionary<K, V> currentList = table[hasher(key)];
            V returnerValue = currentList.insert(key, value);

            if(returnerValue == null) {
                size++;
            }

            if((size)/table.length >= 1) {
                rehash();
            }
        return returnerValue;
    }

    private int hasher(K key) {

        if(key == null) {
            throw new IllegalArgumentException();
        }

            int hashVal = key.hashCode();

            hashVal %= table.length;
            if (hashVal < 0) {
                hashVal += table.length;
            }

            return hashVal;
    }

    private void rehash() {

        int newArraySize = 0;

        if(primeNumbers.hasWork()) {
            newArraySize = primeNumbers.next();
        } else {
            newArraySize  = 2 * size + 1;
        }


            Dictionary<K, V>[] oldTable = table;
            table = new Dictionary[newArraySize];
            for(int i = 0; i < table.length; i++) {
                table[i] = newChain.get();
            }

            for(int i = 0; i < oldTable.length; i++) {

                Dictionary<K, V> currentList = oldTable[i];

                for (Item<K,V> value : currentList) {

                    Dictionary<K, V> inserterList = table[hasher(value.key)];
                    inserterList.insert(value.key,value.value);

                }
            }
    }


    @Override
    public V find(K key) {

        if(key == null) {
            throw new IllegalArgumentException();
        }

        int index = key.hashCode();
        index %= table.length;
        if (index < 0) {
            index += table.length;
        }

        Dictionary<K, V> listToSearch = table[index];
        return listToSearch.find(key);

    }

    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTable.tableIterator();
    }

    private class tableIterator extends SimpleIterator<Item<K,V>> {


        private Iterator<Item<K,V>> currentBucket;
        private int index;


        public tableIterator() {
            index = 0;
            this.currentBucket = table[0].iterator();
        }

        @Override
        public boolean hasNext() {
                if(currentBucket.hasNext()) {
                    return true;
                } else if(index+1 < table.length) {

                    while(index+1 < table.length && currentBucket.hasNext() == false) {
                        currentBucket = table[++index].iterator();
                    }

                    return true;
                } else {
                    return false;
                }
        }

        @Override
        public Item<K, V> next() {

            if(hasNext() == false) {
                throw new NoSuchElementException();
            }

            return  currentBucket.next();

        }

    }

    /**
     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
     * Remove to see proper String representation (inherited from Dictionary)
     */
//    @Override
//    public String toString() {
//        return "ChainingHashTable String representation goes here.";
//    }
}
