package ru.megantcs.enhancer.platform.toolkit;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FolderWatcher {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean isWatching = false;
    private WatchService watchService;

    private final String directoryPath;
    private final Runnable runnable;

    public FolderWatcher(String directoryPath, Runnable runnable) {
        this.directoryPath = directoryPath;
        this.runnable = runnable;
    }


    public void watchToFolder() {
        Path path = Paths.get(directoryPath);

        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return;
        }

        executor.submit(() -> {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                isWatching = true;
                while (isWatching) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        runnable.run();
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }

            } catch (IOException ignored) {
            } finally {
                stopWatching();
            }
        });
    }

    public void stopWatching() {
        isWatching = false;
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignored) {}
        }
    }
}