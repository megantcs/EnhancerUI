package ru.megantcs.enhancer.platform.toolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IOHelper
{
    public static List<Path> getFolders(String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);

        return Files.list(dir)
                .filter(Files::isDirectory)
                .collect(Collectors.toList());
    }

    public static Set<String> getFoldersName(String directoryPath) throws IOException {
            Path dir = Paths.get(directoryPath);

            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                return new HashSet<>();
            }

            try (var stream = Files.list(dir)) {
                return stream
                        .filter(Files::isDirectory)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toSet());
            }
    }
}
