package ForbiddenIsland;

import java.util.Iterator;

//to represent an empty list of T
public class MtList<T> implements IList<T> {

    // return a list of type T iterator
    public Iterator<T> iterator() {
        return (Iterator<T>) new IListIterator<T>(this);
    }

    // check if the list is a cons
    public boolean isCons() {
        return false;
    }

    // return error 
    public ConsList<T> getCons() {
        throw new ClassCastException("error");
    }

    // add the given type T in the list
    public IList<T> add(T t) {
        return new ConsList<T>(t, this);
    }

    // return the size of the list
    public int size() {
        return 0;
    }

    // check if the list has type T
    public boolean has(T t) {
        return false;
    }

    // remove type T from the list
    public IList<T> remove(T t) {
        return new MtList<T>();
    }

    // return a list of type T that the player pick up
    public IList<T> pickUp(Cell player) {
        return new MtList<T>();
    }
}