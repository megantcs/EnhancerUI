package ru.megantcs.enhancer.platform.toolkit.configs;

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
    @Nullable <T extends ConfigItem> List<T> loadArrayFromFile(Class<T> itemClass,
                                                               ExceptionContainer exceptionContainer) throws IOException;

    <T extends ConfigItem> void saveArrayToFile(List<T> items) throws IOException;

    <T extends ConfigItem> void appendToFile(T item,
                                             Class<T> itemClass) throws IOException;

    void saveToFile(ConfigItem item) throws IOException;
    void loadFromFile(ConfigItem item) throws IOException;

    boolean deleteFile();
    boolean fileExists();

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
        }


        @Override
        public <T extends ConfigItem> void saveArrayToFile(List<T> items) throws IOException {
            String json = gson.toJson(items);
            Files.writeString(path, json);
        }

        @Override
        public @Nullable <T extends ConfigItem> List<T> loadArrayFromFile(Class<T> itemClass, ExceptionContainer exceptionContainer) throws IOException {
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
        public <T extends ConfigItem> void appendToFile(T item, Class<T> itemClass) throws IOException {
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
        public void saveToFile(ConfigItem item) throws IOException {
            String json = item.serialize();
            Files.writeString(path, json);
        }

        @Override
        public void loadFromFile(ConfigItem item) throws IOException {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                item.deserialize(json);
            } else {
                saveToFile(item);
                String json = Files.readString(path);
                item.deserialize(json);
            }
        }

        @Override
        public boolean deleteFile() {
            return path.toFile().delete();
        }

        @Override
        public boolean fileExists() {
            String fullPath = directory + "\\" + fileName;
            return new File(fullPath).exists();
        }
    }
}
