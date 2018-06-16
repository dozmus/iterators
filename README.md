iterators
========

A collection of iterators and related utilities for Java, provided under the MIT license.

| Iterator | Purpose |
|---|---|
| DirectoryFileIterator | Iterates over a directory, recursively or otherwise. |
| FilterableIterator | Iterates over a directory, with a filter. |
| ConcurrentIterator | A wrapper for an Iterator to provide thread-safe operations on it. |
| VCSRepositoryIterator | Each subclass of this iterates over tracked files in a version control repository system. This includes support for Git, Mercurial, and Subversion. |

# Creating a custom ignore file
You can create your own ignore file, like the `.gitignore` file using `IgnoreFile`.

1. Parse the ignore file using `IgnoreFile.read(baseDirectory, ignoreFile)`.
   `baseDirectory` is folder the ignore file is being applied to.
2. Apply the rules using a `FilterableIterator`, which passes each file it may iterate over to `IgnoreFile.ignored(file, rules)`.
