package com.github.dozmus.iterators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An iterator over files in the specified directory, and aggregates them.
 */
public class DirectoryFileIterator implements Iterator<Path> {

    private final Path directory;
    private final List<Path> files = new ArrayList<>(); // the element collection
    private final boolean recursive;
    private int cursor = 0; // the index of the next element to return
    protected boolean initialized;

    /**
     * Creates a new instance with the argument directory.
     *
     * @param directory the directory to iterator over
     * @param recursive if the iteration should be recursive
     */
    public DirectoryFileIterator(Path directory, boolean recursive) {
        this.directory = directory;
        this.recursive = recursive;
    }

    /**
     * Finds the files in the working directory and stores them in {@link #files}.
     */
    public void init() {
        scanDir(directory, recursive);
        initialized = true;
    }

    /**
     * Scans the specified directory using {@link DirectoryFileVisitor}. The results are then added to {@link #files}.
     * Symbolic links are not followed.
     *
     * @param path the directory to be searched
     * @param recursive if the directory should be searched recursively
     * @see DirectoryFileVisitor
     */
    private void scanDir(Path path, boolean recursive) {
        try {
            DirectoryFileVisitor visitor = new DirectoryFileVisitor(recursive);
            Files.walkFileTree(path, visitor);
            files.addAll(visitor.getFiles());
        } catch (IOException e) {
//            LOGGER.error("Error scanning directory: {}", e.getMessage());
            // TODO throw ex
        }
    }

    protected void addFile(Path path) {
        files.add(path);
    }

    public Path getDirectory() {
        return directory;
    }

    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public boolean hasNext() {
        if (!initialized)
            init();
        return cursor < files.size();
    }

    @Override
    public Path next() {
        if (!hasNext())
            throw new NoSuchElementException();
        return files.get(cursor++);
    }
}
