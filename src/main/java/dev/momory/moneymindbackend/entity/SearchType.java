package dev.momory.moneymindbackend.entity;

public enum SearchType {
    ALL("all"),
    NAME("name");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
