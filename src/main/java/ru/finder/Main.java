package ru.finder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        ArgsName argsName = ArgsName.of(args);
        valid(argsName);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(argsName.get("o")))) {
            searchFile(argsName).stream().map(Path::toString).forEach(s -> {
                try {
                    writer.write(s);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Path> searchFile(ArgsName argsName) throws IOException {
        Path root = Path.of(argsName.get("d"));
        Predicate<Path> condition;
        String typeSearch = argsName.get("t");
        switch (typeSearch) {

            case "mask" :
                String mask = argsName.get("n");
                String maskRegex = mask.replace("?", "\\?").replace("*", ".*?");
                condition = p -> p.getFileName().toString().matches(maskRegex);
                break;

            case "name" :
                String filename = argsName.get("n");
                condition = p -> p.getFileName().toString().equals(filename);
                break;

            case "regex" :
                String regex = argsName.get("n");
                condition = p -> p.getFileName().toString().matches(regex);
                break;

            default:
                throw new IllegalArgumentException("Wrong search type ");
        }
        Searcher searcher = new Searcher(condition);
        Files.walkFileTree(root, searcher);
        return searcher.getPaths();
    }

    private static void valid(ArgsName argsName) {
        String directory = argsName.get("d");
        String fileName = argsName.get("n");
        String typeSearch = argsName.get("t");
        if (!Files.isDirectory(Path.of(directory))) {
            throw new IllegalArgumentException(String.format("Error: this argument %s is not directory", directory));
        }
        if (Objects.equals(typeSearch, "name") && !fileName.matches("^[^*?\\[\\]()|]+\\.[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException(String.format("Error: this argument %s is wrong name file", fileName));
        }

    }
}
