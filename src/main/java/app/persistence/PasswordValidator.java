package app.persistence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Check the length
        if (password.length() <= 7) {
            return false;
        }
        // Check for uppercase letters
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        int uppercaseCount = 0;
        while (uppercaseMatcher.find()) {
            uppercaseCount++;
        }
        if (uppercaseCount < 1) {
            return false;
        }
        // Check for lowercase letters
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        if (!lowercaseMatcher.find()) {
            return false;
        }
        // Check for digits
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);
        int digitcount = 0;
        while (digitMatcher.find()){
            digitcount++;
        }
        if (digitcount < 1) {
            return false;
        }
        // Check for special characters
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]"); // Add more special characters as needed
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        int specialCharCount = 0;
        while (specialCharMatcher.find()){
            specialCharCount++;
        }
        if (specialCharCount < 1) {
            return false;
        }
        return true;
    }
}
