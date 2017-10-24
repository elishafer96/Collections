/*
 * Eli Shafer
 * TCSS 342 - Assignment 1
 */

package mylistpackage;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents an abstract basic node-based list.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 * @param <E> the type that is in the ArrayList
 */
public abstract class AbstractLinkedMyList<E> implements MyList<E> {

	/**
     * Reference to the last node in the list.
     */
    protected ListNode<E> back; 

    /**
     * index of the last list element
     */
    protected int size;
    
    /**
     * Constructs an empty list.
     */
    public AbstractLinkedMyList() {
        back = null;
        size = -1;
    }
    
    /**
     * @see mylistpackage.MyList#getSize()
     */
    @Override
    public int getSize() {
        return size + 1;
    }

    /**
     * @see mylistpackage.MyList#isEmpty()
     */
    public boolean isEmpty() {
        return size == -1;
    }
    
    /**
     * @see mylistpackage.MyList#contains(java.lang.Object)
     */
    public boolean contains(E value) {
        return getIndex(value) >= 0;
    }
	
    /**
     * @see mylistpackage.MyList#clear()
     */
    public void clear() {
        back = null;
        size = -1;
    }
    
    /**
     * Creates a comma-separated, bracketed version of the list.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (size == -1) {
            return "[]";
        } else {
            String result = "[" + back.next.data;
            ListNode<E> current = back.next;
            for (int i = 0; i < size; i++) {
            	result += ", " + current.next.data;
            	current = current.next;
            }
            return result + "]";
        }
    }
    
    /**
     * @see mylistpackage.MyList#remove(java.lang.Object)
     */
    @Override
    public void remove(E value) {
        int location = getIndex(value);
        if (location > -1) 
           removeAtIndex(location);
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
     * Returns the value at the given index in the list.
     * 
     * @param index 0 <= index <=size
     * @throws IndexOutOfBoundsException if index is less that 0 or index is greater than size
     * @return the value at the given index in the list.
     */
    public E get(int index) {
        checkIndex(index);
        ListNode<E> current = nodeAt(index);
        return current.data;
    } 

    /**
     * Returns the node at a specific index.
     * @param index where 0 <= index <= size
     * @return reference to the node at a specific index
     */
    protected ListNode<E> nodeAt(int index) {
        ListNode<E> current = back;
        for (int i = 0; i <= index; i++) {
                current = current.next;
        }
        return current;
    }

    
    /**
     * Checks if the index is a legal index of the current list.
     * @param index
     * @throws IndexOutOfBoundsException if the given index is not a legal index of the current list
     */
    protected void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }
    
    /*********************************************
     * Index list methods end
     *********************************************/
    
    /*********************************************
     * Iterator list class / methods follow
     *********************************************/
    
    /**
     * Represents a list node.
     * @author Building Java Programs 3rd ed.
     *
     * @param <E> is of any object type
     */
    protected static class ListNode<E> {

        /**
         * Data stored in this node.
         */
        public E data; 

        /**
         * Link to next node in the list.
         */
        public ListNode<E> next;  

        /**
         * Constructs a node with given data and a null link.
         * @param data assigned
         */
        public ListNode(E data) {
            this(data, null);
        }

        /**
         * Constructs a node with given data and given link.
         * @param data assigned
         * @param next assigned
         */
        public ListNode(E data, ListNode<E> next) {
            this.data = data;
            this.next = next;
        }
    }
    
    /**
     * Represents an iterator for the list.
     * 
     * @author modified from BuildingJavaPrograms 3rd Edition
     */
    protected class LinkedIterator implements Iterator<E> {
        
        /**
         * Location of current value to return.
         */
        private ListNode<E> current; 

        /**
         * flag that indicates whether list element can be removed.
         */
        private boolean removeOK; 
        
        /**
         * Location of the prior value in case of removal.
         */
        private ListNode<E> prior;

        /**
         * Constructs an iterator for the given list.
         */
        public LinkedIterator() {
            removeOK = false;
            prior = null;
        }

        /**
         * Returns whether there are more list elements.
         * 
         * @return true if there are more elements left, false otherwise
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return current.next != back.next;
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @throws NoSuchElementException if no more elements.
         * @return the next element in the iteration.
         * @see java.util.Iterator#next()
         */
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prior = current;
            E result = current.data;
            current = current.next;
            removeOK = true;
            return result;
        }

        /**
         * Removes the last element returned by the iterator.
         * 
         * @throws IllegalStateException if a call to next has not been made
         *             before call to remove.
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            if (!removeOK) {
                throw new IllegalStateException();
            }
            AbstractLinkedMyList.this.remove(prior.data);
            removeOK = false;
        }
    }

}
