package com.example.readFile.readFileJava;

class Common {
    static String getPathToTargetFile(String[] args) {
        if (args.length >= 1) {
            return args[0];
        }
        return "src/main/resources/config/test.txt";
    }

    private Common() {}
}
