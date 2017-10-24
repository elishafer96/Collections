/*
 * Eli Shafer
 * Assignment 3
 * SplayTree class
 */

package treemap;

// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
//void put( key, value ) --> Map key to value
//void remove( key )     --> Remove node with key
//AnyValue get( key )    --> Return value mapped to key
//boolean isEmpty( )     --> Return true if empty; else false
//void toString( )       --> Returns String representation of tree in sorted order

/**
 * Implements a top-down Splay tree.
 * Note that all "matching" is based on the compareTo method.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 * 
 * @param <AnyKey> a Comparable key to each node
 * @param <AnyValue> a value for each node
 */
public class SplayTree<AnyKey extends Comparable<? super AnyKey>, AnyValue>
implements MyTreeMap<AnyKey, AnyValue>
{
    
    private BinaryNode<AnyKey, AnyValue> root;
    private BinaryNode<AnyKey, AnyValue> nullNode;
    /**
     * Construct the tree.
     */
    public SplayTree( )
    {
        nullNode = new BinaryNode<AnyKey, AnyValue>( null, null );
        nullNode.left = nullNode.right = nullNode;
        root = nullNode;
    }

    private BinaryNode<AnyKey, AnyValue> newNode = null;  // Used between different inserts

    /**
     * Put into the tree.
     * @param key the key to insert.
     * @param value the value to insert.
     */
    public void put( AnyKey key, AnyValue value )
    {
        if( newNode == null )
            newNode = new BinaryNode<AnyKey, AnyValue>( key, value );

        if( root == nullNode )
        {
            newNode.left = newNode.right = nullNode;
            root = newNode;
        }
        else
        {
            root = splay( key, root );

            int compareResult = key.compareTo( root.key );

            if( compareResult < 0 )
            {
                newNode.left = root.left;
                newNode.right = root;
                root.left = nullNode;
                root = newNode;
            }
            else
                if( compareResult > 0 )
                {
                    newNode.right = root.right;
                    newNode.left = root;
                    root.right = nullNode;
                    root = newNode;
                }
                else
                    return;   // No duplicates
        }
        newNode = null;   // So next insert will call new
    }

    /**
     * Removes a key-value pair from the tree.
     * @param key is the key to be removed
     */
    public void remove( AnyKey key )
    {
        BinaryNode<AnyKey, AnyValue> newTree;

        // If x is found, it will be at the root
        root = splay( key, root );
        if( root.key.compareTo( key ) != 0 )
            return;   // Item not found; do nothing

        if( root.left == nullNode )
            newTree = root.right;
        else
        {
            // Find the maximum in the left subtree
            // Splay it to the root; and then attach right child
            newTree = root.left;
            newTree = splay( key, newTree );
            newTree.right = root.right;
        }
        root = newTree;
    }

    /**
     * Returns an item in the tree.
     * @param key the key of the item to get.
     * @return the value of mapped to the key, null if the key is not contained
     * 		   within the map.
     */
    public AnyValue get( AnyKey key )
    {
        if( isEmpty( ) )
            return null;

        root = splay( key, root );

        if (root.key.compareTo( key ) == 0)
        	return root.value;
        else
        	return null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == nullNode;
    }

    private BinaryNode<AnyKey, AnyValue> header = 
    		new BinaryNode<AnyKey, AnyValue>( null, null ); // For splay

    /**
     * Internal method to perform a top-down splay.
     * The last accessed node becomes the new root.
     * @param key the target item to splay around.
     * @param node the root of the subtree to splay.
     * @return the subtree after the splay.
     */
    private BinaryNode<AnyKey, AnyValue> splay( AnyKey key, BinaryNode<AnyKey, AnyValue> node )
    {
        BinaryNode<AnyKey, AnyValue> leftTreeMax, rightTreeMin;

        header.left = header.right = nullNode;
        leftTreeMax = rightTreeMin = header;

        nullNode.key = key;   // Guarantee a match

        for( ; ; )
        {
            int compareResult = key.compareTo( node.key );

            if( compareResult < 0 )
            {
                if( key.compareTo( node.left.key ) < 0 )
                    node = rotateWithLeftChild( node );
                if( node.left == nullNode )
                    break;
                // Link Right
                rightTreeMin.left = node;
                rightTreeMin = node;
                node = node.left;
            }
            else if( compareResult > 0 )
            {
                if( key.compareTo( node.right.key ) > 0 )
                    node = rotateWithRightChild( node );
                if( node.right == nullNode )
                    break;
                // Link Left
                leftTreeMax.right = node;
                leftTreeMax = node;
                node = node.right;
            }
            else
                break;
        }    

        leftTreeMax.right = node.left;
        rightTreeMin.left = node.right;
        node.left = header.right;
        node.right = header.left;
        return node;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     */
    private BinaryNode<AnyKey, AnyValue> rotateWithLeftChild( BinaryNode<AnyKey, AnyValue> node2 )
    {
        BinaryNode<AnyKey, AnyValue> node1 = node2.left;
        node2.left = node1.right;
        node1.right = node2;
        return node1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     */
    private BinaryNode<AnyKey, AnyValue> rotateWithRightChild( BinaryNode<AnyKey, AnyValue> node1 )
    {
        BinaryNode<AnyKey, AnyValue> node2 = node1.right;
        node1.right = node2.left;
        node2.left = node1;
        return node2;
    }
    
    /**
     * Returns tree contents as a string.
     * @return tree contents as a string
     */
    public String toString() {
		if (root == null)
			return "{}";
		String tree = toString(root);
		tree = tree.substring(0, tree.length() - 2);
		return "{" + tree + "}";
    }
    
    /**
     * Internal method to generate the tree contents as a string.
     * @return tree contents as a string
     */
    private String toString(BinaryNode<AnyKey, AnyValue> node) {
    	StringBuilder builder = new StringBuilder();
		
		if( node != node.left )
        {
            builder.append(toString( node.left ));
        	builder.append( node.toString() + ", ");
            builder.append(toString( node.right ));
        }
		return builder.toString();
    }
}
