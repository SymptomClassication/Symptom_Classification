# SpaCy Classification

This folder contains the code for symtpoms classification using a SpaCy model.

## How to run


The machine should have Python 3 and pip instlaled.
Make sure your machine supports Makefile. If not, you can run the commands in the Makefile manually.

To run the code that tests the training data, run the following command:
    
    make testModel
After doing so, run the following command to test the model on a specific symptom:
    
    python3 classify.py <your_symptom_here>

## How this works

1. Training data, found in the folder 'trainingData', is originally found in a form of two JSON files: 'trainingData.json' that has a list of symptoms in a specific order, and 'matchingTrainingData.json' that has the chapters numbers associated with each symptom.


2.  The 'extract_training_data.py' script is used to couple the symptoms with their respective chapters, randomize them, then divide them so that 80% of the data is used for training and 20% for testing.


3. The 'trainModel.py' script is used to train the SpaCy model on the training data using 'en_sci_core_md' pre-trained word embeddings. The result is a model named 'model.joblib'.


4. The 'machineLearningTesting.py' is used to test the model on the remaining 20% of data that was not used for training. The result is the F-score of the model.


5. Eventually, the 'classify.py' script is used to classify an input symptom, which is given using the command line.


