package storage;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStorage {
    private static final Map<String, Node> storage = new HashMap<>();

    public static void clear() {
        storage.clear();
    }

    public static String put(String key, String value, Long expiration) {
        long currentTimeMillis = System.currentTimeMillis();
        Node node = new Node(value, expiration != null ? currentTimeMillis + expiration : null);
        Node previousValue = storage.put(key, node);
        if (previousValue != null && (previousValue.exp == null || previousValue.exp > currentTimeMillis)) {
            return previousValue.value;
        }
        return null;
    }

    public static String get(String key) {
        long currentTimeMillis = System.currentTimeMillis();
        Node node = storage.get(key);
        if (node != null && (node.exp == null || node.exp > currentTimeMillis)) {
            return node.value;
        } else return null;
    }

    private static class Node {
        private String value;
        private Long exp;

        public Node(String value, Long exp) {
            this.value = value;
            this.exp = exp;
        }
    }
}
