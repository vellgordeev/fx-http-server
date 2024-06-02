package ru.flamexander.http.server.application;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Item {
    private UUID id;
    private String title;
    private int price;

    public Item(String title, int price) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.price = price;
    }
}
