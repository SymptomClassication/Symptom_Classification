package com.symptom.model;

public class ClassifiedSubchapter {
    private int label;
    private String name;
    private int chapterId;

    public ClassifiedSubchapter(String name,int chapterId)
    {
        this.name = name;
        this.chapterId=chapterId;
    }
    public ClassifiedSubchapter(int label, String name,int chapterId)
    {
        this.label = label;
        this.name = name;
        this.chapterId=chapterId;
    }

    public ClassifiedSubchapter(String name) {
        this.name = name;
    }
    public int getLabel()
    {
        return label;
    }
    public String getName()
    {
        return name;
    }
    public String jsonFormat()
    {
        return "\"label\":" + Integer.toString(label);
    }
    public int getChapterId(){return chapterId;}
    public void setLabel(int label)
    {
        this.label = label;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
