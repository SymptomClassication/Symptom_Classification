package com.symptom.model;

public class ClassifiedSymptom {
    private String chapter;
    private String subchapter;
    private String symptom;

    public ClassifiedSymptom(String invalid)
    {
        this.chapter=invalid;
        this.subchapter=invalid;
        this.symptom=invalid;
    }
    public ClassifiedSymptom(String chapter, String subchapter)
    {
        this.chapter = chapter;
        this.subchapter = subchapter;
    }
    public ClassifiedSymptom(String chapter, String subchapter, String symptom)
    {
        this.chapter = chapter;
        this.subchapter = subchapter;
        this.symptom = symptom;
    }

    public String getChapter()
    {
        return chapter;
    }
    public String getSubchapter()
    {
        return subchapter;
    }
    public String getSymptom(){return symptom;}
}
