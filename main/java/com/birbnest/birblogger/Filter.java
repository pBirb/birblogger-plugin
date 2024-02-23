package com.birbnest.birblogger;

import java.util.List;

public class Filter {
    public static boolean containsFilteredWord(String message, List<String> filteredWords) {
        String[] words = message.split(" ");

        for (String word : words) {
            if (filteredWords.contains(word.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}