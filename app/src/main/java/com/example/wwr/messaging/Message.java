package com.example.wwr.messaging;

public class Message {
    private String from;
    private String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return from +
                ":\n" +
                text +
                "\n" +
                "---\n";
    }
}
