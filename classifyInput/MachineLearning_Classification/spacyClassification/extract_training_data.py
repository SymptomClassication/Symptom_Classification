import json
import random
import os

print(os.getcwd())


def pipeline():
    with open("./trainingData/matchingTrainingData.json", "r") as f:
        labels = list(json.load(f))

    with open("./trainingData/trainingData.json", "r") as f:
        train_data = list(json.load(f))

    test_labels = []
    test_train = []

    for i in range(len(labels)):
        #if labels[i] == 3 or labels[i] == 102 or labels[i] == 26 or labels[i] == 32 or labels[i] == 18:
            test_labels.append(labels[i])
            test_train.append(train_data[i])

    to_shuffle = list(zip(test_train, test_labels))

    random.shuffle(to_shuffle)

    to_train = to_shuffle[:int(len(to_shuffle) * 0.8)]
    to_train_data, to_train_labels = zip(*to_train)
    to_test = to_shuffle[int(len(to_shuffle) * 0.8):]
    to_test_data, to_test_labels = zip(*to_test)

    json.dump(to_train_data, open("./trainingData/to_train_data.json", "w"))
    json.dump(to_test_data, open("./trainingData/to_test_data.json", "w"))
    json.dump(to_train_labels, open("./trainingData/to_train_labels.json", "w"))
    json.dump(to_test_labels, open("./trainingData/to_test_labels.json", "w"))


pipeline()
