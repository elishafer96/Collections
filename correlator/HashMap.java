/*
 * Eli Shafer
 * TCSS 342 - Assignment 4
 */

package correlator;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import treemap.MyTreeMap;

// Implements a set of objects using a hash table.
// The hash table uses separate chaining to resolve collisions.

/**
 * Represents a hash map that uses separate chaining to resolve collisions.
 * @author Eli Shafer
 * @version Spring 2017
 *
 * @param <K> the key type to this HashMap
 * @param <V> the value type for this HashMap
 */
public class HashMap<K, V> implements MyTreeMap<K, V> {
	
	/** The max load factor for this Hash Map. */
    private static final double MAX_LOAD_FACTOR = 0.75;
    
    /** The array that holds the hash buckets for this HashMap. */
    private HashEntry<K, V> elementData[];
    
    /** The number of HashEntries currently placed in this HashMap. */
    private int size;
    
    /**
     * Constructs an empty HashMap.
     */
	@SuppressWarnings("unchecked")
	public HashMap() {
        elementData = (HashEntry<K, V>[]) 
        		Array.newInstance(new HashEntry<K, V>(null, null).getClass(), 11);
        size = 0;
    }

    /**
     * Maps a value to a key.
     */
	@Override
	public void put(K key, V value) {
		boolean containsKey = containsKey(key);
		if (loadFactor() >= MAX_LOAD_FACTOR)
			rehash();
		
		// Insert new HashEntry at the front of the list
		int bucket = hashFunction(key);
		if (containsKey) {
			HashEntry<K, V> curr = elementData[bucket];
			while (curr != null) {
				if (curr.key.equals(key)) {
					curr.value = value;
					break;
				}
				curr = curr.next;
			}
		} else {
			elementData[bucket] = new HashEntry<K, V>(key, value, elementData[bucket]);
			size++;
		}
	}
	
	/**
     * Returns the value mapped to the specified key.
     * 
     * @return the value mapped to the specified key
     */
	@Override
	public V get(K key) {
		int bucket = hashFunction(key);
    	HashEntry<K, V> current = elementData[bucket];
    	while (current != null) {
    		if (current.key.equals(key))
    			return current.value;
    		current = current.next;
    	}
    	return null;
	}
    
    /**
     * Removes the given key and value mapping.
     * 
     * @param key the key
     */
	@Override
	public void remove(K key) {
		int bucket = hashFunction(key);
    	if (elementData[bucket] != null) {
    		if (elementData[bucket].key.equals(key)) {
    			elementData[bucket] = elementData[bucket].next;
    			size--;
    		} else {
    			HashEntry<K, V> current = elementData[bucket];
    			while (current.next != null && !current.next.key.equals(key))
    				current = current.next;
    			
    			if (current.next != null && current.next.key.equals(key)) {
    				current.next = current.next.next;
    				size--;
    			}
    		}
    	}
	}
    	
	/**
	 * Removes all elements from the HashMap.
	 */
    public void clear() {
        for (int i = 0; i < elementData.length; i++) {
            elementData[i] = null;
        }
        size = 0;
    }
    
    /**
     * Determines if this HashMap contains the specified key.
     * 
     * @param key the key to check if HashMap contains
     * @return true if this HashMap contains key, false otherwise
     */
    public boolean containsKey(K key) {
    	int bucket = hashFunction(key);
    	HashEntry<K, V> current = elementData[bucket];
    	while(current != null) {
    		if (current.key.equals(key))
    			return true;
    		current = current.next;
    	}
    	return false;
    }
        
    /**
     * Returns true if there are no elements in this HashMap.
     * 
     * @return true if there are no elements in this HashMap, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
	
	/**
	 * Returns the size of the intersection between this HashMap and another.
	 * 
	 * @param o the other HashMap
	 * @return size of the intersection of the two HashMaps
	 */
	public int intersection(HashMap<K, V> o) {
		int count = 0;
		
		if (!isEmpty()) {
            for (int i = 0; i < elementData.length; i++) {
                HashEntry<K, V> current = elementData[i];
                while (current != null) {
                	if (o.containsKey(current.key))
                		count++;
                	current = current.next;
                }
            }
        }
		
		return count;
	}
	
	/**
	 * Returns the size of the union of this HashMap and another.
	 * 
	 * @param o the other HashMap
	 * @return size of the union of the two HashMaps
	 */
	public int union(HashMap<K, V> o) {
		int count = this.size + o.size;
		
		if (!isEmpty()) {
            for (int i = 0; i < elementData.length; i++) {
                HashEntry<K, V> current = elementData[i];
                while (current != null) {
                    if (o.containsKey(current.key))
                    	count--;
                    current = current.next;
                }
            }
        }
		
		return count;
	}
	
	/**
	 * Returns an iterator for this HashMap.
	 * 
	 * @return an interator for this HashMap
	 */
	public HashMapIterator iterator() {
		return new HashMapIterator();
	}
    
	/**
	 * Returns the number of elements in this HashMap.
	 * 
	 * @return the number of elements in this HashMap
	 */
    public int size() {
        return size;
    }
    
