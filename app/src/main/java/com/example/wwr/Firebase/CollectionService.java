package com.example.wwr.Firebase;

import com.example.wwr.Route;
import com.example.wwr.chat.ChatServiceFactory;

public class CollectionService {
    ChatServiceFactory csf;
    Route route;

    public CollectionService(ChatServiceFactory csf) {
        this.csf = csf;
    }

    public CollectionService(Route route) {
        this.route = route;
    }

    public Route getRoutes() {
        return this.route;
    }
    public boolean getInstance() {
        return true;
    }
}
