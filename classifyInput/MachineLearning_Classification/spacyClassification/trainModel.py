import spacy
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.multiclass import OneVsRestClassifier
import json
import re
import scispacy
from joblib import dump, load

def tokenize(text, embeddings=spacy.load("en_core_sci_md")):
    return [token.text for token in embeddings(text)]

def train_model():

    # Load spaCy model and create a text processing pipeline
    vectorizer = TfidfVectorizer(tokenizer=tokenize)
    classifier = LogisticRegression()
    multi_class_classifier = OneVsRestClassifier(classifier)

    # Define your training data
    train_data = []
    labels = []
    with open('./trainingData/to_train_data.json', 'r') as f:
        train_data = list(json.load(f))

    with open('./trainingData/to_train_labels.json', 'r') as f:
        labels = list(json.load(f))

    # Fit the model and make predictions
    model = Pipeline([
        ('vectorizer', vectorizer),
        ('classifier', multi_class_classifier)
    ])
    for i in range(len(train_data)):
        removeNum = re.sub(r"[0-9]", "", train_data[i])
        removeDot = re.sub(r"\.", "", removeNum)
        removeStar = re.sub(r"\*", "", removeDot)
        removeBracket = re.sub(r"\[*\]*", "", removeStar)
        removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
        removeComma = re.sub(r",", "", removeParenthesis)
        removeSemiColon = re.sub(r";", "", removeComma)
        train_data[i] = removeSemiColon

    model.fit(train_data, labels)

    dump(model, './model.joblib')


train_model()