package ru.oneric.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        YamlConfigReader configReader = new YamlConfigReader("dev.yml");

        Map<String, Object> auth = configReader.getConfigMap("auth");
        Map<String, Object> database = configReader.getConfigMap("database");
        Map<String, Object> postgresql = configReader.getConfigMap("database.datastore_1.postgresql");

        System.out.println(YamlConfigReader.getProperty(postgresql, "host", String.class));


        // Database LISTING
        List<DatastoreConfig> datastoreConfigList = new ArrayList<>();

        for (Map.Entry<String, Object> datastoreEntry : database.entrySet()) {
            String datastoreName = datastoreEntry.getKey();
            //System.out.println("Datastore: " + datastoreName);
            // Добавляем проверку типа для безопасности
            if (!(datastoreEntry.getValue() instanceof Map)) {
                System.out.println("  Invalid datastore format");
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> drivers = (Map<String, Object>) datastoreEntry.getValue();

            List<Database> databaseList = new ArrayList<>();

            for (Map.Entry<String, Object> driverEntry : drivers.entrySet()) {
                String driverName = driverEntry.getKey();
                //System.out.println("  Driver: " + driverName);

                if (!(driverEntry.getValue() instanceof Map)) {
                    System.out.println("    Invalid driver format");
                    continue;
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> driverConfig = (Map<String, Object>) driverEntry.getValue();

                // Выводим конфигурацию в отформатированном виде
                Object host = driverConfig.getOrDefault("host", "N/A");
                //System.out.println("    Host: " + host);
                Object port = driverConfig.getOrDefault("port", "N/A");
                //System.out.println("    Port: " + port);
                Object databaseName = driverConfig.getOrDefault("database", "N/A");
                //System.out.println("    Database: " + databaseName);
                Object username = driverConfig.getOrDefault("username", "N/A");
                //System.out.println("    Username: " + username);

                // Пароль обычно не показывают в логах, можно заменить на звездочки
                Object password = driverConfig.get("password");
                String maskedPassword = password != null ? "*****" : "N/A";
                //System.out.println("    Password: " + maskedPassword);
                databaseList.add(new Database(
                        driverName.toString(),
                        host.toString(),
                        port.toString(),
                        driverName.toString(),
                        username.toString(),
                        password.toString())
                );
            }
            datastoreConfigList.add(new DatastoreConfig(datastoreName, databaseList));
        }

        for (Map.Entry<String, Object> entry : auth.entrySet()) {
            String role = entry.getKey();
            System.out.println("Role: " + role);

            // Безопасное приведение типа и проверка на null
            if (entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> credentials = (Map<String, Object>) entry.getValue();

                // Безопасное извлечение и вывод учетных данных
                String username = credentials.containsKey("username") ?
                        credentials.get("username").toString() : "N/A";
                String password = credentials.containsKey("password") ?
                        credentials.get("password").toString() : "N/A";

                // Маскируем пароль, оставляя первые 2 символа (если длина > 2)
                String maskedPassword = password.length() > 2
                        ? password.substring(0, 2) + "***"
                        : "***";

                System.out.println("  Username: " + username);
                System.out.println("  Password: " + maskedPassword);
            } else {
                System.out.println("  Invalid credentials format for role: " + role);
            }
        }
    }
}