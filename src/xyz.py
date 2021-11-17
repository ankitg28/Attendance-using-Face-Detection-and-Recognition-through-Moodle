from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
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

import glob

from os import listdir 
from os.path import isfile, join 
files = [f for f in listdir("check") if isfile(join("check", f))]

print(files)