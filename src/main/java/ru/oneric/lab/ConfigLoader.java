package ru.oneric.lab;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class ConfigLoader {
    private final Map<String, Object> config = new HashMap<>();
    private final Map<String, String> envVars;

    public ConfigLoader() {
        this(System.getenv());
    }

    public ConfigLoader(Map<String, String> envVars) {
        this.envVars = envVars;
        loadDefaultConfig();
    }

    private void loadDefaultConfig() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("dev.yml")) {

            if (inputStream != null) {
                Map<String, Object> loadedConfig = yaml.load(inputStream);
                if (loadedConfig != null) {
                    config.putAll(flattenMap(loadedConfig));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML config", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> flattenMap(Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        flattenMap("", source, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void flattenMap(String prefix, Map<String, Object> source, Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();

            if (entry.getValue() instanceof Map) {
                flattenMap(key, (Map<String, Object>) entry.getValue(), result);
            } else if (entry.getValue() instanceof List) {
                result.put(key, entry.getValue());
            } else {
                result.put(key, entry.getValue());
            }
        }
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        // 1. Проверяем переменные окружения (в формате UPPER_CASE)
        String envKey = key.toUpperCase().replace('.', '_');
        if (envVars.containsKey(envKey)) {
            return envVars.get(envKey);
        }

        // 2. Проверяем конфиг файл
        Object value = config.get(key);
        if (value != null) {
            return value.toString();
        }

        // 3. Возвращаем дефолтное значение
        return defaultValue;
    }

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String strValue = getString(key);
        if (strValue != null) {
            try {
                return Integer.parseInt(strValue);
            } catch (NumberFormatException e) {
                throw new ConfigException("Invalid integer value for key: " + key);
            }
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String strValue = getString(key);
        if (strValue != null) {
            return Boolean.parseBoolean(strValue);
        }
        return defaultValue;
    }
}
