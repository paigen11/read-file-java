package com.example.readFile.readFileJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import static jdk.nashorn.internal.objects.Global.undefined;

public class ReadFileJavaApplicationFileInputStream {

	public static void main(String[] args) throws IOException {
			File f = new File("src/main/resources/config/test.txt");
			// File f = new File("src/main/resources/config/itcont2.txt");
			// File f = new File("/Users/pxn5096/Downloads/indiv18/itcont.txt");

			try {
				FileInputStream inputStream = new FileInputStream(f);
				Scanner sc = new Scanner(inputStream, "UTF-8");

				// get total line count
				Instant lineCountStart = Instant.now();
				int lines = 0;

				Instant namesStart = Instant.now();
				ArrayList<String> names = new ArrayList<>();

				// get the 432nd and 43243 names
				ArrayList<Integer> indexes = new ArrayList<>();

				indexes.add(1);
				indexes.add(433);
				indexes.add(43244);

				// count the number of donations by month
				Instant donationsStart = Instant.now();
				ArrayList<String> dates = new ArrayList<>();

				// count the occurrences of first name
				Instant commonNameStart = Instant.now();
				ArrayList<String> firstNames = new ArrayList<>();


				System.out.println("Reading file using File Input Stream");

				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					lines++;
//					System.out.println(line);

					// get all the names
					String array1[] = line.split("\\s*\\|\\s*");
					String name = array1[7];
					names.add(name);
					if (indexes.contains(lines)) {
						System.out.println("Name: " + names.get(lines - 1) + " at index: " + (lines - 1));
					}

					if (name.contains(", ")) {

						String array2[] = (name.split(", "));
						String firstHalfOfName = array2[1].trim();

						if (firstHalfOfName != undefined || !firstHalfOfName.isEmpty()) {
							if (firstHalfOfName.contains(" ")) {
								String array3[] = firstHalfOfName.split(" ");
								String firstName = array3[0].trim();
								firstNames.add(firstName);
							} else {
								firstNames.add(firstHalfOfName);
							}
						}
					}

					String rawDate = array1[4];
					String month = rawDate.substring(4, 6);
					String year = rawDate.substring(0, 4);
					String formattedDate = month + "-" + year;
					dates.add(formattedDate);

				}

				Instant namesEnd = Instant.now();
				long timeElapsedNames = Duration.between(namesStart, namesEnd).toMillis();
				System.out.println("Name time: " + timeElapsedNames + "ms");

				System.out.println("Total file line count: " + lines);
				Instant lineCountEnd = Instant.now();
				long timeElapsedLineCount = Duration.between(lineCountStart, lineCountEnd).toMillis();
				System.out.println("Line count time: " + timeElapsedLineCount + "ms");

				HashMap<String, Integer> dateMap = new HashMap<>();
				for (String date : dates) {
					Integer count = dateMap.get(date);
					if (count == null) {
						dateMap.put(date, 1);
					} else {
						dateMap.put(date, count + 1);
					}
				}
				for (Map.Entry<String, Integer> entry : dateMap.entrySet()) {
					String key = entry.getKey();
					Integer value = entry.getValue();
					System.out.println("Donations per month and year: " + entry.getKey() + " and donation count: " + entry.getValue());

				}
				Instant donationsEnd = Instant.now();
				long timeElapsedDonations = Duration.between(donationsStart, donationsEnd).toMillis();
				System.out.println("Donations time: " + timeElapsedDonations + "ms");

				HashMap<String, Integer> map = new HashMap<>();
				for (String name : firstNames) {
					Integer count = map.get(name);
					if (count == null) {
						map.put(name, 1);
					} else {
						map.put(name, count + 1);
					}
				}

				LinkedList<Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

				Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> o1,
					                   Map.Entry<String, Integer> o2) {
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
				System.out.println("The most common first name is: " + list.get(0).getKey() + " and it occurs: " + list.get(0).getValue() + " times.");
				Instant commonNameEnd = Instant.now();
				long timeElapsedCommonName = Duration.between(commonNameStart, commonNameEnd).toMillis();
				System.out.println("Most common name time: " + timeElapsedCommonName + "ms");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	}
}


