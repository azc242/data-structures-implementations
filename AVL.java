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
public class AVL <E extends Comparable<E>> implements Iterable <E>{
	
	private Node<E> root;
	private int size;
	
	public AVL() {
		root = null;
		size = 0;
	}
	
	public AVL(E[] collection) throws IllegalArgumentException {
		if(collection == null) {
			throw new IllegalArgumentException("Null argument not allowed");
		}
		Arrays.sort(collection);

		adder(collection, 0, collection.length-1);
	}
	
	/**
	 * Adds elements from array into the AVL so that the tree is balanced
	 * @param arr array of E
	 * @param low left subindex
	 * @param high right subindex
	 */
	private void adder(E[] arr, int low, int high) {
		int mid = (low+high)/2;
		
		if(low >= high) {
			this.add(arr[mid]);
		}
		else {
			this.add(arr[mid]);
			adder(arr, low, mid-1);
			adder(arr, mid+1, high);
		}
	}
	
	/**
	 * This class stores a private node inside the AVL class.
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
	 * Iterator class that can iterate through the AVL
	 * The constructor takes in a parameter that acts as a keyword
	 * so the proper iterator is returned.
	 * @author Alan Chen
	 */
	private class Itr implements Iterator<E>{
		
		
		//we can create an iterator with String keyword as argument
		
		private ArrayList<Object> collection = new ArrayList<Object>(size);
		public int count = 0;
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
			return (E) collection.get(count++);
		}
		
		/**
		 * unimplemented method
		 */
		public void remove() throws UnsupportedOperationException {}
		