    /**
     * Returns a String representation of this HashMap, such as "[aaa=12, bbb=134]".
     * The elements are not guaranteed to be listed in a sorted order.
     * 
     * @return a String representation of this HashMap
     */
    public String toString() {
        String result = "[";
        boolean first = true;
        if (!isEmpty()) {
            for (int i = 0; i < elementData.length; i++) {
                HashEntry<K, V> current = elementData[i];
                while (current != null) {
                    if (!first) {
                        result += ", ";
                    }
                    result += current.key + "=" + current.value;
                    first = false;
                    current = current.next;
                }
            }
        }
        return result + "]";
    }
    
    /**
     * Returns the preferred hash bucket index for the given key.
     * 
     * @param key the key to determine the hash bucket
     * @return the hash bucket index
     */
    private int hashFunction(K key) {
        return Math.abs(key.hashCode()) % elementData.length;
    }
    
    /**
     * Determines the current load factor for this HashMap.
     * 
     * @return the current load factor for this HashMap
     */
    private double loadFactor() {
        return (double) size / elementData.length;
    }
    
    /**
     * Resizes the HashMap to a prime number greater than its current length.
     */
    @SuppressWarnings("unchecked")
	private void rehash() {
        // replace element data array with a larger empty version
        HashEntry<K, V>[] oldElementData = elementData;
        elementData = (HashEntry<K, V>[]) 
        		Array.newInstance(new HashEntry<K, V>(null, null).getClass(), 
        						  nextPrime(oldElementData.length + 1));
        
        HashEntry<K, V> curr;
        for (int i = 0; i < oldElementData.length; i++) {
        	curr = oldElementData[i];
        	while (curr != null) {
        		
        		// Insert new HashEntry at the front of the list
        		int bucket = hashFunction(curr.key);
    			elementData[bucket] = new HashEntry<K, V>(curr.key, curr.value, elementData[bucket]);
        		curr = curr.next;
        	}
        }
    }
    
    /**
     * Returns a set of all of the keys to this HashMap.
     * 
     * @return a set of all of the keys to this HashMap
     */
    public Set<K> keySet() {
    	Set<K> keySet = new HashSet<>();
    	
    	if (!isEmpty()) {
            for (int i = 0; i < elementData.length; i++) {
                HashEntry<K, V> current = elementData[i];
                while (current != null) {
                    keySet.add(current.key);
                    current = current.next;
                }
            }
        }
    	
    	
    	return keySet;
    }
		
	/**
	 * Returns a prime number that is equal to or greater than the specified integer.
	 * 
	 * @param n the number
	 * @return a prime number that is equal to or greater than n
	 */
	private static int nextPrime(int n) {
		if (n % 2 == 0)
			n++;
		while (!isPrime(n))
			n += 2;
		return n;
	}
	
	/**
	 * Determines whether the specified integer is prime or not.
	 * 
	 * @param n the integer
	 * @return true if n is an integer, false otherwise
	 */
	private static boolean isPrime(int n) {
		if (n <= 1)
			return false;
		if (n % 2 == 0 && n > 2)
			return false;
		for (int i = 3; i < n / 2; i += 2)
			if (n % i == 0)
				return false;
		
		return true;
	}
	
	/**
     * Represents a HashEntry with a key and value pair.
     * 
     * @author Eli Shafer
     * @version Spring 2017
     *
     * @param <K> the key type for this HashEntry
     * @param <V> the value type for this HashEntry
     */
    @SuppressWarnings("hiding")
	private class HashEntry<K, V> {
    	
    	/** The key to this HashEntry. */
        private K key;
        
        /** The value for this HashEntry. */
        private V value;
        
        /** The HashEntry that this HashEntry points to. */
        public HashEntry<K, V> next;

        /**
         * Constructs a HashEntry with the specified key and value pair.
         * 
         * @param key the specified key
         * @param value the specified value
         */
        public HashEntry(K key, V value) {
            this(key, value, null);
        }

        /**
         * Constructs a HashEntry with the specified key and value pair that points to
         * the specified next HashEntry.
         * 
         * @param key the specified key
         * @param value the specified value
         * @param next the next HashEntry
         */
        public HashEntry(K key, V value, HashEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

	/**
	 * An iterator for this HashMap.
	 * 
	 * @author Eli Shafer
	 * @version Spring 2017
	 */
	public class HashMapIterator implements Iterator<HashEntry<K, V>> {

		/** The current position of the iterator. */
		private int currentPos = -1;
		
		/** The number of entries this iterator has visited. */
		private int visited = 0;
		
		/**
		 * Returns true if there is another entry to iterate to.
		 */
		@Override
		public boolean hasNext() {
			return visited != size;
		}

		/**
		 * Returns the next entry.
		 */
		@Override
		public HashEntry<K, V> next() {
			if (!hasNext())
				throw new NoSuchElementException();
			
			do {
				currentPos++;
			} while (currentPos < elementData.length);

			visited++;
			return elementData[currentPos];
		}
	}
	
}