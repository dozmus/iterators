package com.github.dozmus.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A thread-safe wrapper for {@link Iterator}.
 *
 * @param <T> the generic type of the {@link Iterator<T>} to wrap
 */
public class ConcurrentIterator<T> implements Iterator<T> {

    /**
     * The underlying {@link Iterator}.
     */
    private final Iterator<T> it;

    /**
     * Creates a new {@link ConcurrentIterator} wrapping the argument iterator.
     *
     * @param it the iterator to wrap
     */
    public ConcurrentIterator(Iterator<T> it) {
        this.it = it;
    }

    /**
     * Returns {@link #it#hasNext()} in a thread-safe manner.
     */
    @Override
    public boolean hasNext() {
        synchronized (it) {
            return it.hasNext();
        }
    }

    /**
     * Returns {@link #it#next()} in a thread-safe manner.
     *
     * @throws NoSuchElementException if there is no next element
     */
    @Override
    public T next() {
        synchronized (it) {
            if (it.hasNext()) {
                return it.next();
            }
        }
        throw new NoSuchElementException("no more files left");
    }
}
