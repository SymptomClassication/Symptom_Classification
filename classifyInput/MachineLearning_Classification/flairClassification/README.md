# How this folder is structured:

## 1. "TrainingData" folder

* This folder contains the training data in a form of two JSON files:

    * "trainingData.json" 
      * Contains the list of symptoms 
      * The order is important. Hence, this file is only to be edited later on using the code, to add new symptoms/corrections for retraining
    
    * "trainingDataMatching.json" 
      * Contains the labels for the symptoms in a form of numbers, each indicating the chapter name. You can find the list of chapters and their numbers in the file **"machineLearningChapters.json"**
      * The order is important. Thus, this file is to be edited only by code, to add new symptoms/corrections for retraining

## 2. "TrainingDataCSV" folder

* This file is created by the code of **"flairClassification.py"**
* We created this just to follow an example, which uses the **CSVClassificationCorpus**. 
* The files are created from the JSON files in the "TrainingData" folder

## 3. "machineLearningChapters.json" file

* This file contains the list of chapters and their numbers.

## 4. "flairClassification.py" file

* This file contains the code for training the model and testing it. The model would be found in a new "data_tsf" folder, along with the JOBLIB versions of the model, and the training log.

## 5. "balancedTrainingData.json" file

* I have used this file to generate the diagram of the training data. 


## notes

* Any remaining files could be ignored. they are the result of different versions of the code based on multiple examples followed.
* The "flairClassification.py" currently runs using the "createModel()" function, then loads the generated model using joblib in the "loadModel()" function.
* The examples tested eventaully after training the model are two, and can be found/edited in the "loadModel()" function in "flairClassification.py"

# How to run the flairClassification.py file

1. Make sure you have the following libraries:

   * flair (if encountering an error downloading this, downloading the deprecated version might be the solution:
       
            pip install flair —use-deprecated=backtrack-on-build-failures

   * pandas
   * re
   * csv
   * torch
   * joblib
   * numpy

2. make sure the "TrainingData" folder is in the same directory as the "flairClassification.py" file

3. run the file as normal:


        python flairClassification.py
