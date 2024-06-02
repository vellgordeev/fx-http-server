package ru.flamexander.http.server.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Storage {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class.getName());
    private static List<Item> items;

    public static void init() {
        logger.info("Хранилище проинициализировано");
        items = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int) (Math.random() * 1000)));
        }
    }

    private Storage() {
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
    }
}
