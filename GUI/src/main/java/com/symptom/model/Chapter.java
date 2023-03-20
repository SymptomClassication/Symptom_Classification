package com.symptom.model;
public class Chapter {
    private int id;
    private String name;

    public Chapter(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Chapter(String name) {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}