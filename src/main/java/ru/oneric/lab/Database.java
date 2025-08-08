package ru.oneric.lab;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Database {
    private String driver;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
}
