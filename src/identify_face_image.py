from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from collections import OrderedDict

import tensorflow as tf
from scipy import misc
import cv2
import numpy as np
import facenet
import detect_face
import os
import time
import pickle
import sys
import csv
from os import listdir 
from os.path import isfile, join


from flask import Flask
app = Flask(__name__)
@app.route("/")
def hello():    
    img_paths = [f for f in listdir("C:/moodle/server/moodle/uploads") if isfile(join("C:/Moodle/server/moodle/uploads", f))]
    #print('Face Detected: %d' % nrof_faces)
    print(img_paths)
    for x in img_paths:
        img_path='C:/moodle/server/moodle/uploads/'+ x
        print(img_path)
        modeldir = './model/20170511-185253.pb'
        classifier_filename = './class/classifier.pkl'
        npy='./npy'
        train_img="./train_img"

        with tf.Graph().as_default():
            gpu_options = tf.GPUOptions(per_process_gpu_memory_fraction=0.6)
            sess = tf.Session(config=tf.ConfigProto(gpu_options=gpu_options, log_device_placement=False))
            with sess.as_default():
                pnet, rnet, onet = detect_face.create_mtcnn(sess, npy)

                minsize = 20  # minimum size of face
                threshold = [0.6, 0.7, 0.7]  # three steps's threshold
                factor = 0.709  # scale factor
                margin = 44
                frame_interval = 3
                batch_size = 1000
                image_size = 182
                input_image_size = 160
                

                HumanNames = os.listdir(train_img)
                HumanNames.sort()

                print('Loading feature extraction model')
                facenet.load_model(modeldir)

                images_placeholder = tf.get_default_graph().get_tensor_by_name("input:0")
                embeddings = tf.get_default_graph().get_tensor_by_name("embeddings:0")
                phase_train_placeholder = tf.get_default_graph().get_tensor_by_name("phase_train:0")
                embedding_size = embeddings.get_shape()[1]


                classifier_filename_exp = os.path.expanduser(classifier_filename)
                with open(classifier_filename_exp, 'rb') as infile:
                    (model, class_names) = pickle.load(infile)

                # video_capture = cv2.VideoCapture("akshay_mov.mp4")
                c = 0


                print('Start Recognition!')
                prevTime = 0
                # ret, frame = video_capture.read()
                frame = cv2.imread(img_path,0)

                frame = cv2.resize(frame, (0,0), fx=0.5, fy=0.5)    #resize frame (optional)

                curTime = time.time()+1    # calc fps
                timeF = frame_interval

                if (c % timeF == 0):
                    find_results = []

                    if frame.ndim == 2:
                        frame = facenet.to_rgb(frame)
                    frame = frame[:, :, 0:3]
                    bounding_boxes, _ = detect_face.detect_face(frame, minsize, pnet, rnet, onet, threshold, factor)
                    nrof_faces = bounding_boxes.shape[0]
                    
                    print('Face Detected: %d' % nrof_faces)

                    totalface = nrof_faces
                    templist = []
                    
                    if nrof_faces > 0:
                        det = bounding_boxes[:, 0:4]
                        img_size = np.asarray(frame.shape)[0:2]

                        cropped = []
                        scaled = []
                        scaled_reshape = []
                        bb = np.zeros((nrof_faces,4), dtype=np.int32)

                        for i in range(nrof_faces):
                            emb_array = np.zeros((1, embedding_size))

                            bb[i][0] = det[i][0]
                            bb[i][1] = det[i][1]
                            bb[i][2] = det[i][2]
                            bb[i][3] = det[i][3]

                            # inner exception
                            if bb[i][0] <= 0 or bb[i][1] <= 0 or bb[i][2] >= len(frame[0]) or bb[i][3] >= len(frame):
                                print('face is too close')
                                continue

                            cropped.append(frame[bb[i][1]:bb[i][3], bb[i][0]:bb[i][2], :])
                            cropped[i] = facenet.flip(cropped[i], False)
                            scaled.append(misc.imresize(cropped[i], (image_size, image_size), interp='bilinear'))
                            scaled[i] = cv2.resize(scaled[i], (input_image_size,input_image_size),
                                                interpolation=cv2.INTER_CUBIC)
                            scaled[i] = facenet.prewhiten(scaled[i])
                            scaled_reshape.append(scaled[i].reshape(-1,input_image_size,input_image_size,3))
                            feed_dict = {images_placeholder: scaled_reshape[i], phase_train_placeholder: False}
                            emb_array[0, :] = sess.run(embeddings, feed_dict=feed_dict)
                            predictions = model.predict_proba(emb_array)
                            print(predictions)
                            best_class_indices = np.argmax(predictions, axis=1)
                            # print(best_class_indices)
                            best_class_probabilities = predictions[np.arange(len(best_class_indices)), best_class_indices]
                            print(best_class_probabilities)
                            cv2.rectangle(frame, (bb[i][0], bb[i][1]), (bb[i][2], bb[i][3]), (0, 255, 0), 2)    #boxing face
                            if best_class_probabilities < 0.45 :
                                continue 
                            
                            
                            

                            #plot result idx under box
                            text_x = bb[i][0]
                            text_y = bb[i][3] + 20
                            print('Result Indices: ', best_class_indices[0])
                            print(HumanNames)
                            for H_i in HumanNames:
                                # print(H_i)
                                if HumanNames[best_class_indices[0]] == H_i:
                                    result_names = HumanNames[best_class_indices[0]]
                                    cv2.putText(frame, result_names, (text_x, text_y), cv2.FONT_HERSHEY_COMPLEX_SMALL,
                                                1, (0, 0, 255), thickness=1, lineType=2)


                        print('Face divyank Detected: %d' % totalface)
                        templist = sorted(set(templist))
                        print(templist)
                        star='a'

                        with open('temp.csv', 'w', newline='') as file:

                            wr = csv.writer(file,delimiter=',')
                            for line in templist:
                                file.write(line)
                                file.write('\n')
                        
                        with open('temp.csv', 'r') as myfile:
                            with open('tempdollar.csv', 'r') as filemy:

                                r1 = csv.reader(myfile)
                                r2 =csv.reader(filemy)

                                for line in r1:
                                    print(line)
                                    for line2 in r2:
                                        print(line2)
                                        if line == line2 :
                                            with open ('original.csv','a',newline='') as file4:
                                                wrr=csv.writer(file4,delimiter=',')
                                                wrr.writerows(star)
                                                file4.write('\t')
                                    filemy.seek(0)

                    else:
                        print('Unable to align')
                cv2.imshow('Image', frame)
                cv2.destroyAllWindows()
    return templist
    
