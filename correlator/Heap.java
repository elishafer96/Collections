/*
 * Eli Shafer
 * TCSS 342 - Assignment 4
 */

package correlator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Represents a heap of comparator objects using a max-heap representation
 * of an array.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 *
 * @param <E> the type that the Heap contains
 */
public class Heap<E extends Comparator<E>> {
	/** The array that holds this heaps contents. */
    private E[] elementData;
    
    /** The Comparator objects used to compare HeapEntries. */
    private Comparator<E> comparator;
    
    /** The number of entries currently placed in the Heap. */
    private int size;
    
    /**
     * Constructs a new Heap that uses the specified Comparator.
     * 
     * @param comparator the Comparator used to compare HeapEntries.=
     */
    @SuppressWarnings("unchecked")
	public Heap(Comparator<E> comparator) {
    	this.comparator = comparator;
    	this.elementData = (E[]) new Comparator[10];
    	this.size = 0;
    }
    
    /**
     * Compares two Heap entries.
     * 
     * @param o1 HeapEntry 1
     * @param o2 HeapEntry 2
     * @return a negative number if o1 should be ordered first, a positive number if o2
     * 	 	   should be ordered first, and 0 if it doesn't matter
     */
    private int compare(E o1, E o2) {
    	return comparator.compare(o1, o2);
    }
    
    /**
     * Adds the given element to this Heap.
     * 
     * @param value the element to be added
     */
    public void add(E value) {
        // resize if necessary
        if (size + 1 >= elementData.length) {
            elementData = Arrays.copyOf(elementData, elementData.length * 2);
        }
        
        // insert as new rightmost leaf
        elementData[size + 1] = value;
        
        // "bubble up" toward root as necessary to fix ordering
        int index = size + 1;
        boolean found = false;   // have we found the proper place yet?
        while (!found && hasParent(index)) {
            int parent = parent(index);
        	if (compare(elementData[index], elementData[parent]) < 0) {
                swap(elementData, index, parent(index));
                index = parent(index);
            } else {
                found = true;  // found proper location; stop the loop
            }
        }
        
        size++;
    }
    
    /**
     * Returns true if there are no elements in this Heap.
     * 
     * @return true if there are no elements in this Heap, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns the element with the maximum value in the Heap without modifying
     * the Heap. If the Heap is empty, throws a NoSuchElementException().
     * 
     * @return the element with the maximum value in the Heap
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elementData[1];
    }
    
    /**
     * Removes and returns the element with the maximum value in the Heap.
     * If the Heap is empty, throws a NoSuchElementException.
     * 
     * @return the element with the maximum value in the Heap
     */
    public E remove() {
        E result = peek();

        // move rightmost leaf to become new root
        elementData[1] = elementData[size];
        size--;
        
        // "bubble down" root as necessary to fix ordering
        int index = 1;
        boolean found = false;   // have we found the proper place yet?
        while (!found && hasLeftChild(index)) {
            int left = leftChild(index);
            int right = rightChild(index);
            int child = left;
            if (hasRightChild(index) &&
            		compare(elementData[right], elementData[left]) < 0) {
                child = right;
            }
            
        	if (compare(elementData[index], elementData[child]) > 0) {
                swap(elementData, index, child);
                index = child;
            } else {
                found = true;  // found proper location; stop the loop
            }
        }
        
        return result;
    }
    
    /**
     * Returns the number of elements in the Heap.
     * 
     * @return the number of elements in the Heap.
     */
    public int size() {
        return size;
    }
    
    // Returns a string representation of this queue, such as "[10, 20, 30]";
    // The elements are not guaranteed to be listed in sorted order.
    public String toString() {
        String result = "[";
        if (!isEmpty()) {
            result += elementData[1];
            for (int i = 2; i <= size; i++) {
                result += ", " + elementData[i];
            }
        }
        return result + "]";
    }
    
    
    // helpers for navigating indexes up/down the tree
    private int parent(int index) {
        return index / 2;
    }
    
    // returns index of left child of given index
    private int leftChild(int index) {
        return index * 2;
    }
    
    // returns index of right child of given index
    private int rightChild(int index) {
        return index * 2 + 1;
    }
    
    // returns true if the node at the given index has a parent (is not the root)
    private boolean hasParent(int index) {
        return index > 1;
    }
    
    // returns true if the node at the given index has a non-empty left child
    private boolean hasLeftChild(int index) {
        return leftChild(index) <= size;
    }
    
    // returns true if the node at the given index has a non-empty right child
    private boolean hasRightChild(int index) {
        return rightChild(index) <= size;
    }
    
    // switches the values at the two given indexes of the given array
    private void swap(E[] a, int index1, int index2) {
        E temp = a[index1];
        a[index1] = a[index2];
        a[index2] = temp;
    }
}
