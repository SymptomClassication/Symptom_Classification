import json
import random
import os
import csv
import re




def pipeline(max_pairs=1200):

    with open("trainingData/trainingData_new.json", "r") as f:
        infoList = json.load(f)
    with open("trainingData/matchingTrainingData_new.json", "r") as f:
        labelsList = json.load(f)

    labels = {}
    for i in range(len(labelsList)):
        if labelsList[i] in labels:
            labels[labelsList[i]] += 1
        else:
            labels[labelsList[i]] = 1

    l = list(dict(sorted(labels.items(), key=lambda item: item[1])).keys())


    file = open('trainingData.csv', 'w', newline ='')

    with file:
        # identifying header
        header = ['sentence', 'label']
        writer = csv.DictWriter(file, fieldnames=header)

        # writing data row-wise into the csv file
        writer.writeheader()
        for i in range(len(infoList)):
            if labelsList[i] not in l:
                continue
            removeNum = re.sub(r"[0-9]", "", infoList[i])
            removeDot = re.sub(r"\.", "", removeNum)
            removeStar = re.sub(r"\*", "", removeDot)
            removeBracket = re.sub(r"\[*\]*", "", removeStar)
            removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
            removeComma = re.sub(r",", "", removeParenthesis)
            removeSemiColon = re.sub(r";", "", removeComma)
            writer.writerow({'sentence': removeSemiColon,'label':labelsList[i]})

    with open("trainingData/matchingTrainingData_new.json", "r") as f:
        labels = list(json.load(f))

    with open("trainingData/trainingData_new.json", "r") as f:
        train_data = list(json.load(f))

    test_labels = []
    test_train = []

    for i in range(len(labels)):
        if labels[i] not in l:
            continue
        #if labels[i] == 3 or labels[i] == 102 or labels[i] == 26 or labels[i] == 32 or labels[i] == 18:
        test_labels.append(labels[i])
        test_train.append(train_data[i])

    to_shuffle = list(zip(test_train, test_labels))

    multi_labeled_data = []
    for i in range(max_pairs):
        pair = random.sample(to_shuffle, 2)
        labels = [pair[0][1], pair[1][1]]
        multi_labeled_data.append(("{} with {}".format(pair[0][0], pair[1][0]), labels))
    random.shuffle(multi_labeled_data)

    to_train = multi_labeled_data[:int(len(multi_labeled_data) * 0.8)]
    to_train_data, to_train_labels = zip(*to_train)
    to_test = multi_labeled_data[int(len(multi_labeled_data) * 0.8):]
    to_test_data, to_test_labels = zip(*to_test)

    json.dump(to_train_data, open("trainingData/to_train_multidata_new.json", "w"))
    json.dump(to_test_data, open("trainingData/to_test_multidata_new.json", "w"))
    json.dump(to_train_labels, open("trainingData/to_train_multilabels_new.json", "w"))
    json.dump(to_test_labels, open("trainingData/to_test_multilabels_new.json", "w"))

pipeline()
