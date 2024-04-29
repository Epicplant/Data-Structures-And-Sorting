package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.trie.TrieMap;
import cse332.interfaces.worklists.WorkList;
import cse332.types.BString;
import datastructures.worklists.ListFIFOQueue;

import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<ChainingHashTable<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {

            this.pointers = new ChainingHashTable<A, HashTrieNode>(MoveToFrontList::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {

            return (Iterator) new trieMapIterator();
        }


        private class trieMapIterator extends SimpleIterator<AbstractMap.SimpleEntry<A,HashTrieNode>> {

            private final WorkList<Item<A,HashTrieNode>> entries;

            public trieMapIterator() {
                this.entries = new ListFIFOQueue<Item<A,HashTrieNode>>();
                Iterator<Item<A, HashTrieNode>> temp = pointers.iterator();
            }

            @Override
            public boolean hasNext() { return (entries.hasWork());}

            @Override
            public AbstractMap.SimpleEntry<A, HashTrieNode> next() {
                if(hasNext() == false) {
                    throw new NoSuchElementException();
                }

                Item<A, HashTrieNode> temp = entries.next();
                return new AbstractMap.SimpleEntry<A, HashTrieNode>(temp.key, temp.value);
            }


        }

    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode overallTrie = (HashTrieNode) this.root;
        HashTrieNode currentNode = overallTrie;

       for(A keyValue : key) {

            Object test = keyValue;

            HashTrieNode finder = currentNode.pointers.find((A) test);
            if(finder != null ) {
                currentNode = finder;
            } else {
                HashTrieNode newNode = new HashTrieNode();
                currentNode.pointers.insert((A) test, newNode);
                currentNode = newNode;
            }
        }

        if(currentNode.value != null) {
            V temp = currentNode.value;
            currentNode.value = value;
            return temp;
        } else {
            this.size++;
            currentNode.value = value;
            return null;
        }
    }

    @Override
    public V find(K key) {

        if(key == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode overallTrie = (HashTrieNode) this.root;
        HashTrieNode currentNode = overallTrie;

        for(A keyValue : key) {

            Object test = keyValue;

            HashTrieNode finder = currentNode.pointers.find((A) test);

            if(finder != null) {
                currentNode = finder;
            } else {
                return null;
            }
        }
        return currentNode.value;
    }

    @Override
    public boolean findPrefix(K key) {

        if(key == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode overallTrie = (HashTrieNode) this.root;
        HashTrieNode currentNode = overallTrie;


        for(A keyValue : key) {
            Object test = keyValue;

            HashTrieNode finder = currentNode.pointers.find((A) test);

            if(finder != null) {
                currentNode = finder;
            } else {
                return false;
            }
        }

        if(overallTrie.value == null && overallTrie.pointers.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void delete(K key) {

        throw new UnsupportedOperationException();

        /*if(key == null) {
            throw new IllegalArgumentException();
        }

        HashTrieNode overallTrie = (HashTrieNode) this.root;
        HashTrieNode currentNode = overallTrie;

        Iterator iter = key.iterator();

        HashTrieNode lastFullNode = overallTrie;
        Object lastKey = null;
        boolean exists = true;



        while(iter.hasNext()) {


            Object test = iter.next();

            if(currentNode.pointers.containsKey(test)) {
                if(currentNode.value != null || currentNode.pointers.size() > 1 || lastKey == null) {
                    lastFullNode = currentNode;
                    lastKey = test;
                }
                currentNode = currentNode.pointers.get(test);
            } else {
                exists = false;
                break;
            }
        }

        if(currentNode.value == null) {
            exists = false;
        }


        if((exists && !key.isEmpty()) || (key.isEmpty() && overallTrie.value != null)) {

            HashTrieNode soonToBeDeleted = currentNode;
            size--;

            if (soonToBeDeleted.pointers.size() != 0 || (key.isEmpty() && overallTrie.value != null)) {
                soonToBeDeleted.value = null;
            } else {
                lastFullNode.pointers.remove(lastKey);
            }
        }
*/    }

    @Override
    public void clear() {
        HashTrieNode overallTrie = (HashTrieNode) this.root;
        overallTrie.value = null;
        overallTrie.pointers.clear();
        size = 0;
    }
}
