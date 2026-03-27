import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head; // Necessary
    private Node<T> tail; // for efficiency
    private int size; // for efficiency

    private int modCount;

    /**
     * initialize new empty list
     */
    public IUSingleLinkedList() {
        head = tail = null;
        size = 0;

        modCount = 0;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<T>(element);
        newNode.setNext(head); // fine if head was null, it just makes null equal null again
        head = newNode;

        // check if list was empty, size == 0 also works, isEmpty uses head == null so
        // that wouldnt work
        // if you want to use isEmpty() would want this if statement before changing
        // head
        if (tail == null) {
            tail = newNode;
        }
        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        // step 1
        Node<T> newNode = new Node<T>(element);

        // step 2
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }

        // step 3
        tail = newNode;
        // step 4
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        Node<T> newNode = new Node<T>(element);

        Node<T> currNode = head;

        while (currNode != null && !currNode.getElement().equals(target)) {
            currNode = currNode.getNext();
        }

        if (currNode == null) {
            throw new NoSuchElementException();
        } else {
            newNode.setNext(currNode.getNext());
            currNode.setNext(newNode);
        }
        size++;
        modCount++;

    }

    @Override
    public void add(int index, T element) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<T>(element);

        Node<T> currNode = head;

        for (int i = 0; i < index - 1; i++) {
            currNode = currNode.getNext();
        }

        newNode.setNext(currNode.getNext());
        currNode.setNext(newNode);

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T retElement = head.getElement();

        head = head.getNext();

        // CPU Garbage Collector already clears element A for you, since nothing is
        // pointing to it anymore

        if (head == null) { // could also do size==1, isEmpty if used head == null, head == tail before
                            // changing head
            tail = null;
        }

        size--;

        modCount++;

        return retElement;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T retElement = tail.getElement();

        if (size == 1) { // special case of single element list
            head = tail = null;
        } else { // "general case" where list is "long" // better to do general case before all
                 // special cases
            Node<T> currNode = head;

            while (currNode.getNext() != tail) {
                currNode = currNode.getNext();
            }

            currNode.setNext(null);
            tail = currNode;
        }
        size--;

        modCount++;
        return retElement;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> currNode = head;
        Node<T> prevNode = null;

        while (currNode != null && !currNode.getElement().equals(element)) {
            prevNode = currNode;
            currNode = currNode.getNext();
        }

        if (currNode == null) {
            throw new NoSuchElementException();
        } else if (currNode == head) { // special case of first element
            return removeFirst();
        } else if (currNode == tail) { // special case of last element
            return removeLast();
        } else { // general case
            prevNode.setNext(currNode.getNext());
            size--;
            modCount++;
            return currNode.getElement();
        }

    }

    @Override
    public T remove(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (index > size - 1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else if(size == 1) {
            return removeFirst();
        } else {

            Node<T> currNode = head;
            Node<T> prevNode = null;

            for(int i = 0; i < index - 1; i++) {
                prevNode = currNode;
                currNode = currNode.getNext();
            }

            prevNode.setNext(currNode.getNext());
            size--;
            modCount++;
            return retNode.getElement();
            
        }
    }

    @Override
    public void set(int index, T element) {
        add(index, element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }

        return currentNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        int retIndex = -1;

        int i = 0;
        Node<T> currNode = head;
        // doesnt work for some reason
        // while(!currNode.getElement().equals(element) && i < size) { //currNode !=
        // null
        // currNode = currNode.getNext();
        // i++;
        // }

        // if(currNode != null) {
        // retIndex = i;
        // }

        while (currNode != null && retIndex < 0) {
            if (currNode.getElement().equals(element)) {
                retIndex = i;
            } else {
                currNode = currNode.getNext();
                i++;
            }

        }

        return retIndex;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        return indexOf(target) > -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // head == null, tail == null, size() == 0
        // head == 0, is also good because size is ++ or --, but necessary because of
        // tests
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

    @Override
    public String toString() {
        // String str = "[";
        // appending with += is bad in this case, because it creates a copy of the prior
        // String
        // concatenation is 0(n^2), only fine with a few things, not lists
        StringBuilder str = new StringBuilder();
        str.append("[");

        for (T element : this) {
            str.append(element.toString());
            str.append(", ");
        }

        // remove trailing comma
        if (str.length() > 1) { // !isEmpty or size() > 0 also works
            str.delete(str.length() - 2, str.length()); // str.length() - 2, is starting index to delete, str.length()
                                                        // is ending index to delete (Exclusive)
        }

        // this toString() works with ALL collections as all require Iterators
        str.append("]");
        return str.toString();
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * basic iterator for single linked list
     */
    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private boolean canRemove;

        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            canRemove = false;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T retEl = nextNode.getElement();

            nextNode = nextNode.getNext();
            canRemove = true;

            return retEl;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!canRemove) {
                throw new IllegalStateException();
            }
            canRemove = false;

            Node<T> currNode = head;

            // special case
            if (head.getNext() == nextNode) {
                head = nextNode;
                if (head == null) { // or size == 1, removing the only el
                    tail = null;
                }
            } else {
                // general case
                // have to have 2 getNext() because if A.getNext() => B, iter points to C, so
                // have to go back 2 from C to remove A
                while (!currNode.getNext().getNext().equals(nextNode)) {
                    currNode = currNode.getNext();
                }
                currNode.setNext(nextNode);
            }

            if (nextNode == null) { // or currNode.getNext() == null
                tail = currNode;
            }

            modCount++;
            iterModCount++;
            size--;

        }

    }

}
