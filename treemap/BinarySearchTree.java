/*
 * Eli Shafer
 * Assignment 3
 * BinarySearchTree class
 */

package treemap;

//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void put( key, value ) --> Map key to value
//void remove( key )     --> Remove node with key
//AnyValue get( key )    --> Return value mapped to key
//boolean isEmpty( )     --> Return true if empty; else false
//void toString( )       --> Returns String representation of tree in sorted order

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * 
 * @author Eli Shafer
 * @version Spring 2017
 * 
 * @param <AnyKey> a Comparable key to each node
 * @param <AnyValue> a value for each node
 */
public class BinarySearchTree<AnyKey extends Comparable<? super AnyKey>, AnyValue> 
implements MyTreeMap<AnyKey, AnyValue>
{
    
    /** The tree root. */
    protected BinaryNode<AnyKey, AnyValue> root;
    
    /**
     * Construct the tree.
     */
    public BinarySearchTree( )
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
		BinaryNode<AnyKey, AnyValue> node = get( key, root );
		return node == null ? null : node.value;
	}

	/**
     * Removes a key-value pair from the tree.
     * @param key is the key to be removed
     */
	@Override
	public void remove(AnyKey key) {
		root = remove( key, root );
		
	}
	
	/**
     * Returns tree contents as a String.
     * @return tree contents as a String
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
     * Internal method to insert into a subtree.
     * @param key the item to insert.
     * @param node the node that roots the tree.
     * @return the new root.
     */
    protected BinaryNode<AnyKey, AnyValue> put( AnyKey key, AnyValue value, 
    											BinaryNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            node = new BinaryNode<AnyKey, AnyValue>( key, value );
        else if( key.compareTo( node.key ) < 0 )
            node.left = put( key, value, node.left );
        else if( key.compareTo( node.key ) > 0 )
            node.right = put( key, value, node.right );
        else
        	node.value = value;
        return node;
    }

    /**
     * Internal method to remove from a subtree.
     * @param key the item to remove.
     * @param node the node that roots the tree.
     * @return the new root.
     * @throws ItemNotFoundException if x is not found.
     */
    protected BinaryNode<AnyKey, AnyValue> remove( AnyKey key, 
    											   BinaryNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            throw new IllegalArgumentException( key.toString( ) );
        if( key.compareTo( node.key ) < 0 )
            node.left = remove( key, node.left );
        else if( key.compareTo( node.key ) > 0 )
            node.right = remove( key, node.right );
        else if( node.left != null && node.right != null ) // Two children
        {
            node.value = getMin( node.right ).value;
            node.right = removeMin( node.right );
        }
        else
            node = ( node.left != null ) ? node.left : node.right;
        return node;
    }

    /**
     * Internal method to remove minimum item from a subtree.
     * @param node the node that roots the tree.
     * @return the new root.
     * @throws ItemNotFoundException if t is empty.
     */
    protected BinaryNode<AnyKey, AnyValue> removeMin( BinaryNode<AnyKey, AnyValue> node )
    {
        if( node == null )
            throw new IllegalArgumentException( );
        else if( node.left != null )
        {
            node.left = removeMin( node.left );
            return node;
        }
        else
            return node.right;
    }    

    /**
     * Internal method to find the smallest item in a subtree.
     * @param node the node that roots the tree.
     * @return node containing the smallest item.
     */
    protected BinaryNode<AnyKey, AnyValue> getMin( BinaryNode<AnyKey, AnyValue> node )
    {
        if( node != null )
            while( node.left != null )
                node = node.left;

        return node;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param key is item to search for.
     * @param node the node that roots the tree.
     * @return node containing the matched item.
     */
    private BinaryNode<AnyKey, AnyValue> get( AnyKey key, BinaryNode<AnyKey, AnyValue> node )
    {
        while( node != null )
        {
            if( key.compareTo( node.key ) < 0 )
                node = node.left;
            else if( key.compareTo( node.key ) > 0 )
                node = node.right;
            else
                return node;    // Match
        }

        return null;         // Not found
    }
    
    /**
     * Internal method to generate the tree contents as a string.
     * @return tree contents as a string
     */
    private String toString(BinaryNode<AnyKey, AnyValue> node) {
    	StringBuilder builder = new StringBuilder();
		if (node == null)
			return "";
		builder.append(toString(node.left));
		builder.append(node.toString() + ", ");
		builder.append(toString(node.right));
		return builder.toString();
	}
}
