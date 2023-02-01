import spacy
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression
from sklearn.feature_extraction.text import TfidfVectorizer
import json
from joblib import dump, load

def tokenize(text, embeddings=spacy.load("en_core_web_sm")):
    return [token.text for token in embeddings(text)]

def train_model():

    # Load spaCy model and create a text processing pipeline
    vectorizer =  TfidfVectorizer(tokenizer=tokenize)
    classifier = LogisticRegression()

    # Define your training data
    train_data = []
    labels = []

    with open('trainingData/trainingData.json', 'r') as f:
        train_data = list(json.load(f))

    with open('trainingData/matchingTrainingData.json', 'r') as f:
        labels = list(json.load(f))

    # Fit the model and make predictions
    model = Pipeline([
        ('vectorizer', vectorizer),
        ('classifier', classifier)
    ])

    model.fit(train_data, labels)

    dump(model, 'model.joblib')

    model = load('model.joblib')

train_model()