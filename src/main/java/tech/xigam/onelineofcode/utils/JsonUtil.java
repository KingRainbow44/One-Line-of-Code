package tech.xigam.onelineofcode.utils;

import com.google.gson.Gson;

public final class JsonUtil {
    public static String jsonSerialize(Object object) {
        return new Gson().toJson(object);
    }
    
    public static <T> T jsonDeserialize(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}
