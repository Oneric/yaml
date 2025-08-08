package ru.oneric.lab;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DatastoreConfig {
    private String name;
    private List<Database> databaseList;
}
