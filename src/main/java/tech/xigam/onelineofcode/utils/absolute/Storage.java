package tech.xigam.onelineofcode.utils.absolute;

import java.util.HashMap;
import java.util.Map;

public final class Storage {
    private static final Map<String, Object> storage = new HashMap<>();

    public static void store(String key, Object value) {
        storage.put(key, value);
    }

    public static <T> T fetch(String key, Class<T> type) {
        return type.cast(storage.get(key));
    }
    
    public static void clear(String key) {
        storage.remove(key);
    }
}
