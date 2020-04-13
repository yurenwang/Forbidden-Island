package ForbiddenIsland;

import java.util.Iterator;

//to represent a ForbiddenIsland.IListIterator
public class IListIterator<T> implements Iterator<T> {
    IList<T> item;

    // constructor
    IListIterator(IList<T> item) {
        this.item = item;
    }

    // return true if the list of iterator has next
    public boolean hasNext() {
        return this.item.isCons();
    }

    // return the next type of the list iterator
    public T next() {
        if (!this.hasNext()) {
            throw new RuntimeException("No Next");
        }
        else {
            ConsList<T> curLoc = this.item.getCons();
            T result = curLoc.first;
            this.item = curLoc.rest;
            return result;
        }
    }

    //
    public void remove() {
        // do nothing
    }
}