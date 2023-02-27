from joblib import dump, load
import spacy
import scispacy
import sklearn
import json
import re
import sys


def tokenize(text, embeddings=spacy.load("en_core_sci_md")):
    return [token.text for token in embeddings(text)]


def pipelines(symptom):

    model = load('model.joblib')


    prediction = model.predict([symptom])
    classification = ""

    with open('machineLearningChapters.json', 'r') as f:
        chapters = list(json.load(f))

    for chapter in chapters:
        if chapter['id'] == prediction[0]:
            if chapter['id'] == chapter['chapterId']:
                classification = chapter['name']
            else:
                for bigger_chapter in chapters:
                    if chapter['chapterId'] == bigger_chapter['id']:
                        classification = bigger_chapter['name'] + " - " + chapter['name']

    return classification

if __name__ == "__main__":
    print(pipelines(" ".join(sys.argv[1:])))

