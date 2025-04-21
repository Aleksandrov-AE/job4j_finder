package ru.jo4j_finder;

import java.util.HashMap;
import java.util.Map;

public class ArgsName {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: 'Xms' is missing");
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        for (String value: args) {
            String[] pars = value.substring(1).split("=", 2);
            values.put(pars[0], pars[1]);
        }
    }

    public static ArgsName of(String[] args) {
        checkArgs(args);
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }
    private static void checkArgs(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        for (String s: args) {
            if (!s.startsWith("-")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not start with a '-' character", s));
            }
            if (!s.contains("=")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain an equal sign", s));
            }
            String key = s.substring(1, s.indexOf("="));
            String value = s.substring(s.indexOf("=") + 1);
            if (key.isEmpty()) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a key", s));
            }
            if (value.isEmpty()) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a value", s));
            }
        }
    }

    public static void main(String[] args) {
        ArgsName jvm = ArgsName.of(new String[] {"-Xmx=512", "-encoding=UTF-8"});
        System.out.println(jvm.get("Xmx"));

        ArgsName zip = ArgsName.of(new String[] {"-out=project.zip", "-encoding="});
        System.out.println(zip.get("out"));
    }
}