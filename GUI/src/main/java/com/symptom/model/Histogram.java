package com.symptom.model;

public class Histogram {
    int id;
    String subchapterName;
    String symptom;
    String chapterName;
    public Histogram(int id, String subchapterName, String symptom, String chapterName)
    {
        this.id = id;
        this.subchapterName = subchapterName;
        this.symptom = symptom;
        this.chapterName = chapterName;
    }
    public int getId()
    {
        return id;
    }
    public String getSubchapterName()
    {
        return subchapterName;
    }
    public String getSymptom()
    {
        return symptom;
    }
    public String getChapterName()
    {
        return chapterName;
    }
}
