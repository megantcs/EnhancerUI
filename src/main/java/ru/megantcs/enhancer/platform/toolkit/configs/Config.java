package ru.megantcs.enhancer.platform.toolkit.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config
{
    public static Config INSTANCE = new Config("platform");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String folder = MinecraftClient.getInstance().runDirectory + "\\config\\";

    public Config(String targetFolder) {
        folder += targetFolder;
        File configDir = new File(folder);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public <T extends ConfigItem> void saveArrayToFile(List<T> items, String fileName) {
        try {
            String fullPath = folder + "\\" + fileName + ".json";
            Path path = Paths.get(fullPath);

            String json = gson.toJson(items);

            Files.writeString(path, json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends ConfigItem> List<T> loadArrayFromFile(String fileName, Class<T> itemClass) {
        try {
            String fullPath = folder + "\\" + fileName + ".json";
            Path path = Paths.get(fullPath);

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
                            e.printStackTrace();
                        }
                    }
                }

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public <T extends ConfigItem> void appendToFile(T item, String fileName, Class<T> itemClass) {
        try {
            String fullPath = folder + "\\" + fileName + ".json";
            Path path = Paths.get(fullPath);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(ConfigItem item, String fileName)
    {
        try {
            String fullPath = folder + "\\" + fileName + ".json";
            Path path = Paths.get(fullPath);

            String json = item.serialize();
            Files.writeString(path, json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(ConfigItem item, String fileName)
    {
        try {
            String fullPath = folder + "\\" + fileName + ".json";
            Path path = Paths.get(fullPath);

            if (Files.exists(path)) {
                String json = Files.readString(path);
                item.deserialize(json);
            } else {
                saveToFile(item, fileName);
                String json = Files.readString(path);
                item.deserialize(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName) {
        String fullPath = folder + "\\" + fileName + ".json";
        File file = new File(fullPath);
        return file.delete();
    }

    public boolean fileExists(String fileName) {
        String fullPath = folder + "\\" + fileName + ".json";
        return new File(fullPath).exists();
    }

    public String getFolderPath() {
        return folder;
    }

    public void setFolder(String newFolder) {
        this.folder = newFolder;
        File configDir = new File(folder);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }
}