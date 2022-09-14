package com.sg.gate.minispringstep02.beans;

public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object values) {
        this.name = name;
        this.value = values;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
