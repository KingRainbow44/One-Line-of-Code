package tech.xigam.onelineofcode.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public final class JsonUtil {
    public static String jsonSerialize(Object object) {
        return new Gson().toJson(object);
    }

    public static String jsonFileSerialize(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

    public static <T> T jsonDeserialize(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> T jsonFileDeserialize(File file, Class<T> clazz) {
        return JsonUtil.jsonDeserialize(FileUtil.readFile(file), clazz);
    }
}