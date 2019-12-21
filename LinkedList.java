package project3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Alan Chen
 * Generic Singly Linked List. Stores a pointer to the head and tail
 * to improve performance of certain functions. Has a size variable to
 * calculate size more efficiently.
 */
public class LinkedList<E> implements Collection<E>,  Iterable<E> {
	
	/**
	 * 
	 * @author Alan Chen
	 * Node class stores element and keeps track of next Node
	 * @param <E>
	 */
	private static class Node<E>{
		
		private E element;
		private Node<E> next;
		
		public Node(E e) {
			element = e;
			}
		public Node(E e, Node<E> next) {
			element = e;
			this.next = next;
		}
		
		/**
		 * returns element inside Node
		 * @return element
		 */
		public E getElement() {
			return element;
		}
		
		/**
		 * gets the next Node
		 * @return next
		 */
		public Node<E> getNext() {
			return next;
		}
	}
	
	
	private Node<E> head;
	private Node<E> tail;
	private int size;
	
	
	public LinkedList(){
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * compares 2 LinkedLists 
	 * @return true if objects being compared are equal
	 */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (getClass( ) != o.getClass( )) {
			return false;
		}
		LinkedList<E> other = (LinkedList) o; 
		
		//return false if Linked Lists have different sizes
		if (size != other.size) {
			return false;
		}
		//iterates through each Linked List 
		Node<E> itA = head; // traverse the primary list
		Node<E> itB = other.head; // traverse the secondary list
		while (itA != null) {
			if (!itA.getElement().equals(itB.getElement())) {
				return false; 
			}
			//retrieve next element to compare
			itA = itA.getNext();
			itB = itB.getNext();
			}
		//All elements match
		return true;
		}
	
	/**
	 * returns size of LinkedList
	 * @return size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * removes Node at the given index and returns element in that Node
	 * @param pos
	 * @return element 
	 */
	public E remove (int pos ) {
    	if (pos<0 || pos >= size  ) throw new IllegalArgumentException("bad position"); 
    	
    	//removes head if LinkedList of pos == 0
    	if (pos == 0 ) {
    		Node<E> current = head; 
    		head = head.next; 
    		size -- ;
    		return (E)current.element; 
    	}
    	
    	//acts as an iterator
    	Node<E> prev = head;
    	Node<E> cur = head.next; 
    	int at = 1; 
    	
    	while ( at < pos ) {
    		prev = prev.next; 
    		cur = cur.next; 
    		at++;
    	}
    	//removes pointer to Node at index pos and sets pointer to the Node after it
    	prev.next = cur.next; 
    	size--; 
    	
    	return (E)cur.element;
    	
    }
   
	/**
	 * removes element from LinkedList
	 * @return true if element is removed and false if element is not in List
	 * @param o
	 */
    @Override
    public boolean remove(Object o) {
    	if(!contains(o)) {
    		return false;
    	}
    	int index = indexOf(o);
    	remove(index);
    	return true;
    }
	
    /**
     * sorts LinkedList
     */
	public void sort ( ) {
		//creates array with LinkedList's elements and sorts
		Object [] array = toArray();
		Arrays.sort(array);
		this.clear();
		//stores sorted array elements back into LinkedList after clearing it
		for (Object o : array ) {
		this.add( (E)o );
		}
	}
	
	/**
	 * sorts LinkedList based on Comparator
	 * @param comp
	 */
	public void sort ( Comparator <E> comp ) {
		//creates array with LinkedList's elements and sorts
		Object [] array = toArray();
		Arrays.sort((E[])array, comp);
		this.clear();
		//stores sorted array elements back into LinkedList after clearing it
		for (Object o : array ) {
		this.add( (E)o );
		}
	}
	
	/**
	 * returns string containing elements in LinkedList enclosed by "[]"
	 * @return string of LinkedList elements
	 */
	public String toString() {
		if(size==0) {
			return "[]";
		}
		StringBuilder str = new StringBuilder();
		str.append("[");
		Node<E> current = head;
		str.append(current.element);
		//current = current.next;
		
		if(size > 1) {

			//works only if size is not 0
			for(int i = 1; i < size; i++) {
				current = current.next;
				str.append(", " + current.element);
			}
		}
		str.append("]");
		return str.toString();
	}
	
	/**
	 * checks and returns index of object given in the LinkedList
	 * returns -1 if object is not present 
	 * @param c
	 * @return index of parameter in the list
	 *
	 */
	//TEST INDEX OFF TO MAKE SURE IT DOESNT TEST REFERENCE
	public int indexOf(Object c) {
		int count = 0;
		Node current = head;
		while ( current != null) {
			if(current.element.equals(c)) {
				return count;
			}
			current = current.next;
			count++;
		}
		//returns -1 when item is not present in the list
		return -1;
	}
	
