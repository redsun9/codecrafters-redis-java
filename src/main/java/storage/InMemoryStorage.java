package storage;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStorage {
    private static final Map<String, String> storage = new HashMap<>();

    public static String put(String key, String value) {
        return storage.put(key, value);
    }

    public static String get(String key) {
        return storage.get(key);
    }
}
