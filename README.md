# Java Large File / Data Reading & Performance Testing

This is an example of 3 different ways to use Java to process big data files. One file is the Java's `bufferedReader()` method, the second is with Java's `fileInputString()` method, and the third way is with the help of the Apache Commons IO package, `lineIterator()`.

There is also use of Java's date time methods `Instant.now()` and `Duration.between()` to determine the performance of the 3 different implementations, and which is most efficient processing the files.

### To Download the Really Large File
Download the large file zip here: https://www.fec.gov/files/bulk-downloads/2018/indiv18.zip

The main file in the zip: `itcont.txt`. I used the smaller file included in the config folder `test.txt` to test before running my solutions against the larger scale files.

### To Run
I used Intellij's handy Spring Boot configurations to create main class runners for each of the three main files I included inside the repo for the purposes of this example.

Add the file path for one of the files (could be the big one `itcont.txt` or any of its smaller siblings in the `indiv18` folder that were just downloaded - you can see how I set up my relative and hard coded file paths in each of the files), and then run whichever main class file you want by right clicking the file and selecting 'Run'.
[Intellij Run](/src/main/resources/img/intellijRun.png)

Either run `./gradlew assemble` and then 
* `java -cp ./build/libs/readFileJava-0.0.1-SNAPSHOT.jar com.example.readFile.readFileJava.ReadFileJavaApplicationBufferedReader`,
* `java -cp ./build/libs/readFileJava-0.0.1-SNAPSHOT.jar com.example.readFile.readFileJava.ReadFileJavaApplicationFileInputStream`, 
* `java -cp ./build/libs/readFileJava-0.0.1-SNAPSHOT.jar com.example.readFile.readFileJava.ReadFileJavaApplicationLineIterator`
for whichever method you want to run, from the command line.

Or the easier way:
 
You can set up the Spring Boot runner in Intellij, by simply specifying which main class file you want to run.
[Intellij Configuration](/src/main/resources/img/classRunnerExample.png) 

Then you'll see the answers required from the file printed out to the terminal.

### To Check Performance Testing
Performance testing is already implemented for all three files as well using `Instant.now()`. You can see the performance for the three methods by passing files of different sizes and seeing how long it takes to process them.

### Gotchas
The downloadable file seems to be live data that keeps getting larger. When I downloaded the file in the beginning of Oct 2018, it was 2.55GB. Now, some users have reported it being 3.5GB large, so please be aware some of your results and numbers will differ from mine for that reason.