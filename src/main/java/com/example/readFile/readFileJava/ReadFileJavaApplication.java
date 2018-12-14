package com.example.readFile.readFileJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class ReadFileJavaApplication {

	public static void main(String[] args) {
		try {

//			File f = new File("src/main/resources/config/test.txt");
			File f = new File("src/main/resources/config/itcont2.txt");

			try (BufferedReader b = new BufferedReader(new FileReader(f))) {

				String readLine = "";

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

				System.out.println("Reading file using Buffered Reader");

				while ((readLine = b.readLine()) != null) {
					lines++;
//					System.out.println(readLine);

					// get all the names
					String array1[] = readLine.split("\\s*\\|\\s*");
					String name = array1[7];
					names.add(name);
					if(indexes.contains(lines)){
						System.out.println("Name: " + names.get(lines - 1) + " at index: " + (lines - 1));
					}

				}
				Instant namesEnd = Instant.now();
				long timeElapsedNames = Duration.between(namesStart, namesEnd).toMillis();
				System.out.println("Name time: " + timeElapsedNames + "ms");

				System.out.println("Total file line count: " + lines);
				Instant lineCountEnd = Instant.now();
				long timeElapsedLineCount = Duration.between(lineCountStart, lineCountEnd).toMillis();
				System.out.println("Line count time: " + timeElapsedLineCount + "ms");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

