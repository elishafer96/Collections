/*
 * Eli Shafer
 * Assignment 3
 * AvlTree class
 */

package treemap;

// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void put( key, value ) --> Map key to value
// void remove( key )     --> Remove node with key
// AnyValue get( key )    --> Return value mapped to key
// boolean isEmpty( )     --> Return true if empty; else false
// void toString( )       --> Returns String representation of tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree map.
 * Note that all "matching" is based on the compareTo method.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 *
 * @param <AnyKey> a Comparable key to each node
 * @param <AnyValue> a value for each node
 */
public class AvlTree<AnyKey extends Comparable<? super AnyKey>, AnyValue>
implements MyTreeMap<AnyKey, AnyValue>
{
    
    /** The tree root. */
    private AvlNode<AnyKey, AnyValue> root;
    
    /**
     * Construct the tree.
     */
    public AvlTree( )
    {
        root = null;
    }

    /**
     * Put into the tree.
     * @param key the key to insert.
     * @param value the value to insert.
     */
	@Override
	public void put(AnyKey key, AnyValue value) {
		root = put(key, value, root);
	}
	
	/**
     * Returns a value mapped to a key.
     * @param key is the key
     * @return the value associated with the key
     */
	@Override
	public AnyValue get(AnyKey key) {
		return get(key, root);
	}
	
	/**
     * Removes a key-value pair from the tree.
     * @param key is the key to be removed
     */
	@Override
	public void remove(AnyKey key) {
		root = remove(key, root);
	}
	
    /**
     * Internal method to remove from a subtree.
     * @param key the item to remove.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyKey, AnyValue> remove( AnyKey key, AvlNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            return node;   // Item not found; do nothing
            
        int compareResult = key.compareTo( node.key );
            
        if( compareResult < 0 )
            node.left = remove( key, node.left );
        else if( compareResult > 0 )
            node.right = remove( key, node.right );
        else if( node.left != null && node.right != null ) // Two children
        {
        	AvlNode<AnyKey, AnyValue> temp = findMin( node.right );
        	node.key = temp.key;
            node.value = temp.value;

            node.right = remove( node.key, node.right );

        }
        else
            node = ( node.left != null ) ? node.left : node.right;
        return balance( node );
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }
    
    /**
     * Returns tree contents as a string.
     * @return tree contents as a string
     */
	@Override
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
    private String toString(AvlNode<AnyKey, AnyValue> node) {
    	StringBuilder builder = new StringBuilder();
		if (node == null)
			return "";
		builder.append(toString(node.left));
		builder.append(node.toString() + ", ");
		builder.append(toString(node.right));
		return builder.toString();
	}

    private static final int ALLOWED_IMBALANCE = 1;
    
    /**
     * Balances the AvlTree.
     * @param node the root node of the subtree to balance
     * @return the balanced subtree
     */
    private AvlNode<AnyKey, AnyValue> balance( AvlNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            return node;
        
        if( height( node.left ) - height( node.right ) > ALLOWED_IMBALANCE )
            if( height( node.left.left ) >= height( node.left.right ) )
                node = rotateWithLeftChild( node );
            else
                node = doubleWithLeftChild( node );
        else
        if( height( node.right ) - height( node.left ) > ALLOWED_IMBALANCE )
            if( height( node.right.right ) >= height( node.right.left ) )
                node = rotateWithRightChild( node );
            else
                node = doubleWithRightChild( node );

        node.height = Math.max( height( node.left ), height( node.right ) ) + 1;
        return node;
    }
    
    
    /**
     * Internal method to insert into a subtree.
     * @param key the item to insert.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyKey, AnyValue> put( AnyKey key, AnyValue value, 
    									   AvlNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            return new AvlNode<AnyKey, AnyValue>( key, value );
        
        int compareResult = key.compareTo( node.key );
        
        if( compareResult < 0 )
            node.left = put( key, value, node.left );
        else if( compareResult > 0 )
            node.right = put( key, value, node.right );
        else
            ;  // Duplicate; do nothing
        return balance( node );
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param node the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyKey, AnyValue> findMin( AvlNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            return node;

        while( node.left != null )
            node = node.left;
        return node;
    }

    /**
     * Internal method to get a value in a subtree.
     * @param key is item to search for.
     * @param node the node that roots the tree.
     * @return the value mapped to key, null if key is not in the map.
     */
    private AnyValue get( AnyKey key, AvlNode<AnyKey, AnyValue> node )
    {
        while( node != null )
        {
            int compareResult = key.compareTo( node.key );
            
            if( compareResult < 0 )
                node = node.left;
            else if( compareResult > 0 )
                node = node.right;
            else
                return node.value;    // Match
        }

        return null;   // No match
    }

    /**
     * Return the height of node n, or -1, if null.
     */
    private int height( AvlNode<AnyKey, AnyValue> node )
    {
        return node == null ? -1 : node.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyKey, AnyValue> rotateWithLeftChild( AvlNode<AnyKey, AnyValue> node2 )
    {
        AvlNode<AnyKey, AnyValue> node1 = node2.left;
        node2.left = node1.right;
        node1.right = node2;
        node2.height = Math.max( height( node2.left ), height( node2.right ) ) + 1;
        node1.height = Math.max( height( node1.left ), node2.height ) + 1;
        return node1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyKey, AnyValue> 
    rotateWithRightChild( AvlNode<AnyKey, AnyValue> node1 )
    {
        AvlNode<AnyKey, AnyValue> node2 = node1.right;
        node1.right = node2.left;
        node2.left = node1;
        node1.height = Math.max( height( node1.left ), height( node1.right ) ) + 1;
        node2.height = Math.max( height( node2.right ), node1.height ) + 1;
        return node2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyKey, AnyValue> doubleWithLeftChild( AvlNode<AnyKey, AnyValue> node )
    {
        node.left = rotateWithRightChild( node.left );
        return rotateWithLeftChild( node );
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyKey, AnyValue> 
    doubleWithRightChild( AvlNode<AnyKey, AnyValue> node )
    {
        node.right = rotateWithLeftChild( node.right );
        return rotateWithRightChild( node );
    }

    /**
     * Represents a node for the AvlTree.
     * 
     * @author Eli Shafer
     * @version Spring 2017
     *
     * @param <AnyKey> a key to the node
     * @param <AnyValue> the value in the node
     */
    private static class AvlNode<AnyKey, AnyValue>
    {
            // Constructors
        AvlNode( AnyKey key, AnyValue value )
        {
            this( key, value, null, null );
        }

        AvlNode( AnyKey key, AnyValue value, 
        		AvlNode<AnyKey, AnyValue> left, AvlNode<AnyKey, AnyValue> right )
        {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            height = 0;
        }
        
        /**
         * Returns a String representation of this AvlNode.
         * @return String representation of this AvlNode.
         */
        @Override
        public String toString() {
       	 	return key + "=" + value;
        }

        AnyKey    					key;   	// The key to the node
        AnyValue					value; 	// The value in the node
        AvlNode<AnyKey, AnyValue>  	left;	// Left child
        AvlNode<AnyKey, AnyValue>  	right;  // Right child
        int               			height;	// Height
    }
}
