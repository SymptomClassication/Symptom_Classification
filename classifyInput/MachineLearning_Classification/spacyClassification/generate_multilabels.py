import json
import random
import os




def pipeline(max_pairs=100000):
    with open("trainingData/matchingTrainingData.json", "r") as f:
        labels = list(json.load(f))

    with open("trainingData/trainingData.json", "r") as f:
        train_data = list(json.load(f))

    test_labels = []
    test_train = []

    for i in range(len(labels)):
        #if labels[i] == 3 or labels[i] == 102 or labels[i] == 26 or labels[i] == 32 or labels[i] == 18:
        test_labels.append(labels[i])
        test_train.append(train_data[i])

    to_shuffle = list(zip(test_train, test_labels))

    multi_labeled_data = []
    for i in range(max_pairs):
        pair = random.sample(to_shuffle, 2)
        labels = [pair[0][1], pair[1][1]]
        multi_labeled_data.append(("{} and {}".format(pair[0][0], pair[1][0]), labels))
    print(multi_labeled_data[3])
    random.shuffle(multi_labeled_data)

    to_train = multi_labeled_data[:int(len(multi_labeled_data) * 0.8)]
    to_train_data, to_train_labels = zip(*to_train)
    to_test = multi_labeled_data[int(len(multi_labeled_data) * 0.8):]
    to_test_data, to_test_labels = zip(*to_test)

    json.dump(to_train_data, open("trainingData/to_train_multidata.json", "w"))
    json.dump(to_test_data, open("trainingData/to_test_multidata.json", "w"))
    json.dump(to_train_labels, open("trainingData/to_train_multilabels.json", "w"))
    json.dump(to_test_labels, open("trainingData/to_test_multilabels.json", "w"))

pipeline()
