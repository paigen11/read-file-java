package com.example.readFile.readFileJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Alternate implementation exploring the effects of tuning the amount of data stored in memory and the data structures
 * used throughout.
 *
 * Based on {@link com.example.readFile.readFileJava.ReadFileJavaApplicationBufferedReader}, diversions will be noted
 * as comments in the body of {@link ReadFileJavaApplicationStoringOnlySummaryData#main(String[])}
 */
public class ReadFileJavaApplicationStoringOnlySummaryData {

    public static void main(String[] args) {
        try {
            File f = new File(Common.getPathToTargetFile(args));
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {

                String readLine;

                Instant start = Instant.now();
                int lines = 0;

                // While the runtime differences between a lookup for ArrayList or HashSet for a 3 element collection
                // are very minimal, HashSet is much more clear about the intended purpose of `indexes`.
                Set<Integer> indexes = new HashSet<>();
                indexes.add(1);
                indexes.add(433);
                indexes.add(43244);

                // Because only the summary data is stored, all that's needed is the count for each month or name so
                // we can eliminate all but two of the data structures that were used to hold intermediate values.
                Map<String, Integer> donationsPerYearMonth = new HashMap<>();
                Map<String, Integer> firstNameOccurrences = new HashMap<>();

                while ((readLine = reader.readLine()) != null) {
                    lines++;
                    String array1[] = readLine.split("\\s*\\|\\s*");
                    String name = array1[7];
                    if (indexes.contains(lines)) {
                        // Storing, then immediately retrieving the name was simplified down to simply printing
                        // the values.
                        System.out.println("Name: " + name + " at index: " + (lines - 1));
                    }

                    if (name.contains(", ")) {
                        String array2[] = (name.split(", "));
                        String firstHalfOfName = array2[1].trim();

                        if (!firstHalfOfName.isEmpty()) {
                            if (firstHalfOfName.contains(" ")) {
                                String array3[] = firstHalfOfName.split(" ");
                                String firstName = array3[0].trim();
                                // As we only care about how many times a particular first name shows up,
                                // we don’t need to store every instance we come across, just storing a
                                // single copy and how many times it occurs is more than sufficient.
                                firstNameOccurrences.merge(firstName, 1, (a, b) -> a + b);
                            } else {
                                // This would have been possible even if we didn't have `merge`, but having
                                // it does make things nicely concise.
                                firstNameOccurrences.merge(firstHalfOfName, 1, (a, b) -> a + b);
                            }
                        }
                    }

                    String rawDate = array1[4];
                    String month = rawDate.substring(4, 6);
                    String year = rawDate.substring(0, 4);
                    String formattedDate = month + "-" + year;

                    // Same optimization here as with the first name.
                    donationsPerYearMonth.merge(formattedDate, 1, (a, b) -> a + b);
                }

                // One of the side effects of moving most of the logic into the loop is that there
                // isn’t much to time outside the loop. On the upside, the previous timing information
                // measured time-to-output rather than time-calculating, so the utility of the omitted
                // information was a YMMV situation to begin with.
                Instant end = Instant.now();
                System.out.println("Total file line count: " + lines);
                Duration timeElapsedLineCount = Duration.between(start, end);
                // In addition to the milliseconds, the elapsed time is printed in ISO-8601 duration format.
                // Once you are familiar with the format, it's much easier to compare times if 121449ms is also
                // printed as PT2M1.449S (2 minutes, 1.449 seconds)
                System.out.println("Time: " + timeElapsedLineCount.toMillis() + "ms (" + timeElapsedLineCount + ")");

                donationsPerYearMonth.forEach((key, value) ->
                        System.out.println("Donations per month and year: " + key + " and donation count: " + value)
                );

                // ArrayList vs LinkedList is really context dependent. LinkedList is much better for
                // building up data by prepending, but you can’t beat ArrayList for random access. One
                // of the consequences of this is that, if you’re doing an in-place sort, ArrayList is
                // generally a better structure to use.
                List<Map.Entry<String, Integer>> list = new ArrayList<>(firstNameOccurrences.entrySet());
                list.sort(Comparator.comparing(o -> -o.getValue()));
                Map.Entry<String, Integer> head = list.get(0);
                System.out.println("The most common first name is: " + head.getKey() + " and it occurs: " + head.getValue() + " times.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
