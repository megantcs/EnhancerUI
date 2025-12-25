package ru.megantcs.enhancer.platform.toolkit.configs.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@API(status = API.Status.MAINTAINED)
public interface Config
{
    @Nullable <T extends SerializeConfig> List<T> loadArray(Class<T> itemClass,
                                                            ExceptionContainer exceptionContainer) throws IOException;

    <T extends SerializeConfig> void saveArray(List<T> items) throws IOException;

    <T extends SerializeConfig> void append(T item,
                                            Class<T> itemClass) throws IOException;

    void save(SerializeConfig item) throws IOException;
    void load(SerializeConfig item) throws IOException;

    boolean delete();
    boolean exists();

    class BaseConfigImpl implements Config
    {
        private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        private final String fileName;
        private final String directory;
        private final Path path;

        protected BaseConfigImpl(String fileName, String directory) {
            Objects.requireNonNull(fileName);
            Objects.requireNonNull(directory);

            this.fileName = fileName;
            this.directory = directory;
            this.path = Paths.get(directory, fileName);
            try {
                ensureDirectoryExists();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private void ensureDirectoryExists() throws IOException {
            Path dirPath = Paths.get(directory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        }

        @Override
        public <T extends SerializeConfig> void saveArray(List<T> items) throws IOException {
            ensureDirectoryExists();
            String json = gson.toJson(items);
            Files.writeString(path, json);
        }

        @Override
        public @Nullable <T extends SerializeConfig> List<T> loadArray(Class<T> itemClass, ExceptionContainer exceptionContainer) throws IOException {
            ensureDirectoryExists();

            if (Files.exists(path)) {
                String json = Files.readString(path);
                List<T> result = new ArrayList<>();

                JsonArray jsonArray = gson.fromJson(json, JsonArray.class);

                if (jsonArray != null) {
                    for (JsonElement element : jsonArray) {
                        try {
                            T item = itemClass.getDeclaredConstructor().newInstance();

                            String itemJson = gson.toJson(element);
                            item.deserialize(itemJson);

                            result.add(item);
                        } catch (Exception e) {
                            exceptionContainer.add(Config.BaseConfigImpl.class, e);
                        }
                    }
                }

                return result;
            }

            return null;
        }

        @Override
        public <T extends SerializeConfig> void append(T item, Class<T> itemClass) throws IOException {
            List<T> items = new ArrayList<>();

            if (Files.exists(path)) {
                String existingJson = Files.readString(path);
                JsonArray jsonArray = gson.fromJson(existingJson, JsonArray.class);

                if (jsonArray != null) {
                    for (JsonElement element : jsonArray) {
                        T existingItem = gson.fromJson(element, itemClass);
                        items.add(existingItem);
                    }
                }
            }

            items.add(item);

            String json = gson.toJson(items);
            Files.writeString(path, json);
        }

        @Override
        public void save(SerializeConfig item) throws IOException {
            String json = item.serialize();
            Files.writeString(path, json);
        }

        @Override
        public void load(SerializeConfig item) throws IOException {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                item.deserialize(json);
            } else {
                save(item);
                String json = Files.readString(path);
                item.deserialize(json);
            }
        }

        @Override
        public boolean delete() {
            return path.toFile().delete();
        }

        @Override
        public boolean exists() {
            return path.toFile().exists();
        }

        protected Path getPath() {
            return path;
        }

        protected String getDirectory() {
            return directory;
        }

        protected String getFileName() {
            return fileName;
        }
    }
}
