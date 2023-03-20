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
    classification = ""
    confidence = 0

    with open('machineLearningChapters.json', 'r') as f:
        chapters = list(json.load(f))

    # Create a mapping between chapter IDs and indices in the probability array
    id_to_index = {}
    for idx, estimator in enumerate(model.named_steps['classifier'].estimators_):
        id_to_index[estimator.classes_[0]] = idx

    for chapter in chapters:
        if chapter['id'] == prediction[0][0]:  # Access the index for the predicted class
            if chapter['id'] == chapter['chapterId']:
                classification = chapter['name']
            else:
                for bigger_chapter in chapters:
                    if chapter['chapterId'] == bigger_chapter['id']:
                        classification = bigger_chapter['name'] + " - " + chapter['name']

    if prediction[0][0] in id_to_index:
        # Obtain the probabilities from the LogisticRegression classifier directly
        probabilities = model.named_steps['classifier'].estimators_[id_to_index[prediction[0][0]]].predict_proba(model.named_steps['vectorizer'].transform([symptom]))[0]
        predicted_class_index = np.argmax(probabilities)  # Get the index of the predicted class in the probabilities array
        confidence = probabilities[predicted_class_index]  # Get the probability for the predicted class

    return classification, confidence

if __name__ == "__main__":
    classification, confidence = pipelines(" ".join(sys.argv[1:]))
    print(f"Classification: {classification}, Confidence: {confidence}")

