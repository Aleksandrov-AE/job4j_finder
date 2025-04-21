package ru.finder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    @Test
    void whenSearchByName(@TempDir Path tempDir) throws IOException {
        Path file = Files.createFile(tempDir.resolve("test.txt"));

        ArgsName args = ArgsName.of(new String[]{
                "-d=" + tempDir,
                "-t=name",
                "-n=test.txt",
                "-o=out.txt"
        });

        List<Path> result = Main.searchFile(args);
        assertThat(result).containsExactly(file);
    }

    @Test
    void whenSearchByMask(@TempDir Path tempDir) throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("test1.txt"));
        Path file2 = Files.createFile(tempDir.resolve("test2.txt"));
        Files.createFile(tempDir.resolve("image.png"));

        ArgsName args = ArgsName.of(new String[]{
                "-d=" + tempDir,
                "-t=mask",
                "-n=*.txt",
                "-o=out.txt"
        });

        List<Path> result = Main.searchFile(args);
        assertThat(result).containsExactlyInAnyOrder(file1, file2);
    }

    @Test
    void whenSearchByRegex(@TempDir Path tempDir) throws IOException {
        Path file1 = Files.createFile(tempDir.resolve("log1.md"));
        Path file2 = Files.createFile(tempDir.resolve("notes.md"));
        Files.createFile(tempDir.resolve("file.txt"));

        ArgsName args = ArgsName.of(new String[]{
                "-d=" + tempDir,
                "-t=regex",
                "-n=.*\\.md",
                "-o=out.txt"
        });

        List<Path> result = Main.searchFile(args);
        assertThat(result).containsExactlyInAnyOrder(file1, file2);
    }
}
