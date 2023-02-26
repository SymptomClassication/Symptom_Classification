from joblib import dump, load
import spacy
import scispacy
import sklearn
import json
import re


with open('symptomsClassification/classifyInput/MachineLearning_Classification/spacyClassification/trainingData/to_test_data.json', 'r') as f:
    symptoms = list(json.load(f))

with open('symptomsClassification/classifyInput/MachineLearning_Classification/spacyClassification/trainingData/to_test_labels.json', 'r') as f:
    correct_labels = list(json.load(f))

predicted_labels = []

def tokenize(text, embeddings=spacy.load("en_core_sci_md")):
    return [token.text for token in embeddings(text)]


def pipelines():

    model = load('./model.joblib')
    for s in symptoms:
        removeNum = re.sub(r"[0-9]", "", s)
        removeDot = re.sub(r"\.", "", removeNum)
        removeStar = re.sub(r"\*", "", removeDot)
        removeBracket = re.sub(r"\[*\]*", "", removeStar)
        removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
        removeComma = re.sub(r",", "", removeParenthesis)
        removeSemiColon = re.sub(r";", "", removeComma)
        predicted_labels.append(model.predict([removeSemiColon]))

    print(sklearn.metrics.classification_report(correct_labels, predicted_labels, digits=3))


pipelines()
