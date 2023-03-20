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

2. New data are found in folder 'more-data' in a form of RTF files. Parsing of such data is done using the script 'parse_rtf.py' and the old data along with the new data are added into two new JSON files: 'trainingData_new.json' and 'matchingTrainingData_new.json'.

3. Random coupling is used to randomly pair the data to create multi-labeled data. This is done using the script 'generate_multilabels.py'. The generated data are then seperated into 80% for training and 20% for testing. The script takes the required number of data to be generated as an input. It is set to 12000 on default. 

5. The 'trainMultiLabelModel.py' script is used to train the SpaCy model on the training data using 'en_sci_core_md' pre-trained word embeddings. The result is a model named 'model.joblib'.


4. The 'Multi_MachineLearningTesting.py' is used to test the model on the remaining 20% of data that was not used for training. The result is the average precision score.

5. Eventually, the 'classify.py' script is used to classify an input symptom, which is given using the command line.
