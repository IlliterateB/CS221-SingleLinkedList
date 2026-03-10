import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported. 
 * 
 * @author 
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (T[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0;
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {

		// says this is bad
		// T[] newArray = (T[])(new Object[array.length * 2]);
		// for(int i = 0; i < array.length; i++) {
		// 	newArray[i] = array[i];
		// }
		// array = newArray;

		// this is the same but simpler
		array = Arrays.copyOf(array, array.length*2);
	}

	@Override
	public void addToFront(T element) {
		// TODO 
		if (rear == array.length) {
			expandCapacity();
		}
		for (int i = rear; i > 0; i--) {
			array[i] = array[i-1];
		}
		array[0] = element;
		rear++;
		modCount++;

		// the reason to not just use add(0, element) is because
		// it just gives more practice
		// and because arrrays are great with indices,
		// but ALL other collections are
		// extremely slow comparatively
	}

	@Override
	public void addToRear(T element) {
		add(element);
	}

	@Override
	public void add(T element) {
		// TODO 
		if (rear == array.length) {
			expandCapacity();
		}
		array[rear] = element;
		rear++;
		modCount++;
	}

	@Override
	public void addAfter(T element, T target) {
		// TODO 
		if (rear == array.length) {
			expandCapacity();
		}

		if (indexOf(target) == NOT_FOUND) {
			throw new NoSuchElementException();
		}

		for (int i = 0; i < rear; i++) {
			if (array[i].equals(target)) {
				add(i+1, element);
				return;
			}
		}
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		// test if index is valid, throw exception if not
		if (index < 0 || index > rear) {
			throw new IndexOutOfBoundsException();
		}

		if (rear == array.length) {
			expandCapacity();
		}
		for (int i = rear; i > index; i--) {
			array[i] = array[i-1];
		}
		array[index] = element;
		rear++;
		modCount++;
		
	}

	@Override
	public T removeFirst() {
		// TODO 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T element = array[0];
		for (int i = 0; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}
		array[rear - 1] = null;
		rear--;
		modCount++;
		return element;
	}

	@Override
	public T removeLast() {
		// TODO
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T element = array[rear - 1];
		array[rear - 1] = null;
		rear--;
		modCount++;
		return element;
	}


	@Override
	public T remove(T element) {
		int index = indexOf(element);

		// NOT_FOUND is equal to -1, which indexOf() returns if the element is not found
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		// already given in file, remove(index) works and reduces code-duplication
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		// had to store the old value to be returned, is object type T
		T oldVal = array[index];

		for(int i = index; i < rear-1; i++) {
			array[i] = array[i+1];
		}
		array[rear - 1] = null;
		rear--;
		modCount++;

		return oldVal;
	}

	@Override
	public void set(int index, T element) {
		// TODO 
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		array[index] = element;
		modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}

		T element = array[index];
		return element;
	}

	@Override
	public int indexOf(T element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

	@Override
	public T first() {
		// TODO 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return array[0];
	}

	@Override
	public T last() {
		// TODO 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return array[rear - 1];
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		// TODO 
		return rear == 0;
	}

	@Override
	public int size() {
		return rear;
	}


	@Override
	public String toString() {
		// String str = "[";
		// appending with += is bad in this case, because it creates a copy of the prior String
		// concatenation is 0(n^2), only fine with a few things, not lists
		StringBuilder str = new StringBuilder();
		str.append("[");

		// loop thru array and add to str
			// for (int i = 0; i < rear; i++) {
			// 	str.append(array[i]);
			// 	str.append(", ");
			// }
		// this only works with arrayLists, must use iterators for other collections 

		// for-each loop does this but makes it simpler
			// Iterator<T> iter = this.iterator();
			// while(iter.hasNext()) {
			// 	str.append(iter.next().toString());
			// 	str.append(", ");
			// }


		for(T element : this) {
			str.append(element.toString());
			str.append(", ");

			// don't want to add if statement in this to remove trailing comma
			// because that would add coefficients to the Order
		}
		
		// remove trailing comma
		if (str.length() > 1) {  // !isEmpty or size() > 0 also works
			str.delete(str.length() - 2, str.length()); // str.length() - 2, is starting index to delete, str.length() is ending index to delete (Exclusive)
		}

		// this toString() works with ALL collections as all require Iterators
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new ALIterator();
	}

	// only in double linked list
	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	// only in double linked list
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUArrayList */
	private class ALIterator implements Iterator<T> {
		private int nextIndex;
		private int iterModCount;
		private boolean canRemove;
		
		public ALIterator() {
			nextIndex = 0;
			iterModCount = modCount;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			
			return nextIndex < rear;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			nextIndex++;

			canRemove = true;
			return array[nextIndex - 1];
		}

		// optional in iterators, because it was added after creation
		// reqd for assignment
		@Override
		public void remove() {
			// TODO
			if(iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if(!canRemove) {
				throw new IllegalStateException();
			}
			
			for (int i = nextIndex-1; i < rear - 1; i++) {
				array[i] = array[i+1];
			}
			array[rear-1] = null;
			rear--;
			
			modCount++; // scope in all iterators
			iterModCount++; // scope in this iterator

			nextIndex--; // have to move nextIndex because items are shifted left
			canRemove = false;
		}
	}
}
