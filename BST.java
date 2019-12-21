package project6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is a generic Binary Search Tree class. 
 * Contains a private static Node class
 * @author Alan Chen
 *
 * @param <E> generic element
 */
public class BST <E extends Comparable<E>> implements Iterable <E>{
	
	private Node<E> root;
	private int size;
	
	public BST() {
		root = null;
		size = 0;
	}
	
	public BST(E[] collection) throws IllegalArgumentException {
		if(collection == null) {
			throw new IllegalArgumentException("Null argument not allowed");
		}
		Arrays.sort(collection);

		adder(collection, 0, collection.length-1);
	}
	
	/**
	 * Adds elements from array into the BST so that the tree is balanced
	 * @param arr array of E
	 * @param low left subindex
	 * @param high right subindex
	 */
	private void adder(E[] arr, int low, int high) {
		//median of this sub array
		int mid = (low+high)/2;
		
		if(low >= high) {
			this.add(arr[mid]);
		}
		else {
			this.add(arr[mid]);
			adder(arr, low, mid-1);  //recur on left half
			adder(arr, mid+1, high);  //recur on right half
		}
	}
	
	/**
	 * This class stores a private node inside the BST class.
	 * The class has data fields for a Left node, Right node.
	 * Stores height for making returning height O(1) time.
	 * Stores left and right height of node to make get(index)
	 * function run in efficient time.
	 * @author Alan Chen
	 *
	 * @param <E> generic element
	 */
	private static class Node <E>{
		
		private E data;
		private Node<E> left;
		private Node<E> right;
		private int height;
		private int leftSize;
		private int rightSize;
		
		public Node(E e) {
			data = e;
			height = 1;
			leftSize = 0;
			rightSize = 0;
		}
	}

	/**
	 * Iterator class that can iterate through the BST
	 * The constructor takes in a parameter that acts as a keyword
	 * so the proper iterator is returned.
	 * @author Alan Chen
	 */
	private class Itr implements Iterator<E>{
		
		
		//we can create an iterator with String keyword as argument
		
		private ArrayList<Object> collection = new ArrayList<Object>(size);
		private int count = 0;
		public Itr(String keyword) {
			collection = traverse(root, collection, keyword);
		}

		@Override
		public boolean hasNext() {
			return count < collection.size();
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() throws NoSuchElementException{
			if(!hasNext()) {
				throw new NoSuchElementException("No next element");
			}
			return (E)collection.get(count++);
		}
		
		/**
		 * unimplemented method
		 */
		public void remove() throws UnsupportedOperationException {}
		
		/**
		 * Traversal method that puts the elements into ArrayList in proper order
		 * @param root
		 * @param arr collection being added to
		 * @param order keyword of which traversal type
		 * @return
		 */
		private ArrayList<Object> traverse(Node<E> root, ArrayList<Object> arr, String order) {
			
			//inOrder traversal
			if(order.equals("in") && root != null) {
				traverse(root.left, arr, order);
				arr.add(root.data);
				traverse(root.right, arr, order);
			}
			
			//preOrder traversal
			if(order.equals("pre") && root != null) {
				arr.add(root.data);
				traverse(root.left, arr, order);
				traverse(root.right, arr, order);
			}
			
			//postOrder traversal
			if(order.equals("post") && root != null) {
				traverse(root.left, arr, order);
				traverse(root.right, arr, order);
				arr.add(root.data);
			}
			return arr;			
		}
	}
	
	/**
	 * Updates the root with the added Node with given element
	 * @param e element being added to BST
	 * @return whether element has been added or not
	 * @throws NullPointerException when element is null
	 */
	public boolean add ( E e ) throws NullPointerException{
		if(e == null) {
			throw new NullPointerException ("element is null");
		}
        int oldSize = size();
        root  =  add ( e, root ); //replaces the root with completely updated tree

        //nothing was added
        if (oldSize == size())
            return false;
        return true;
    }
	
	/**
	 * Gets the left height of a Node
	 * @return leftSize, however many elements are in the left subtree
	 */
	public int leftSize() {
		if (root == null) {
			return 0;
		}
		return root.leftSize;
	}
	/**
	 * Gets the right height of a Node
	 * @return rightSize, however many elements are in the right subtree
	 */
	public int rightSize() {
		if(root == null) {
			return 0;
		}
		return root.rightSize;
	}	
	
