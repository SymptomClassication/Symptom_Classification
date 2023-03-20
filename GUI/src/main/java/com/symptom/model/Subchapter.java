package com.symptom.model;
public class Subchapter {
    private int id;
    private String name;
    private int chapterId;

    public Subchapter(String name,int chapterId)
    {
        this.name = name;
        this.chapterId=chapterId;
    }
    public Subchapter(int id, String name,int chapterId)
    {
        this.id = id;
        this.name = name;
        this.chapterId=chapterId;
    }

    public Subchapter(String name) {
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
    public int getChapterId(){return chapterId;}
    @Override
    public String toString()
    {
        return name;
    }
}
