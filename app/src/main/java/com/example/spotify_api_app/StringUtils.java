package com.example.spotify_api_app;

import java.util.List;

public class StringUtils {
    public static String joinStrings(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size() - 1; i++) {
            sb.append(strings.get(i)).append(", ");
        }
        sb.append(strings.get(strings.size() - 1));

        return sb.toString();
    }

    public static String joinAndCapitalizeFirstLetters(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size() - 1; i++) {
            sb.append(capitalizeFirstLetter(strings.get(i))).append(", ");
        }
        sb.append(capitalizeFirstLetter(strings.get(strings.size() - 1)));

        return sb.toString();
    }

    private static String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
