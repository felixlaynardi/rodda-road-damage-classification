import json
import keras
from flask import Flask, request, jsonify
from keras.models import load_model
import firebase_admin
# from firebase_admin import credentials, firestore, initialize_app
from firebase_admin import credentials, firestore

# Library for prediction
from PIL import Image
import urllib.request
import numpy as np
import cv2 as cv2

# Instantiate flask
app = Flask(__name__)

# Connect to firebase
cred = credentials.Certificate("cap-0422-firebase-adminsdk-a7hd7-4b5ef4e8ac.json")
firebase_admin.initialize_app(cred)

db = firestore.client()
ref = db.collection('results')

# Import model
model = load_model('pothole_classification.h5')

def convert_to_array(img):
    # Retrieve image to url and keep the image inside the machine
    request = urllib.request.urlretrieve(img, "image.png")

    # Read image and pre process the image
    img_array = cv2.imread('image.png')
    img = Image.fromarray(img_array, 'RGB')
    image = img.resize((224, 224))
    # Return image as array
    return np.array(image)

# Set label into Crack and Pothole
def get_profile_name(label):
    if label==0:
        return "Crack"
    if label==1:
        return "Pothole"
    if label==2:
        return "Good"

# Predict function
def predict_profile(file):
    # Set empty array for containing prediction results
    predictions = []
    predictions_acc = []
    # Start prediction
    print("Predicting...")

    for f in file:
        # Convert image to array
        array = convert_to_array(f)
        array = array/255.0

        # Convert array into numpy array
        a = []
        a.append(array)
        a = np.array(a)

        # Get score from prediction
        score = model.predict(a,verbose=1)
        label_index = np.argmax(score)
        acc = np.max(score)

        # Get labels
        profile = get_profile_name(label_index)
        predictions.append(profile)
        predictions_acc.append(float(acc))
    return (predictions, predictions_acc)  

@app.route("/")
def hello():
    return "Hello, World!"

@app.route("/predict", methods=['POST'])
def predict():
    # Get JSON requests
    data = request.get_json()
    # Start prediction
    output = predict_profile(data['images'][1:])
    print(output)

    # Split output content
    predictions, predictions_acc = output

    # Create data for input:
    data  = { 
        u"fullName": data["fullName"], 
        u"location": data["location"], 
        u"predictions_acc": predictions_acc,
        u"predictions": predictions, 
        u"images": data["images"],
        u"time": data['time']
    }

    # Create new document in collection with data
    try:
        ref.document().set(data)
        # Return success
        return jsonify({"success": True}), 200
    except Exception as e:
        # Return failed
        return f"An Error Occured: {e}"
    

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)