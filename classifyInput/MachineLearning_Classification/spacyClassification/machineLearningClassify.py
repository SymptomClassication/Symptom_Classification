from joblib import dump, load
import spacy


def tokenize(text, embeddings=spacy.load("en_core_web_sm")):
    return [token.text for token in embeddings(text)]


def pipelines():

    model = load('model.joblib')
    print(model.predict(["I have dry eyes"]))



pipelines()