	/**
	 * returns the element at the given index
	 * @param index index of the element
	 * @return generic E
	 * @throws IndexOutOfBoundsException
	 */
	public E get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("index is less than 0 or greater than list size");
		}
		Node<E> t = head;
		for(int i = 0; i < index; i++) {
			t = t.next;
		}
		return (E)t.element;
	}

	/**
	 * returns whether LinkedList is empty or not
	 * @return size == 0
	 */
	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	/**
	 * searches the LinkedList for Object o and returns true if present
	 * and false if not present
	 * @param o Object that may or may not be present in the list
	 * @return true if parameter is present in LinkedList
	 */
	@Override
	public boolean contains(Object o) {
		if(head == null) return false;
		
		Node<E> current = head;
		while(current != null) {
			if(current.element.equals(o)) {
				return true;
			}
			current = current.next;
		}
		
		return false;
	}

	/**
	 * creates and returns an iterator for the LinkedList
	 * @return itr Iterator
	 */
	@Override
	public Iterator<E> iterator() {
		
		Iterator<E> itr = new Itr(); 
		
		return itr;
	}
	
	/**
	 * nested class that allows Iteration 
	 * implements Iterator<E> interface
	 * 
	 */
	private class Itr implements Iterator<E>{
		
		Node<E> current = head;

		@Override
		public boolean hasNext() {
			
			if (current != null ) 
				return true; 
			
			return false;
			}

		/**
		 * returns the next element 
		 * @throws NoSuchElementException when there is no next element
		 */
		@Override
		public E next() throws NoSuchElementException{
			if (current == null) {
				throw new NoSuchElementException("There is no next element");
			}
			E curr = (E) current.element;
			current = current.next;
			
			return curr;
		}
		
	}

	/**
	 * copies each element in the LinkedList into an array
	 * and returns the array
	 * @return array of each element in the LinkedList
	 */
	@Override
	public Object[] toArray() {
		Node<E> current = head;
		Object[] ret = new Object[size];
		//stores elements into array
		for(int i = 0; i < size; i++) {
			ret[i] = current.element;
			current = current.next;
		}
		return ret;
	}

	
	/**
	 * copies each element from the LinkedList into
	 * the array given
	 * if the array is too small, a new array is created
	 * @param a array of Object to to be returned
	 * @return given array containing all elements from
	 * the LinkedList 
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		T[] arr = a;
		Node<E> current  = head;
		
		//create new array if given array is not large enough
		if(a.length < size) {
			arr = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
			for(int i = 0; i < size; i++) {
				arr[i] = (T) current.element;
				current = current.next;
			}
			return (T[])arr;
		}
		for(int i = 0; i < size; i++) {
			a[i] = (T)current.element;
			current = current.next;
			}
		return a;
		}

	/**
	 * Adds element regardless of whether it already exists or not
	 * @return true
	 * @throws IllegalArgumentException if parameter is null
	 */
	@Override
	public boolean add(E e) throws IllegalArgumentException{
		if(e == null) {
			throw new IllegalArgumentException("Not allowing null parameter");
		}
		if(isEmpty()) {
			head = new Node(e);
			tail = head;
			size++;
		}
		else {
			tail.next = new Node(e);
			tail = tail.next;
			size++;
		}
		return true;
	}
	
	/**
	 * checks if all elements of COllection<?> c are present in the LinkedList
	 * @return true if all elements are present in LinkedList
	 * @param c Collection of elements
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		if(c.size() > size) {
			return false;
		}
		//return false if any element in Collection c is not present
		for(Object a: c) {
			if(!this.contains(a)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds all of the elements in the specified collection
	 * to this collection 
	 * @throws UnsupportedOperationException due to no implementation
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("No method implementation");
	}
	
	/**
	 * Removes all of the elements in the specified collection
	 * @throws UnsupportedOperationException due to no implementation
	 */
	@Override
	public boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("No method implementation");
	}

	/**
	 * Retains only the elements in this collection that are 
	 * contained in the specified collection
	 * @throws UnsupportedOperationException due to no implementation
	 */
	@Override
	public boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("No method implementation");
	}
	
	/**
	 * Returns the hash code value for this collection.
	 * @return int hashCode of collection
	 */
	public int hashCode() {
		return super.hashCode();
		}
	
	/**
	 * removes all Nodes from LinkedList
	 */
	@Override
	public void clear() {
		for(Object o: this) {
			remove(o);
		}
		size = 0;
	}


}
