from joblib import dump, load
import spacy
import scispacy
import sklearn
import json
import re
import numpy as np
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.metrics import label_ranking_average_precision_score


with open('trainingData/to_test_multidata.json', 'r') as f:
    symptoms = list(json.load(f))

with open('trainingData/to_test_multilabels.json', 'r') as f:
    correct_labels = list(json.load(f))

predicted_labels = []


def tokenize(text, embeddings=spacy.load("en_core_sci_md")):
    return [token.text for token in embeddings(text)]


def pipelines():

    model = load('model.joblib')
    for s in symptoms:
        removeNum = re.sub(r"[0-9]", "", s)
        removeDot = re.sub(r"\.", "", removeNum)
        removeStar = re.sub(r"\*", "", removeDot)
        removeBracket = re.sub(r"\[*\]*", "", removeStar)
        removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
        removeComma = re.sub(r",", "", removeParenthesis)
        removeSemiColon = re.sub(r";", "", removeComma)
        predicted_labels.append(model.predict([removeSemiColon])[0].tolist())  # convert numpy array to list

    mlb = MultiLabelBinarizer()
    binary_predicted_labels = mlb.fit_transform(predicted_labels)
    binary_correct_labels = mlb.transform(correct_labels)

    print(label_ranking_average_precision_score(binary_correct_labels, binary_predicted_labels))


pipelines()
