# rodda-road-damage-classification
Bangkit Capstone Project

## Machine Learning

**Dependencies**
```
  pip install tensorflow
  pip install zipfile36
  pip install opencv-python
  pip install numpy
  pip install matplotlib
  pip install keras-utils
```

1. Use RoadDamageClassification.ipynb for **Training**
2. Use app.py for **Deployment**

## Android Apps

**Features**
1. Login/Registration  
<img src="https://user-images.githubusercontent.com/79303742/121196274-8a8dce00-c89a-11eb-9f80-c85a91224720.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/79303742/121196348-9d080780-c89a-11eb-8d30-4b45f1f47b15.jpg" width="200" height="400">

2. Report of Damaged Road (Main Page)  
List of reports - Navigation Bar - Detail Item (if the report item is clicked)  
<img src="https://user-images.githubusercontent.com/79303742/121196516-bf9a2080-c89a-11eb-93de-4f1c7edf70c8.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/79303742/121196552-c9238880-c89a-11eb-89a5-b2a5cbd2dbf2.jpg" width="200" height="400"> <img src="https://user-images.githubusercontent.com/79303742/121201148-81066500-c89e-11eb-9942-a5fdb995d2b3.jpg" width="200" height="400">

3. Fill the information about the road  
Road - Damaged Road (Detail) - Location  
<img src="https://user-images.githubusercontent.com/79303742/121199413-1b65a900-c89d-11eb-98f3-a390a2c3772b.jpg" width="200" height="400">  <img src="https://user-images.githubusercontent.com/79303742/121199438-202a5d00-c89d-11eb-8ded-e343679b862c.jpg" width="200" height="400">  <img src="https://user-images.githubusercontent.com/79303742/121199454-23bde400-c89d-11eb-86ab-d416d9a1b87b.jpg" width="200" height="400">


## Cloud Computing

**Create Cloud Instance**

1. Name the instance (ex. CAP-0422)
2. Set machine type to n1-standard-1
3. Set Location Us-Central1
4. Change boot disk to ubuntu (ver. 16 or 18) and use 25GB of SSD storage
5. Check allow HTTP
6. Click "Management, security, disks, networking, sole tenancy", Under Networking click Network interfaces and change External IP to static
7. Click Create

gcloud beta compute --project=cap-0422 instances create cap-0422 --zone=us-central1-a --machine-type=n1-standard-1 --subnet=default --address=104.154.249.90 --network-tier=PREMIUM --maintenance-policy=MIGRATE --service-account=969576237823-compute@developer.gserviceaccount.com --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/trace.append --tags=http-server --image=ubuntu-1804-bionic-v20210604 --image-project=ubuntu-os-cloud --boot-disk-size=25GB --boot-disk-type=pd-ssd --boot-disk-device-name=cap-0422 --no-shielded-secure-boot --shielded-vtpm --shielded-integrity-monitoring --reservation-affinity=any

**Create Cloud Storage**

1. Create bucket with unique name
2. Set the location to regional and choose closest to you
3. Click create

**Upload Machine Learning Model & API Scipt**

1. In created bucket click upload file
2. Choose the ML model and API script in your local machine

**Running the API server**

1. SSH to created instance
2. Copy file from bucket to vm using gsutil cp gs://<BUCKETNAME>/<FILENAME> <FILENAME>
3. Install every package that was needed for the script to be run
4. Install tmux (for background process, so it won't stop even if the vm is closed)
5. Type tmux
6. Run your API script

*To see running backgroud process type "tmux list"
*To enter the running process type "tmux -a t 0"
*To close tmux press "ctrl+d"