	/**
	 * 
	 * @param item being added
	 * @param root of BST
	 * @return updated Node after element has been added (if possible)
	 */
    private Node<E> add ( E item, Node<E> root ) {

    	//adding to empty tree
        if ( root == null || root.data == null) {
        	size++;
            return new Node<E>(item);
        }
        
        //item already exists
        if ( root.data.equals(item )) {
            return root;
        }
        //item is less than current Node's data
        else if ( ((Comparable<E>) root.data).compareTo(item) < 0  ) {
            root.right =  add ( item, root.right );
            root.rightSize++;
        }
        //item is larger than current Node's data
        else {
            root.left = add ( item, root.left);
            root.leftSize++;
        }
        this.updateHeight(root);
        return root;
    }
	
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * @param collection to be added to BST
	 * @return whether BST has changed or not
	 * @throws NullPointerException if collection is null or if any of its elements are null
	 */
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException {
		
		//Null parameter
		if(collection == null) {
			throw new NullPointerException("Collection is null");
		}
		int oldSize = size;

		for(E e: collection) {
			this.add(e);
		}
		return oldSize != size;
	}
	
	
	/**
	 * Returns the least element greater than or equal to e
	 * or null if there is no such element
	 * @param e element
	 * @return least element greater than e
	 */
	public E ceiling(E e) throws NullPointerException, ClassCastException{
		if(root == null) {
			return null;
		}
		if(e == null) {
			throw new NullPointerException("Null argument");
		}
		else if(e.getClass() != root.data.getClass()) {
			throw new ClassCastException("Wrong object type");
		}
		Node<E> current = root;

		return ceiling(current, e);
	}
	
	/**
	 * Returns proper element that is the least element
	 * greater than given element
	 * back to ceiling wrapper method
	 * @param root
	 * @param e element
	 * @return E smallest element that is greater than given element
	 */
	private E ceiling( Node<E> root, E e) {
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo((E)e) < 0) { //current Node element too small
			return (E) ceiling(root.right, e);
		}
		
		E c = (E) ceiling(root.left, e);
		
		if(c != null && ((Comparable<E>) c).compareTo((E)e) >= 0) {
			return c;
		}
		else {
			return (E)root.data;
		}	
	}
	
	/**
	 * Clears the Binary Search Tree
	 */
	public void clear() {
		root = null;
		size = 0;
	}
	
