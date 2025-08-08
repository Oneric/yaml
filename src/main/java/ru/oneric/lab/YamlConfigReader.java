package ru.oneric.lab;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigReader {
    private Map<String, Object> config;

    public YamlConfigReader(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(filePath)) {

            if (inputStream == null) {
                throw new RuntimeException("File not found: " + filePath);
            }

            this.config = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML file", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String path, Class<T> type) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;

        for (int i = 0; i < keys.length - 1; i++) {
            Object value = current.get(keys[i]);
            if (value instanceof Map) {
                current = (Map<String, Object>) value;
            } else {
                return null;
            }
        }

        Object value = current.get(keys[keys.length - 1]);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Map<String, Object> map, String path, Class<T> type) {
        String[] keys = path.split("\\.");

        for (int i = 0; i < keys.length - 1; i++) {
            Object value = map.get(keys[i]);
            if (value instanceof Map) {
                map = (Map<String, Object>) value;
            } else {
                return null;
            }
        }

        Object value = map.get(keys[keys.length - 1]);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    public Map<String, Object> getConfigMap(String path) {
        if (getProperty(path, Map.class) != null) {
            return getProperty(path, Map.class);
        }
        return null;
    }

    public String getPropertyAsString(String path) {
        if (getProperty(path, String.class) != null) {
            return getProperty(path, String.class);
        }
        return null;
    }

    public String getPropertyAsString(String path, String defaultValue) {
        if (getProperty(path, String.class) != null) {
            return getProperty(path, String.class);
        }
        return defaultValue;
    }

    public Integer getPropertyAsInt(String path) {
        if (getProperty(path, Integer.class) != null) {
            return getProperty(path, Integer.class);
        }
        return null;
    }

    public Integer getPropertyAsInt(String path, String defaultValue) {
        if (getProperty(path, Integer.class) != null) {
            return getProperty(path, Integer.class);
        }
        return Integer.parseInt(defaultValue);
    }

    public Boolean getPropertyAsBoolean(String path) {
        if (getProperty(path, Boolean.class) != null) {
            return getProperty(path, Boolean.class);
        }
        return null;
    }

    public Boolean getPropertyAsBoolean(String path, String defaultValue) {
        if (getProperty(path, Boolean.class) != null) {
            return getProperty(path, Boolean.class);
        }
        return Boolean.parseBoolean(defaultValue);
    }

    public Long getPropertyAsLong(String path) {
        if (getPropertyAsInt(path) != null) {
            return Long.valueOf(getPropertyAsInt(path));
        }
        return null;
    }

    public Long getPropertyAsLong(String path, String defaultValue) {
        if (getPropertyAsInt(path) != null) {
            return Long.valueOf(getPropertyAsInt(path));
        }
        return Long.parseLong(defaultValue);
    }
}
