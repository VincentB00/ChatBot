import nltk
from nltk.stem.lancaster import LancasterStemmer
stemmer = LancasterStemmer()

import numpy
import tflearn
import tensorflow
import random
import json
import threading


class ChatBotBrain:
    jsonFile = ''
    words = []
    labels = []
    docs_x = []
    docs_y = []
    training = []
    output = []
    model = ''

    # preprocessing data

    def preprocessingData(data):
        ChatBotBrain.jsonFile = ''
        ChatBotBrain.words = []
        ChatBotBrain.labels = []
        ChatBotBrain.docs_x = []
        ChatBotBrain.docs_y = []
        ChatBotBrain.training = []
        ChatBotBrain.output = []
        ChatBotBrain.model = ''
        ChatBotBrain.jsonFile = data
        # extract data
        for intent in data:
            for pattern in intent['patterns']:
                wrds = nltk.word_tokenize(pattern)
                ChatBotBrain.words.extend(wrds)
                ChatBotBrain.docs_x.append(wrds)
                ChatBotBrain.docs_y.append(intent["tag"])
                
            if intent['tag'] not in ChatBotBrain.labels:
                ChatBotBrain.labels.append(intent['tag'])

        # steam each word in ChatBotBrain.words
        ChatBotBrain.words = [stemmer.stem(w.lower()) for w in ChatBotBrain.words if w != "?"]
        ChatBotBrain.words = sorted(list(set(ChatBotBrain.words)))

        ChatBotBrain.labels = sorted(ChatBotBrain.labels)



        # create ChatBotBrain.training data for ChatBotBrain.model
        out_empty = [0 for _ in range(len(ChatBotBrain.labels))]

        for x, doc in enumerate(ChatBotBrain.docs_x):
            bag = []

            wrds = [stemmer.stem(w.lower()) for w in doc]

            for w in ChatBotBrain.words:
                if w in wrds:
                    bag.append(1)
                else:
                    bag.append(0)

            output_row = out_empty[:]
            output_row[ChatBotBrain.labels.index(ChatBotBrain.docs_y[x])] = 1

            ChatBotBrain.training.append(bag)
            ChatBotBrain.output.append(output_row)

    def loadModel(jsonFile = ''):
        if jsonFile != '':
            ChatBotBrain.preprocessingData(jsonFile)

        ChatBotBrain.training = numpy.array(ChatBotBrain.training)
        ChatBotBrain.output = numpy.array(ChatBotBrain.output)

        xShape = len(ChatBotBrain.training[0])

        net = tflearn.input_data(shape=[None, xShape])
        net = tflearn.fully_connected(net, 8)
        net = tflearn.fully_connected(net, 8)
        net = tflearn.fully_connected(net, len(ChatBotBrain.output[0]), activation="softmax")
        net = tflearn.regression(net)

        ChatBotBrain.model = tflearn.DNN(net)

        ChatBotBrain.model.load("ChatBotNeuralNetworkModel")

    def createAndTrainModel(jsonFile = ''):
        if jsonFile != '':
            ChatBotBrain.preprocessingData(jsonFile)

        ChatBotBrain.training = numpy.array(ChatBotBrain.training)
        ChatBotBrain.output = numpy.array(ChatBotBrain.output)

        xShape = len(ChatBotBrain.training[0])

        net = tflearn.input_data(shape=[None, xShape])
        net = tflearn.fully_connected(net, 8)
        net = tflearn.fully_connected(net, 8)
        net = tflearn.fully_connected(net, len(ChatBotBrain.output[0]), activation="softmax")
        net = tflearn.regression(net)
        ChatBotBrain.model = tflearn.DNN(net)
        ChatBotBrain.model.fit(ChatBotBrain.training, ChatBotBrain.output, n_epoch=1000, batch_size=8, show_metric=True)

        ChatBotBrain.model.save("ChatBotNeuralNetworkModel")

    # this function will do one hot encode on a sentence base on ChatBotBrain.words array
    def bag_of_words(s, wordsT):
        bag = [0 for _ in range(len(wordsT))]

        s_words = nltk.word_tokenize(s)
        s_words = [stemmer.stem(word.lower()) for word in s_words]

        for se in s_words:
            for i, w in enumerate(wordsT):
                if w == se:
                    bag[i] = 1
                
        return numpy.array(bag)

    def predictTag(question):
        results = ChatBotBrain.model.predict([ChatBotBrain.bag_of_words(question, ChatBotBrain.words)])
        results_index = numpy.argmax(results)
        tag = ChatBotBrain.labels[results_index]
        return tag;

    def predictAnswer(question):
        responses = []
        tagT = ChatBotBrain.predictTag(question)

        for intent in ChatBotBrain.jsonFile:
            tag = intent["tag"]
            if tag == tagT:
                responses = intent['responses']
                break

        return (random.choice(responses))

    def chat():
        print("Start talking with the bot (type quit to stop)!")
        while True:
            question = input("Human: ")
            if question.lower() == "quit":
                break
            print(ChatBotBrain.predictAnswer(question))