from pandas import *
import matplotlib.pyplot as plt
import numpy as np


def count_chapters():
    csv = read_csv('../spacyClassification/trainingData.csv')
    labels = csv['label'].tolist()
    labels_set = set(labels)
    labels_dict = {}
    for l in labels_set:
        labels_dict[l] = labels.count(l)

    avg = sum(list(labels_dict.values()))/len(labels_dict)
    print("Average: " + str(avg))
    print("Max: " + str(max(list(labels_dict.values()))) + " of chapter ID " + str(list(labels_dict.keys())[list(labels_dict.values()).index(max(list(labels_dict.values())))]))


    plt.bar(range(len(labels_dict)), list(labels_dict.values()), align='center')
    plt.xticks(range(len(labels_dict)), list(labels_dict.keys()), rotation='vertical')
    ax = plt.gca()
    ax.set_xticks(ax.get_xticks()[::1])
    #plt.axhline(y= avg, color='r', linestyle='-')
    avg = avg/2.5
    plt.axhline(y= avg + (avg/2), color='y', linestyle='-')
    plt.axhline(y= avg - (avg/2), color='y', linestyle='-')
    print(avg + (avg/2))
    print(avg - (avg/2))
    print("Current standard deviation: " + str(np.std(list(labels_dict.values()))))
    print("If we downsample and se SMOTE based on the yellow lines, we would have a standard deviation of " + str(np.std([avg + (avg/2), avg - (avg/2)])))
    print("Red line represents the average number of chapters per label")
    print("Yellow lines represent where the data would be if we downsampled and used SMOTE")
    plt.show()


if __name__ == '__main__':
    count_chapters()
