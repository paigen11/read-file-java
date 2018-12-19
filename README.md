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
 
You can set up the Spring Boot runner in Intellij, by simply specifying which main class file you want to run.
[Intellij Configuration](/src/main/resources/img/classRunnerExample.png) 

Then you'll see the answers required from the file printed out to the terminal.

### To Check Performance Testing
Performance testing is already implemented for all three files as well using `Instant.now()`. You can see the performance for the three methods by passing files of different sizes and seeing how long it takes to process them.