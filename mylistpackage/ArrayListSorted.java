/*
 * Eli Shafer
 * TCSS 342 - Assignment 1
 */
package mylistpackage;

/**
 * Represents basic sorted node-based list.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 * @param <E> is of any object type
 */
public class ArrayListSorted<E extends Comparable<? super E>> extends AbstractArrayMyList<E> {

	/**
	 * Constructs an empty list of default capacity.
	 */
	public ArrayListSorted() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Constructs an empty list of the given capacity.
	 * 
	 * @param capacity
	 *            > 0
	 * @throws IllegalArgumentException
	 *             if capacity <= 0
	 */
	@SuppressWarnings("unchecked")
	public ArrayListSorted(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("capacity: " + capacity);
		}
		elementData = (E[]) new Comparable[capacity];
		size = -1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		elementData = (E[]) new Comparable[DEFAULT_CAPACITY];
		size = -1;
	}

	@Override
	public boolean contains(E value) {
		return getIndex(value) != -1;
	}

	@Override
	public void insert(E value) {
		ensureCapacity(size + 2);
		int index = getIndex(value, true);
		for (int i = size + 1; i > index; i--)
			elementData[i] = elementData[i - 1];
		elementData[index] = value;
		size++;
	}

	@Override
	public void remove(E value) {

		int index = getIndex(value);
		if (index > -1)
			removeAtIndex(index);
	}

	/*********************************************
	 * Index list methods follow
	 *********************************************/

	@Override
	public int getIndex(E value) {
		return getIndex(value, false);
	}
	
	/**
	 * Checks this ArrayListSorted for the specified value. If this method is
	 * used to insert, it will return the index of insertion, while if it is
	 * used to check if a certain value is stored in the ArrayListSorted, -1
	 * will be returned if the value is not within the list.
	 * 
	 * @param value
	 *            the value to search for
	 * @param lowerBound
	 *            the lower bound for searching for the value
	 * @param upperBound
	 *            the upper bound for searching for the value
	 * @param isInserting
	 *            indicator of whether or not this method is being used for
	 *            insertion or not
	 * @return if used for insertion, the index of insertion will be returned,
	 *         if not used for insertion, the index of the value will be
	 *         returned if it is contained in the list, -1 will be returned if
	 *         it is not contained in the list
	 */
	private int getIndex(E value, boolean isInserting) {
		int lowerBound = 0;
		int upperBound = size;
		while (lowerBound <= upperBound) {
			int midPoint = (lowerBound + upperBound) / 2;
			if (value.equals(elementData[midPoint]))
				return midPoint;
			else if (value.compareTo(elementData[midPoint]) > 0)
				lowerBound = midPoint + 1;
			else if (value.compareTo(elementData[midPoint]) < 0)
				upperBound = midPoint - 1;
		}
		if (isInserting)
			return Math.max(lowerBound, upperBound);
		return -1;
	}

	/**
     * Removes value at the given index, shifting subsequent values up.
     * 
     * @param index <= size and index >= 0
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
	@Override
	public void removeAtIndex(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		for (int i = index; i < size; i++)
			elementData[i] = elementData[i + 1];
		elementData[size] = null;
		size--;

	}

	/**
     * Replaces the value at the given index with the given value.
     * 
     * @param 0 <= index <=size
     * @param value is assigned
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     * @throws IllegalArgumentException if index is not the correct index
     */
	@Override
	public void set(int index, E value) {
		int validIndex = getIndex(value, true);
		System.out.println(validIndex);
		if (index < 0 || index > size) 
			throw new IndexOutOfBoundsException();
		if (index != validIndex) 
			throw new IllegalArgumentException();
		elementData[index] = value;
	}

	/*********************************************
	 * Index list methods end
	 *********************************************/

}
