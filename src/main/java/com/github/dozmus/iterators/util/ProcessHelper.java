package com.github.dozmus.iterators.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of utility methods for dealing with processes.
 *
 * @see Process
 */
public final class ProcessHelper {

    /**
     * Starts a process with the description in the argument and then returns it.
     *
     * @param command a string array containing the program and its arguments
     * @return the process started
     *
     * @throws IOException if an I/O error occurs
     * @see ProcessBuilder
     */
    public static Process run(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        return pb.start();
    }

    /**
     * Returns the output in a {@code List<String>} for the argument.
     *
     * @param process the process to read the output of
     * @return the output of the program
     *
     * @throws IOException if an I/O error occurs
     */
    public static List<String> readOutput(Process process) throws IOException {
        List<String> output = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = br.readLine()) != null) {
                output.add(line);
            }
        }
        return output;
    }
}
