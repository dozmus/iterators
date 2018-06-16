package com.github.dozmus.iterators.vcs;

import com.github.dozmus.iterators.util.ProcessHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Iterates over files in the specified directory which is home to a subversion repository, by aggregating them.
 *
 * @see VCSRepositoryIterator
 */
public class SubversionRepositoryIterator extends VCSRepositoryIterator {

    /**
     * Creates a new instance with the argument directory.
     *
     * @param directory the directory to search for files in
     */
    public SubversionRepositoryIterator(Path directory) {
        super(directory, "svn", ".svn");
    }

    /**
     * Runs <tt>svn status</tt> and returns its output as a <tt>List</tt>.
     *
     * @return a list of the output paths
     * @throws Exception an error occurred while reading output
     */
    protected List<Path> lsFiles() throws Exception {
        List<String> output;

        try {
            Process p = ProcessHelper.run("svn", "status");
            output = ProcessHelper.readOutput(p);
            p.waitFor(5, TimeUnit.SECONDS);
            p.destroy();
        } catch (InterruptedException | IOException e) {
            throw new Exception("Error running svn status: " + e.getMessage());
        }

        // Check if hg repository found
        if (output.size() == 0) {
            throw new Exception("Error running svn status: no output");
        }

        String line1 = output.get(0);

        if (line1.startsWith("svn: E:") || line1.startsWith("E:")) {
            throw new Exception("Error running svn status: " + line1);
        }

        if (line1.startsWith("'svn' is not recognized as an internal or external command")  // win error msg
                || line1.endsWith("command not found") // linux error msg
                || line1.startsWith("The program 'svn' is currently not installed.")) { // linux error msg
            throw new Exception("Error running svn status: svn is not recognized");
        }

        // Map into a Path list
        return output.stream()
                .filter(s -> s.startsWith(" ") || s.startsWith("A") || s.startsWith("M")
                        || s.startsWith("R") || s.startsWith("C"))
                .map(s -> s.substring(8)) // removes all flags
                .map(fileName -> Paths.get(fileName))
                .collect(Collectors.toList());
    }
}
