package com.example.readFile.readFileJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootApplication
public class ReadFileJavaApplication {

	public static void main(String[] args) throws IOException {
		String fileName = "config/test.txt";

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		File file = new File(classLoader.getResource(fileName).getFile());

		//File is found
		System.out.println("File Found : " + file.exists());

		//Read File Content
		String content = new String(Files.readAllBytes(file.toPath()));
		System.out.println(content);
	}

}