		/**
		 * Traversal method that puts the elements into ArrayList in proper order
		 * @param root
		 * @param arr
		 * @param order
		 * @return
		 */
		private ArrayList<Object> traverse(Node<E> root, ArrayList<Object> arr, String order) {
			
			if(order.equals("in") && root != null) {
				traverse(root.left, arr, order);
				arr.add(root.data);
				traverse(root.right, arr, order);
			}
			
			if(order.equals("pre") && root != null) {
				arr.add(root.data);
				traverse(root.left, arr, order);
				traverse(root.right, arr, order);
			}
			
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
	 * @param e element being added to AVL
	 * @return whether element has been added or not
	 * @throws NullPointerException when element is null
	 */
	public boolean add( E e ) throws NullPointerException{
		if(e == null) {
			throw new NullPointerException ("element is null");
		}
        int oldSize = size();
        
        if(!this.contains((Object)e)) {
        root = add ( e, root );

        }

        if (oldSize == size())
            return false;
        return true;
    }

	
	
	private Node<E> checkBalance(Node<E> root) {
		
		int balance = balanceFactor(root);
        //checking to see if tree is balanced at the current node
        
        //right height is more than 1 greater than left height
        if(balance > 1) {
        	//right tree's left side is greater
        	if(balanceFactor(root.right) < 0) {
        		root = balanceRL(root);
        	}
        	else {
        		root = balanceRR(root);
        	}
        }
        else if(balance < -1) {
        	//left subtree is right heavy
        	if(balanceFactor(root.left) > 0) { //needs to be 0 because we already know that the BF was not greater than 1 when we recurred up
        		root = balanceLR(root);
        	}
        	else {
        		root = balanceLL(root);
        	}
        }
        return root;
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
	 * @param root of AVL
	 * @return updated Node after element has been added (if possible)
	 */
	private Node<E> add ( E item, Node<E> root ) {

        if ( root == null || root.data == null) {
        	size++;
            return new Node<E>(item);
        }
        if ( root.data.equals(item )) {
            return root;
        }
        else if ( ((Comparable<E>) root.data).compareTo(item) < 0  ) {
            root.right =  add ( item, root.right );
            root.rightSize++;
        }
        else {
            root.left = add ( item, root.left);
            root.leftSize++;
        }
        this.updateHeight(root);
        
        root = (Node<E>) checkBalance(root);
        
        return root;
    }
	
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * @param collection to be added to AVL
	 * @return whether AVL has changed or not
	 * @throws NullPointerException if collection is null or if any of its elements are null
	 */
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException {
		
		if(collection == null) {
			throw new NullPointerException("Collection is null");
		}
		int oldSize = size;

		for(E e: collection) {
			if(e == null) {
				throw new NullPointerException("Null element in collection");
			}
			this.add(e);
		}
		return oldSize != size();
	}
	
	
	/**
	 * Returns the least element greater than or equal to e
	 * or null if there is no such element
	 * @param e element
	 * @return least element greater than e
	 */
	public E ceiling(E e) {
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
		if(((Comparable<E>) root.data).compareTo(e) < 0) {
			return ceiling(root.right, e);
		}
		
		E c = ceiling(root.left, e);
		
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
		root.leftSize = 0;
		root.rightSize = 0;
		root = null;
		size = 0;
		
		this.updateHeight(root);
	}
	
	public AVL<E> clone() {
		AVL<E> clone = new AVL<E>();
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
	 * @return whether object exists in AVL or not
	 * @throws NullPointerException
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) throws NullPointerException, ClassCastException{
		if(o == null) {
			throw new NullPointerException("Null parameter");
		}
		if(root == null) {
			return false;
		}
		if(o.getClass() != root.data.getClass()) {
			throw new ClassCastException("Parameter is of wrong type");
		}
		if(o.equals(root.data)) {
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
	 * AVL or not. Returns true if all elements exist.
	 * @param c Collection of items being searched for within this AVL
	 * @return whether all elements in c exist or not
	 * @throws NullPointerException
	 * @throws ClassCastException
	 */
	public boolean containsAll(Collection<?> c) throws NullPointerException, ClassCastException {
		
		if(c == null) {
			throw new NullPointerException("Null parameter");
		}
		for(Object t: c) {
			if(!this.contains(t)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * Compares 2 AVL objects to see if they are equivalent
	 * @param obj this is being compared to
	 * @return boolean true if both are equal and false if not equal
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		//same elements
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		if(this == obj) {
			return true;
		}
		if(this.size != ((AVL)obj).size()) {
			return false;
		}
		
		Iterator<E> thisAVL = this.iterator();
		Iterator<E> otherAVL = ((AVL)obj).iterator();
		
		//uses 2 iterators to maintain O(N) time
		while(thisAVL.hasNext()) {
			if(thisAVL.next() != otherAVL.next()) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Returns lowest (smallest) element in the BST
	 * @return lowest element
	 * @throws NoSuchElementException
	 */
	public E first() throws NoSuchElementException {
		if(root == null) {
			throw new NoSuchElementException("Set is empty");
		}
		
		Node<E> current = root;
		while(current.left != null) {
			current = current.left;
		}
		return current.data;
	}
	
	
	/**
	 * Returns the largest element less than or equal to e
	 * or null if there is no such element
	 * @param e element
	 * @return largest element less than e
	 */
	public E floor(E e) {
		
		if(e == null) {
			throw new NullPointerException("Null argument");
		}
		else if(e.getClass() != root.data.getClass()) {
			throw new ClassCastException("Wrong object type");
		}
		Node<E> current = root;

		return floor(current, e);
	}
	
	/**
	 * Helper method to get the floor of the BST
	 * @param root
	 * @param e element
	 * @return largest element less than e
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
	 * Gets the value at the given index (assuming inOrder traversal)
	 * @param index
	 * @return value at given index
	 * @throws IndexOutOfBoundsException
	 */
	public E get(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return getHelp(root, index);
	}
	
	/**
	 * Helper method 
	 * @param root
	 * @param index
	 * @return value at given index
	 */
	private E getHelp(Node<E> root, int index) {
		
		//avoid NullPointerException
		if(root == null) {
			return null;
		}
		
		if(root.leftSize - index == 0) {
			return (E)root.data;
		}

		//move right and subtract all the elements (size) on the left tree and root from the index
		else if(index - root.leftSize > 0) {
			return (E) getHelp(root.right, index - root.leftSize - 1);
		}
		//move left
		else {
			return (E) getHelp(root.left, index);
		}


	}
	
	/**
	 * Returns an ArrayList of generic type E containing all the elements
	 * within the given range [fromElement, toElement], inclusive
	 * @param fromElement lower bound
	 * @param toElement upper bound
	 * @return ArrayList of type E containing elements within the range
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
	 * helper function for getting the ArrayList 
	 * @param fromElement lower bound
	 * @param toElement upper bound
	 * @param root
	 * @param list being added to
	 * @return ArrayList of type E containing elements within the range
	 */
	private ArrayList<E> rangeTraverse(E fromElement, E toElement, Node<E> root, ArrayList<E> list) {
		
		if(root == null) {
			return null;
		}
		
		//within the range
		if(((Comparable<E>) root.data).compareTo((E)fromElement) >= 0 &&
				((Comparable<E>) root.data).compareTo(toElement) <= 0) {
			
			rangeTraverse(fromElement, toElement, root.left, list);
			
			//add to ArrayList
			list.add(root.data);

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
	 * Gets the height of the BST
	 * @return returns height
	 */
	public int height() {
		if(root == null) {
			return 0;
		}
		return root.height;
	}
	
	/**
	 * Gets balanceFactor of the given Node
	 * @param n Node whose balance factor is being checked
	 * @return balance factor of the Node
	 */
	public int balanceFactor ( Node<E> n ) {
		
		if( n == null ) {
			return 0;
		}
		if( n.right == null ) {
			return (n.height*-1) + 1; //height is not 0 based indexing 
			}
		if ( n.left == null ) {
			return n.height - 1; //gets rid of 0 based indexing and 1 based indexing for height issue
		}
		return n.right.height - n.left.height;
	}

	/**
	 * Performs single left rotation
	 * @param A Node
	 * @return root of balanced tree
	 */
	private Node<E> balanceLL(Node<E> A) {
		
		Node<E> B = A.left;
		
		A.left = B.right;
		B.right = A;
		
		updateHeight(A);
		updateHeight(B);
		
		return B;
	}
	
	/**
	 * Performs singly right rotation
	 * @param A unbalanced Node
	 * @return root of balanced tree
	 */
	private Node<E> balanceRR(Node<E> A) {
		
		Node<E> B = A.right;
		
		A.right = B.left;
		B.left = A;
		
		updateHeight(A);
		updateHeight(B);
		
		return B;
	}
	
	/**
	 * Performs double left-right rotation
	 * @param A unbalanced Node
	 * @return root of balanced tree
	 */
	private Node<E> balanceLR(Node<E> A) {
		Node<E> B = A.left;
		Node<E> C = B.right;
		
		A.left = C.right;
		B.right = C.left;
		C.left = B;
		C.right = A;
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return C;
	}
	
	/**
	 * Performs a double right-left rotation
	 * @param A unbalanced Node
	 * @return root of balanced tree
	 */
	private Node<E> balanceRL(Node<E> A) {
		Node<E> B = A.right;
		Node<E> C = B.left;
		
		A.right = C.left;
		B.left = C.right;
		C.right = B;
		C.left = A;
		
		updateHeight(A);
		updateHeight(B);
		updateHeight(C);
		
		return C;
	}
	
	
	
	/**
	 * Updates the height of each Node in the AVL
	 * @param n Node being updated
	 */
	private void updateHeight(Node<E> n) {
		
		if(n == null) {
			//do nothing, height is already 0
		}
		
		else if(n.left == null && n.right == null) {
			n.height = 1; //can sometimes be set to 0 to avoid OBO for balance factor
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
	 * Returns the smallest value in the AVL that is greater than the given parameter.
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

	return higherHelper(current, e);

	}
	
	/**
	 * Recursively returns the smallest value in AVL greater than given value
	 * @param root
	 * @param e value
	 * @return proper value, or null if it doesn't exist
	 */
	@SuppressWarnings("unchecked")
	private E higherHelper(Node<?> root, E e) {
		
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo(e) <= 0) {
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
	 * Returns true if AVL is empty, false if it is not
	 * @return size == 0
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Gets last element (largest) in the BST
	 * @return largest element in BST
	 * @throws NoSuchElementException
	 */
	public E last() throws NoSuchElementException {
		
		if(root == null) {
			throw new NoSuchElementException("Set is empty");
		}
		
		Node<E> current = root;
		while(current.right != null) { //move right until reaching right leaf
			current = current.right;
		}
		return (E)current.data;
	}
	

	/**
	 * Returns the largest value in the AVL that is less than the given parameter.
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
	 * Recursively returns the largest value in AVL lower than given value
	 * @param root
	 * @param e value
	 * @return proper value, or null if it doesn't exist
	 */
	@SuppressWarnings("unchecked")
	private E lowerHelper(Node<?> root, E e) {
		
		if(root == null) {
			return null;
		}
		if(((Comparable<E>) root.data).compareTo(e) >= 0) {
			return lowerHelper(root.left, e);
		}
		
		E lower = lowerHelper(root.right, e);
		
		if(lower != null && ((Comparable<E>) lower).compareTo(e) < 0) {
			return lower;
		}
		else {
			return (E)root.data;
		}	
		
	}
	
	/**
	 * Returns an iterator which traverses the AVL in a 
	 * inorder traversal
	 * @return inorder Iterator
	 */
	@Override
	public Iterator<E> iterator() {
		return new Itr("in");
	}
	
	/**
	 * Returns an iterator which traverses the AVL in a 
	 * preOrder traversal
	 * @return preOrder Iterator
	 */
	public Iterator<E> preorderIterator() {
		return new Itr("pre");
		}
	
	/**
	 * Returns an iterator which traverses the AVL in a postOrder traversal
	 * @return postOrder Iterator
	 */
	public Iterator<E> postorderIterator() {

		return new Itr("post");
		}
	
	//boolean to see whether or not the element is already in the AVL or not
	private boolean found;
	
	/**
	 * function removes given element inside a AVL, returns true. Returns false
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
	 * @param target value to find in the AVL
	 * @param node that is being checked for target value
	 * @return entire modified Node, or the root, that has either been modified or not
	 */
    @SuppressWarnings("unchecked")
	private Node<E> recRemove(Object target, Node<E> node)
    {
        if (node == null)
            found = false;
        else if (((Comparable<E>)target).compareTo(node.data) < 0) {
            node.left = recRemove(target, node.left);
            updateHeight(node.left);
            
            node.left = (Node<E>) checkBalance(node.left);
            
            //if we found the node AKA are removing something
            if(found) {
            	node.leftSize--;
            }
        }
        else if (((Comparable<E>) target).compareTo((E) node.data) > 0) {
            node.right = recRemove(target, node.right );
            updateHeight(node.right);
            
            node.right = (Node<E>) checkBalance(node.right);
            
            
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
        
        node = (Node<E>) checkBalance(node);
        
        return node;
    }

    /**
     * Removes the node with given target value from the AVL
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
            temp = temp.right;
        return (E) temp.data;
    }
	
	/**
	 * Returns the size of the AVL, or how many elements are in it
	 * @return size of the AVL
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Utilizes iterator (inOrder) to place elements of AVL into array
	 * @return array of Objects in sorted order
	 */
	public Object[] toArray() {
		int count = 0;
		Object[] arr = new Object[size];
		Iterator<E> itr = this.iterator();
		
		while(itr.hasNext()) {
			arr[count++] = itr.next();
		}
		return arr;
	}
	
	/**
	 * Returns a String representation of AVL in order
	 * @return String Array representation of AVL
	 */
	public String toString() {
		return Arrays.toString(this.toArray());
	}
	

	/**
	 * Formats the AVL for a readable output
	 * @param tree root of the AVL
	 * @param level of the node
	 * @param output formatted AVL
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
    * Returns a formatted and readable AVL 
    * @return formatted AVL
    */
    public String toStringTreeFormat() {

        StringBuilder s = new StringBuilder();

        preOrderPrint(root, 0, s);
        return s.toString();
    }
	
	
}
