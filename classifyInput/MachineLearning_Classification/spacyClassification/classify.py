from joblib import dump, load
import spacy
import json
import numpy as np
import sys


def tokenize(text, embeddings=spacy.load("en_core_sci_md")):
    return [token.text for token in embeddings(text)]


def pipelines(symptom):

    model = load('model.joblib')

    prediction = model.predict([symptom])
    classifications = []
    confidences = []

    with open('machineLearningChapters.json', 'r') as f:
        chapters = list(json.load(f))

    for pred in prediction[0]:  # Iterate through multiple predictions
        classification = ""
        for chapter in chapters:
            if chapter['id'] == pred:
                if chapter['id'] == chapter['chapterId']:
                    classification = chapter['name']
                else:
                    for bigger_chapter in chapters:
                        if chapter['chapterId'] == bigger_chapter['id']:
                            classification = bigger_chapter['name'] + " - " + chapter['name']
                break
        classifications.append(classification)

    # Obtain the probabilities from the MultiOutputClassifier
    prediction_proba = model.predict_proba([symptom])

    for i, pred in enumerate(prediction[0]):
        # Get the probability for the predicted class
        confidence = prediction_proba[i][0][np.where(model.named_steps['classifier'].estimators_[i].classes_ == pred)[0][0]]
        confidences.append(confidence)

    return classifications, confidences

if __name__ == "__main__":
    classification, confidence = pipelines(" ".join(sys.argv[1:]))
    print(f"Classification: {classification}, Confidence: {confidence}")

