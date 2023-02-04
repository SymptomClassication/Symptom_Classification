import random, json, re, pandas as pd
from flair.data import Corpus
from flair.data import Sentence
from flair.embeddings import FlairEmbeddings,WordEmbeddings,DocumentLSTMEmbeddings
from flair.models import TextClassifier
from flair.trainers import ModelTrainer
import csv
import numpy as np
from flair.datasets import CSVClassificationCorpus
from flair.data import Corpus
def create_corpus_files(info, labels):
    # Split the data into three parts

    with open(info, "r") as f:
        infoList = json.load(f)
    with open(labels, "r") as f:
        labelsList = json.load(f)


    for line in infoList:
            removeNum = re.sub(r"[0-9]", "", line)
            removeDot = re.sub(r"\.", "", removeNum)
            removeStar = re.sub(r"\*", "", removeDot)
            removeBracket = re.sub(r"\[*\]*", "", removeStar)
            removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
            removeComma = re.sub(r",", "", removeParenthesis)
            removeSemiColon = re.sub(r";", "", removeComma)
            infoList[infoList.index(line)] = removeSemiColon.strip()
    data = list(zip(infoList, labelsList))
    random.shuffle(data)
    num_data = len(data)
    train_data = data[:int(num_data * 0.7)]
    dev_data = data[int(num_data * 0.7):int(num_data * 0.9)]
    test_data = data[int(num_data * 0.9):]

    # Write the data to files
    with open("trainingData/train.txt", "w") as f:
        for pair in train_data:
            f.write(f"{pair[0]}\t{pair[1]}\n")
    with open("trainingData/dev.txt", "w") as f:
        for pair in dev_data:
            f.write(f"{pair[0]}\t{pair[1]}\n")
    with open("trainingData/test.txt", "w") as f:
        for pair in test_data:
            f.write(f"{pair[0]}\t{pair[1]}\n")


def create_model():
    # Define the columns of the file
    #columns = {0: 'text', 1: 'label', 2: 'ner'}

    # Load the data
    '''corpus = ColumnCorpus('trainingData', columns,
                          train_file='train.txt',
                          dev_file='dev.txt',
                          test_file='test.txt',
                          column_delimiter='\t')'''

    with open("trainingData/trainingData.json", "r") as f:
        infoList = json.load(f)
    with open("trainingData/matchingTrainingData.json", "r") as f:
        labelsList = json.load(f)

    file = open('trainingData.csv', 'w', newline ='')

    with file:
        # identifying header
        header = ['sentence', 'label']
        writer = csv.DictWriter(file, fieldnames=header)

        # writing data row-wise into the csv file
        writer.writeheader()
        for i in range(len(infoList)):
            writer.writerow({'sentence': infoList[i], 'label': labelsList[i]})

    df = pd.read_csv("trainingData.csv")

    train,test,dev = np.split(df,[int(.6*len(df)),int(.8*len(df))])

    train.to_csv("trainingDataCSV/train.csv")
    test.to_csv("trainingDataCSV/test.csv")
    dev.to_csv("trainingDataCSV/dev.csv")

    column_name_map = {2:"label_topic",1:"text"}

    data_folder = 'trainingDataCSV/'

    corpus_csv: Corpus = CSVClassificationCorpus(data_folder,column_name_map=column_name_map,skip_header=True,delimiter=',', label_type = "label_topic")
    label_dict_csv = corpus_csv.make_label_dictionary(label_type = "label_topic")

    word_embeddings = FlairEmbeddings('news-backward-fast')

    #document_embeddings = DocumentRNNEmbeddingss(word_embeddings,hidden_size=512,reproject_words=True,reproject_words_dimension=256)

    #clf = TextClassifier(document_embeddings,label_dictionary=label_dict_csv)

    #trainer = ModelTrainer(clf,corpus_csv)

    #trainer.train('data_fst/',max_epochs=2)



    '''info = []
    labels = []
    for i in infoList:
        info.append(Sentence(i))
    for i in labelsList:
        labels.append(str(i))


    corpusSentences = []
    for sentence, label in zip(info, labels):
        sentence.add_label(value=label, typename="label")
        corpusSentences.append(sentence)

    corpus = Corpus(corpusSentences)

    # Choose a pre-trained word embedding
    word_embeddings = WordEmbeddings('glove')

    # Initialize the text classifier
    classifier = TextClassifier(word_embeddings, label_dictionary=corpus.make_label_dictionary(label_type="label"), multi_label=False, label_type="label")

    # Initialize the model trainer
    trainer = ModelTrainer(classifier, corpus)

    # Start training
    trainer.train('models', learning_rate=0.1, mini_batch_size=32, anneal_factor=0.5, patience=5, max_epochs=100)'''
def pipeline():

    create_corpus_files("trainingData/trainingData.json", "trainingData/matchingTrainingData.json")

    create_model()

pipeline()

