import numpy as np
import flair
from flair.embeddings import WordEmbeddings
from flair.data import Sentence
from joblib import dump, load
import spacy


def tokenize(text, embeddings=spacy.load("en_core_web_sm")):
    return [token.text for token in embeddings(text)]


def pipelines():

    model = load('model.joblib')



pipelines()
