package ForbiddenIsland;

import java.util.Iterator;

//to represent a cons list of type T
public class ConsList<T> implements IList<T> {

    T first;
    IList<T> rest;

    //the constructor
    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // return a list of iterator
    public Iterator<T> iterator() {
        return (Iterator<T>) new IListIterator<T>(this);
    }

    // check if the list is a cons
    public boolean isCons() {
        return true;
    }

    // return a cons list
    public ConsList<T> getCons() {
        return this;
    }

    // add the given type T in the list
    public IList<T> add(T t) {
        return new ConsList<T>(t, this);
    }

    // return the size of the list
    public int size() {
        return 1 + this.rest.size();
    }

    // return true if the give type is in the list
    public boolean has(T t) {
        return (t.equals(this.first)) || this.rest.has(t);
    }


    // return the list after remove the given type
    public IList<T> remove(T t) {
        if (t == this.first) {
            return this.rest;
        }
        else {
            return new ConsList<T>(this.first, this.rest.remove(t));
        }
    }

    // return a list of helicopter pieces, which the player collected
    public IList<T> pickUp(Cell player) {
        if (((Target) this.first).isPicked(player)) {
            return this.remove(this.first);
        }
        else {
            return new ConsList<T>(this.first, this.rest.pickUp(player));
        }
    }
}
