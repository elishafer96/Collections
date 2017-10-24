package treemap;


//Basic node stored in unbalanced binary search trees
//Note that this class is not accessible outside
//of this package.

class BinaryNode<AnyKey, AnyValue>
{
     // Constructor
 BinaryNode( AnyKey key, AnyValue value )
 {
	 this.key = key;
     this.value = value;
     left = right = null;
 }
 
 /**
  * Returns a String representation of this BinaryNode.
  * @return String representation of this BinaryNode.
  */
 @Override
 public String toString() {
	 return key + "=" + value;
 }

   // Data; accessible by other package routines
 AnyKey				 key;	// The key to the node
 AnyValue            value;  // The value in the node
 BinaryNode<AnyKey, AnyValue> left;     // Left child
 BinaryNode<AnyKey, AnyValue> right;    // Right child
}
