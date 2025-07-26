package ru.shapovalov.common.logging.gradle;

public class LoggingStarterAutoConfiguration {

    public static void println(String text) {
        System.out.println("Выведено из gradle стартера: " + text);
    }
}
