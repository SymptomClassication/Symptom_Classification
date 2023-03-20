# Homeopathy Symptom Classification:

The repository for Homeopathy symptom classification. The classification is used to match a given symptom to one or more body part (chapter) and a specific part of that chapter (subchapter). Given the initial chapters list, subchapter list, and training data files of sample symptoms, the software classifies a symptom given by the user either literally or using an NLP machine learning model, along with the ability to store, edit, and fetch chapters, subchapters, and classified symptoms.

## The following repository consists of two parts:

- GUI in Java for frontend
- Business logic in Python for classificaiton

## The Business logic side consists of three folders:

1. **populate_database**: used for the initial population of the database with the chapters provided by word/pdf documents.
2. **classifyInput**: used to classify given symptoms.
3. **classify_tests**: used to test the keyword classification method on a given set of symptoms

## The classifyInput folder consists of two ways of classification:

1. **keywordClassify.py**: used to classify symptoms using RegEx.
2. **MachineLearningClassification**: a folder consisting of two folders, one for FlairNLP library classification and one for SpaCy library classification. 


    