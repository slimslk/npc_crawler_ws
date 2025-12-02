package net.dimmid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainAppContext {
    private static final Map<String, Object> mainAppContext = new HashMap<>();

    public static void addObject(String key, Object value) {
        mainAppContext.put(key, value);
    }

    public static Optional<Object> getObject(String key) {
        return Optional.ofNullable(mainAppContext.get(key));
    }
}
