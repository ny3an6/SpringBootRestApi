package com.ndmitrenko.dinospringbootapp.util;

public class ToUpperCaseString {
    public static String toUpperCase(String text) { // capitalize the first letter of the word
        StringBuilder builder = new StringBuilder(text);
        builder.setCharAt(0, Character.toUpperCase(text.charAt(0)));
        return builder.toString();
    }
}
