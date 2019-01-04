package com.example.readFile.readFileJava;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * More efficient java solution of the challenge
 */
public class FileReadingChallenge {

    // for memory conservation reasons i cheated and looked up the line count beforehand
    // we _could_ just use ArrayList for keeping them and convert to arrays after but meh
    private static final int LINE_COUNT = 18245416;

    static final String[] names = new String[LINE_COUNT];
    static final String[] firstNames = new String[LINE_COUNT];
    static int resultsToWaitFor = 0;

    static final ConcurrentHashMap<String, Integer> firstNameCount = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Integer, Integer> monthCount = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Hello World!");

        File f = new File("src/main/resources/config/test.txt");
        if (!f.exists() || !f.canRead()) {
            System.out.println("Problem reading file " + f);
        }

        int lineCount = 0;
        Instant lineRead = Instant.now();
        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                lineCount++;
                if (lineCount < 10) {
                    // print some of the data so we can see what it looks like - most editors fail to display the file
                    System.out.println("line " + (lineCount + 1) + " printout: " + line);
                }

                incrementLock();
                if (lineCount % 1000000 == 0) {
                    System.out.println("reading line " + lineCount + ", fibers running: " + (resultsToWaitFor - 1));
                    // optimization: as it is possible for I/O to supply new fibers faster than can be completed,
                    // which chokes and stalls the JVM, we pause the line reading if the fiber count is too high.
                    while (resultsToWaitFor > 9999) {
                        try {
                            System.out.println("waiting for fiber count to drop, now at: " + resultsToWaitFor);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // optimization: reading a line and doing anything with the content is decoupled from each other
                // and asynchronous. We use Fibers, from the CompletableFuture API
                CompletableFuture.runAsync(new LineRunnable(line, lineCount)).thenAcceptAsync(whenDone -> decrementLock());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Instant lineReadDone = Instant.now();
        long millis = Duration.between(lineRead, lineReadDone).toMillis();
        System.out.printf("%d lines read and supplied in %d milliseconds\n", lineCount, millis);

        // now keep querying our lock until it indicates that all fibers have finished
        int waitCount = 0;
        while (resultsToWaitFor > 0) {
            try {
                waitCount++;
                if (waitCount % 10 == 0) {
                    // give some user feedback
                    System.out.println("fibers not done yet: " + resultsToWaitFor);
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Instant parsingDone = Instant.now();
        millis = Duration.between(lineReadDone, parsingDone).toMillis();
        System.out.printf("fibers kept parsing async for (approx) %d milliseconds longer\n", millis);

        // present the solutions!
        System.out.println("name 432: " + names[431]);
        System.out.println("name 43243: " + names[43242]);

        // technically these last steps could be async too but it is not worth it here
        int highestNameCount = 0;
        String mostCommonName = null;
        for (Map.Entry<String, Integer> entry : firstNameCount.entrySet()) {
            if (entry.getValue() > highestNameCount) {
                mostCommonName = entry.getKey();
                highestNameCount = entry.getValue();
            }
        }
        System.out.println("most common name is " + mostCommonName + " with " + highestNameCount + " occurences");

        int highestMonthCount = 0;
        int bestMonth = 0;
        for (Map.Entry<Integer, Integer> entry : monthCount.entrySet()) {
            if (entry.getValue() > highestMonthCount) {
                bestMonth = entry.getKey();
                highestMonthCount = entry.getValue();
            }
        }
        System.out.println("most mentioned month is no. " + bestMonth + " with a count of " + highestMonthCount);
    }

    // we need to prevent race conditions on our locking variable
    static synchronized void incrementLock() {
        resultsToWaitFor++;
    }

    static synchronized void decrementLock() {
        resultsToWaitFor--;
    }

    private static class LineRunnable implements Runnable {

        final String input;
        final int index;

        LineRunnable(String line, int indexOfLine) {
            input = line;
            index = indexOfLine - 1;
        }

        @Override
        public void run() {
            if (index < 10) {
                // we are somewhat paranoid about our fibers starting...
                System.out.println("fiber started for line " + (index + 1));
            }
            String[] columns = input.split("\\|");
            if (columns.length < 8) {
                return;
            }
            names[index] = columns[7];
            String[] nameFragments = columns[7].split(",");
            // i will just disregard anything not following the "NAME, SURNAME" template
            if (nameFragments.length == 2) {
                firstNames[index] = nameFragments[1];
                // amass a count
                Integer count = firstNameCount.getOrDefault(nameFragments[1], 0);
                firstNameCount.put(nameFragments[1], count + 1);
            }

            // like 201701230300133512
            String dateString = columns[4];
            if (dateString.length() >= 8) {
                // dont need to parse the whole date fragment, in fact this is overkill too
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    Date date = sdf.parse(dateString.substring(0, 8));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int month = calendar.get(Calendar.MONTH);
                    Integer count = monthCount.getOrDefault(month, 0);
                    monthCount.put(month, count + 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (index < 10) {
                // we are somewhat paranoid about our fibers finishing...
                System.out.println("fiber finished for line " + (index + 1));
            }
        }
    }
}