	/**
	 * Clones the BST, creating a shallow copy
	 * @return shallow copy of BST
	 */
	public BST<E> clone() {
		BST<E> clone = new BST<E>();
		Iterator<E> pre = this.preorderIterator();
		
		while(pre.hasNext()) {
			clone.add(pre.next());
		}
		return clone;
	}
	
	
	/**
	 * 
	 * Checks whether given object exists in the binary search tree
	 * @param o Object being searched for
	 * @return whether object exists in BST or not
	 * @throws NullPointerException
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) throws NullPointerException, ClassCastException{
		if(o == null) { //null parameter
			throw new NullPointerException("Null parameter");
		}
		if(root == null) { //null root
			return false;
		}
		if(o.getClass() != root.data.getClass()) { //wrong class type
			throw new ClassCastException("Parameter is of wrong type");
		}
		if(o.equals(root.data)) { //element is in root
			return true;
		}
		Node<E> current  = root;
		while(current != null) {
			
			if(((Comparable<E>)current.data).compareTo((E) o) == 0) {
				return true;
			}
			
			else if(((Comparable<E>)current.data).compareTo((E) o) < 0) {
				current = current.right;
			}
			else {
				current = current.left;
			}
		}
		//object not found
		return false;
	}
	
	
	/**
	 * Checks whether each element in given collection exists within the
	 * BST or not. Returns true if all elements exist.
	 * @param c Collection of items being searched for within this BST
	 * @return whether all elements in c exist or not
	 * @throws NullPointerException
	 * @throws ClassCastException
	 */
	public boolean containsAll(Collection<?> c) throws NullPointerException, ClassCastException {
		if(c == null) {
			throw new NullPointerException("Null parameter");
		}
		if(root == null) {
			return false;
		}
		for(Object t: c) { //checks each object
			if(!this.contains(t)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * Compares 2 BST objects to see if they are equivalent
	 * @param obj this is being compared to
	 * @return boolean true if both are equal and false if not equal
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		
		if(obj == null) {
			return false;
		}
		//same elements
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		//same reference
		if(this == obj) {
			return true;
		}
		//BST are of different lengths
		if(this.size != ((BST<E>)obj).size()) {
			return false;
		}
		
		Iterator<E> thisBST = this.iterator();
		Iterator<E> otherBST = ((BST<E>)obj).iterator();
		
		//uses 2 iterators to maintain O(N) time
		while(thisBST.hasNext()) {
			if(thisBST.next() != otherBST.next()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the smallest element in the BST
	 * @return first, or smallest element in the BST
	 * @throws NoSuchElementException when BST is empty
	 */
	public E first() throws NoSuchElementException {
		if(root == null) {
			throw new NoSuchElementException("Set is empty");
		}
		
		Node <E> current = root;
		while(current.left != null) {
			current = current.left;
		}
		return (E) current.data;
	}
	
	
	/**
	 * Returns the largest element less than or equal to e
	 * or null if there is no such element
	 * @param e element
	 * @return largest element less than e
	 */
	public E floor(E e) throws NullPointerException, ClassCastException{
		
		if(e == null) {
			throw new NullPointerException("Null argument");
		}
		else if(e.getClass() != root.data.getClass()) {
			throw new ClassCastException("Wrong object type");
		}
		Node<E> current = root;

		return (E) floor(current, e);
	}
	
	/**
	 * helper class that returns the floor
	 * @param root
	 * @param e
	 * @return correct floor
	 */
	private E floor(Node<E> root, E e) {
		
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo((E)e) > 0) {
			return (E) floor(root.left, e);
		}
		
		E f = (E) floor(root.right, e);
		
		if(f != null && ((Comparable<E>) f).compareTo((E)e) <= 0) {
			return f;
		}
		else {
			return (E)root.data;
		}	
		
	}
	
	
	/**
	 * Gets the element at the given index (in order) in O(H) time
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public E get(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return getHelp(root, index);
	}
	
	private E getHelp(Node<E> root, int index) {
		
		//avoid NullPointerException
		if(root == null) {
			return null;
		}
		
		//root is at the index
		if(root.leftSize - index == 0) {
			return (E)root.data;
		}

		//index is in the right child/branch
		else if(index - root.leftSize > 0) {
			return getHelp(root.right, index - root.leftSize - 1);
		}
		else {
			return getHelp(root.left, index);
		}
	}
	
	/**
	 * Gets an ArrayList filled with all elements between fromElement and 
	 * toElement, inclusive
	 * @param fromElement lower bound
	 * @param toElement upper bound
	 * @return ArrayList with elements in the range
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList <E> getRange(E fromElement, E toElement) throws NullPointerException, IllegalArgumentException {
		
		if(fromElement == null || toElement == null) {
			throw new NullPointerException("Parameters can not be null");
		}
		if(fromElement.compareTo(toElement) > 0) {
			throw new IllegalArgumentException("fromElement can not be greater than toElement");
		}
		ArrayList<E> list = new ArrayList<E>();
		return rangeTraverse(fromElement, toElement, root, list);
	}
	
	/**
	 * Traverses the BST to find elements within the range given
	 * @param fromElement
	 * @param toElement
	 * @param root
	 * @param list
	 * @return
	 */
	private ArrayList<E> rangeTraverse(E fromElement, E toElement, Node<E> root, ArrayList<E> list) {
		
		if(root == null) {
			return null;
		}
		
		//within the range
		if(((Comparable<E>) root.data).compareTo(fromElement) >= 0 &&
				((Comparable<E>) root.data).compareTo(toElement) <= 0) {
			
			rangeTraverse(fromElement, toElement, root.left, list);
			
			//add to arraylist 
			list.add((E)root.data);

			rangeTraverse(fromElement, toElement, root.right, list);
		}
		
		//if not within the range
		else {	
			//if root data is less than fromElement then recur right	
			if(((Comparable<E>) root.data).compareTo(fromElement) < 0) {
				rangeTraverse(fromElement, toElement, root.right, list);
			}
			
		
			//if root data is more than toElement then recur left
			if(((Comparable<E>) root.data).compareTo(toElement) > 0) {
				rangeTraverse(fromElement, toElement, root.left, list);
			}
		}
		
		return list;
	}
	
	/**
	 * Returns height of the BST
	 * @return height
	 */
	public int height() {
		if(root == null) {
			return 0;
		}
		
		return root.height;
	}
	
	/**
	 * Updates the height of each Node in the BST
	 * @param n Node being updated
	 */
	private void updateHeight(Node<E> n) {
		
		if(n == null) {
			//do nothing, height is already 0
		}
		else if(n.left == null && n.right == null) {
			n.height = 1; //this is sometimes set to 0
		}
		else if (n.left == null) { 
			n.height = n.right.height + 1;
		}
		else if (n.right == null) {
			n.height = n.left.height + 1;
		}
		else {
			if(n.right.height > n.left.height) {
				n.height = n.right.height + 1;
			}
			else {
				n.height = n.left.height + 1;
			}
		}
	}
	
	/**
	 * Returns the smallest value in the BST that is greater than the given parameter.
	 * Returns null if value doens't exist
	 * @param e value
	 * @return E value largest value greater than e
	 */
	public E higher(E e) throws ClassCastException, NullPointerException {

	if(e == null) {
		throw new NullPointerException("Null argument");
	}
	else if(e.getClass() != root.data.getClass()) {
		throw new ClassCastException("Wrong object type");
	}
	Node<E> current = root;
	return (E) higherHelper(current, e);
	}
	
	/**
	 * Recursively returns the smallest value in BST greater than given value
	 * @param root
	 * @param e value
	 * @return proper value, or null if it doesn't exist
	 */
	private E higherHelper(Node<E> root, E e) {
		
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo((E)e) <= 0) {
			return higherHelper(root.right, e);
		}
		
		E higher = higherHelper(root.left, e);
		
		if(higher != null && ((Comparable<E>) higher).compareTo((E)e) > 0) {
			return higher;
		}
		else {
			return (E)root.data;
		}	
	}
	
	/**
	 * Returns true if BST is empty, false if it is not
	 * @return size == 0
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the lowest element
	 * @return lowest element
	 * @throws NoSuchElementException
	 */
	public E last() throws NoSuchElementException {
		
		if(root == null) {
			throw new NoSuchElementException("Set is empty");
		}
		
		Node<E> current = root;
		while(current.right != null) {
			current = current.right;
		}
		return (E)current.data;
	}
	

	/**
	 * Returns the largest value in the BST that is less than the given parameter.
	 * Returns null if value doens't exist
	 * @param e value
	 * @return E value largest value less than e
	 */
	public E lower(E e) throws ClassCastException, NullPointerException{
		if(e == null) {
			throw new NullPointerException("Null argument");
		}
		else if(e.getClass() != root.data.getClass()) {
			throw new ClassCastException("Wrong object type");
		}
		return lowerHelper(root, e);
	}
	
	/**
	 * Recursively returns the largest value in BST lower than given value
	 * @param root
	 * @param e value
	 * @return proper value, or null if it doesn't exist
	 */
	private E lowerHelper(Node<E> root, E e) {
		
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo((E)e) >= 0) {
			return lowerHelper(root.left, e);
		}
		
		E lower = lowerHelper(root.right, e);
		
		if(lower != null && ((Comparable<E>) lower).compareTo((E)e) < 0) {
			return lower;
		}
		else {
			return (E)root.data;
		}	
		
	}
	
	/**
	 * Returns an iterator which traverses the BST in a 
	 * inOrder traversal
	 * @return inOrder Iterator
	 */
	@Override
	public Iterator<E> iterator() {
		return new Itr("in");
	}
	
	/**
	 * Returns an iterator which traverses the BST in a 
	 * preOrder traversal
	 * @return preOrder Iterator
	 */
	public Iterator<E> preorderIterator() {
		return new Itr("pre");
		}
	
	/**
	 * Returns an iterator which traverses the BST in a postorder traversal
	 * @return postorder Iterator
	 */
	public Iterator<E> postorderIterator() {

		return new Itr("post");
		}
	
	//boolean to see whether or not the element is already in the BST or not
	private boolean found;
	
	/**
	 * function removes given element inside a BST, returns true. Returns false
	 * if element was not found.
	 * @param val 
	 * @return whether element has been found and removed or not
	 */
	public boolean remove(Object o) throws ClassCastException, NullPointerException {
		if(o == null) {
			throw new NullPointerException("Specified element is null");
		}
        found = false;
        root = recRemove(o, root);
        if(found) {
        	size--;
        }
        return found;
    }

	/**
	 * Finds given node containing the target value
	 * @param target value to find in the BST
	 * @param node that is being checked for target value
	 * @return entire modified Node, or the root, that has either been modified or not
	 */
    @SuppressWarnings("unchecked")
	private Node<E> recRemove(Object target, Node<E> node)
    {
        if (node == null) {
            found = false;
        }
        else if (((Comparable<E>) target).compareTo(node.data) < 0) {
            node.left = recRemove(target, node.left);
            updateHeight(node.left);
            //if we found the node AKA are removing something
            if(found) {
            	node.leftSize--;
            }
        }
        else if (((Comparable<E>) target).compareTo((E) node.data) > 0) {
            node.right = recRemove(target, node.right );
            updateHeight(node.right);
            if(found) {
            	node.rightSize--;
            }
        }
        else {
        	node.leftSize--;
            node = removeNode(node);
            found = true;
        }

        updateHeight(node);
        return node;
    }

    /**
     * Removes the node with given target value from the BST
     * @param node
     * @return updated branch after given node has been removed
     */
	private Node<E> removeNode(Node<E> node) {
        E data;

        if (node.left == null)
            return node.right ;
        else if (node.right  == null)
            return node.left;

        else {
            data = (E) getPredecessor(node.left);
            node.data = data;
            node.left = recRemove(data, node.left);
            return node;
        }
    }

    /**
     * Finds the predecessor, or the largest value on the left branch of the 
     * root to replace the node that is being replaced
     * @param subtree
     * @return element E that is the predecessor
     */
    private E getPredecessor(Node<E> subtree)
    {
        Node<E> temp = subtree;
        while (temp.right  != null)
            temp = temp.right ;
        return (E) temp.data;
    }
	
	/**
	 * Returns the size of the BST, or how many elements are in it
	 * @return size of the BST
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Utilizes iterator (inorder) to place elements of BST into array
	 * @return array of Objects in sorted order
	 */
	public Object[] toArray() {
		int count = 0;
		Object[] arr = new Object[size];
		Iterator<E> itr = this.iterator();
		
		while(itr.hasNext()) { //using iterator to update each index of array
			arr[count++] = itr.next();
		}
		return arr;
	}
	
	/**
	 * Returns a String representation of BST in order
	 */
	public String toString() {
		return Arrays.toString(this.toArray());
		}
	

	/**
	 * Formats the BST for a readable output
	 * @param tree root of the BST
	 * @param level of the node
	 * @param output formatted BST
	 */
   private void preOrderPrint(Node<E> tree, int level, StringBuilder output) {
        if (tree != null) {
            String spaces = "\n";
            if (level > 0) {
                for (int i = 0; i < level - 1; i++)
                    spaces += "   ";
                spaces += "|--";
            }
            output.append(spaces);
            output.append(tree.data);
            preOrderPrint(tree.left, level + 1, output);
            preOrderPrint(tree.right, level + 1, output);
        }
        // uncomment the part below to show "null children" in the output
        else {
            String spaces = "\n";
            if (level > 0) {
                for (int i = 0; i < level - 1; i++)
                    spaces += "   ";
                spaces += "|--";
            }
            output.append(spaces);
            output.append("null");
        }
    }


   /**
    * Returns a formatted and readable BST 
    * @return formatted BST
    * @author Joanna Klukowska
    */
    public String toStringTreeFormat() {

        StringBuilder s = new StringBuilder();

        preOrderPrint(root, 0, s);
        return s.toString();
    }
	
	
}
