/*
 * Eli Shafer
 * TCSS 342 - Assignment 1
 */

package mylistpackage;

import java.util.Iterator;

/**
 * Represents basic unsorted node-based list.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 * @param <E> is of any object type
 */
public class LinkedListUnsorted<E> extends AbstractLinkedMyList<E> { 
   
	/**
     * Constructs an empty list.
     */
    public LinkedListUnsorted() {
        back = null;
        size = -1;
    }

    /**
     * @see mylistpackage.MyList#insert(java.lang.Object)
     */
    @Override
    public void insert(E value) {
    	ListNode<E> valueNode = new ListNode<E>(value);
        if (size == -1) {
        	back = valueNode;
        	back.next = back;
        } else {
        	valueNode.next = back.next;
        	back.next = valueNode;
        	back = valueNode;
        }
        size++;
    }  

    /*********************************************
     * Index list methods follow
     *********************************************/

    /**
     * Returns the index of value.
     * 
     * @param value assigned.
     * @return index of value if in the list, -1 otherwise.
     */
    public int getIndex(E value) {
        ListNode<E> current = back.next;
        for (int i = 0; i <= size; i++) {
            if (current.data.equals(value)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }
    
    /**
     * Removes value at the given index, shifting subsequent values up.
     * 
     * @param index <= size and index >= 0
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    public void removeAtIndex(int index) {
        checkIndex(index);
        if (index == 0) {
        	if (size == 0)
                back = null;
        	else
        		back.next = back.next.next;
        } else {
            ListNode<E> current = nodeAt(index - 1);
            current.next = current.next.next;
            if (index == size)
                back = current;
        }
        size--;
    }
    
    /**
     * Replaces the value at the given index with the given value.
     * 
     * @param 0 <= index <=size
     * @param value is assigned
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    public void set(int index, E value) {
        checkIndex(index);
        ListNode<E> current = nodeAt(index);
        current.data = value;
    }
    
    /*********************************************
     * Index list methods end
     *********************************************/

    /*********************************************
     * Iterator list class / methods follow
     *********************************************/
    
    /**
     * Returns an iterator for this list.
     * 
     * @return an iterator for the list.
     */
    public Iterator<E> iterator() {
        return new LinkedIterator();
    }
}

