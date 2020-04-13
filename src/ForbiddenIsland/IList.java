package ForbiddenIsland;

import java.util.Iterator;

//to represent a list of T
interface IList<T> extends Iterable<T> {
    // check if the list is a cons
    boolean isCons();
    // return  a cons list in cons
    ConsList<T> getCons();
    // return a list of type T iterator
    Iterator<T> iterator();
    // add the given type T to the list
    IList<T> add(T t);
    // return the size of the list
    int size();
    // check if the list has the given type T
    boolean has(T t);
    // remove type T from the list
    IList<T> remove(T t);
    // return a list of type T that the player pick up 
    IList<T> pickUp(Cell player);
}