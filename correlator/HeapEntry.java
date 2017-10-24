/*
 * Eli Shafer
 * TCSS 342 - Assignment 4
 */

package correlator;

import java.util.Comparator;

/**
 * Class that represents a HeapEntry with a key and value pair.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 *
 * @param <K> key
 * @param <V> value
 */
public class HeapEntry<K extends Comparable<? super K>, V extends Comparable<? super V>> 
implements Comparator<HeapEntry<K, V>> {
	
	/** The key for this HeapEntry. */
	private K key;
	
	/** The value for this HeapEntry. */
	private V value;
	
	/**
	 * Constructs a new HeapEntry with null fields.
	 */
	public HeapEntry() {
		this(null, null);
	}

	/**
	 * Constructs a new HeapEntry with the specified key and value.
	 * 
	 * @param key the specified key
	 * @param value the specified value
	 */
	public HeapEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Defines how two HeapEntries are to be compared.
	 * 
	 * @return negative int if o1 should be ordered first, 0 if ordering is meaningless,
	 * 		   positive int if o2 should be ordered first
	 */
	@Override
	public int compare(HeapEntry<K, V> o1, HeapEntry<K, V> o2) {
		int result = o2.value.compareTo(o1.value);
		if (result == 0)
			result = o1.key.compareTo(o2.key);
		return result;
	}
	
	/**
	 * Generates a String representation of this HeapEntry.
	 * 
	 * @return a String representation of this HeapEntry
	 */
	@Override
	public String toString() {
		return key.toString() + "=" + value.toString();
	}
	
	/**
	 * Returns the key to this HeapEntry.
	 * 
	 * @return the key to this HeapEntry
	 */
	public K getKey() {
		return key;
	}
	
	/**
	 * Returns the value of this HeapEntry.
	 * 
	 * @return the value of this HeapEntry.
	 */
	public V getValue() {
		return value;
	}
}
