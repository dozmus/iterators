package com.github.dozmus.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An {@link Iterator} which allows filtering the elements it contains, by a {@link Predicate} filter.
 *
 * @param <T> the generic type of the {@link Iterator} to wrap
 */
public class FilterableIterator<T> implements Iterator<T> {

    private final Iterator<T> it;
    private final Predicate<T> filter;
    private T nextElement;

    /**
     * Constructs a new {@link FilterableIterator} with the arguments.
     *
     * @param it the iterator to wrap
     * @param filter the filter to apply to the iterator
     */
    public FilterableIterator(Iterator<T> it, Predicate<T> filter) {
        this.it = it;
        this.filter = filter;
        findNextElement();
    }

    /**
     * Finds the next element in {@link #it} and assigns it to {@link #nextElement}. If no next element is found, then
     * <tt>null</tt> is assigned to it instead.
     */
    private void findNextElement() {
        while (it.hasNext()) {
            T next = it.next();

            if (filter.test(next)) {
                nextElement = next;
                return;
            }
        }
        nextElement = null;
    }

    @Override
    public boolean hasNext() {
        return nextElement != null;
    }

    @Override
    public T next() {
        if (hasNext()) {
            T element = nextElement;
            findNextElement();
            return element;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        it.forEachRemaining(action);
    }
}
