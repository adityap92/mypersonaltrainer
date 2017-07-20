# MyPersonalTrainer

MyPersonalTrainer app was built as a Capstone Project for the Udacity Android Nanodegree. This app is a workout diary of sorts.

The user signs in using their google email and enters some preliminary data about themselves. From here the user will be able to plan their workouts by day each week using predefined exercises from a JSON file. User and Workout data is stored in a SQLite database. The user is also able to view a tutorial/informational video pertaining to each workout; this includes instructions on how to complete the exercises.

Workout exercise information is saved dynamically to the database through NumberPickers defining the weight, sets, and reps for each exercise. Once complete the user is able to share their days workout to Google+. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

A project must be setup in the Google APIs Console
https://console.developers.google.com 
The 'google-services.json' file will need to be downloaded and placed in the '/app' directory. This is needed for all Google API calls.

The latest YoutubeAndroidPlayer Jar will need to be downloaded from the below link:
https://developers.google.com/youtube/android/player/downloads/
This will need to be placed in the '/app/libs' directory with the name 'YouTubeAndroidPlayerApi.jar'.

In addition to this jar the a string resource named 'yt_api_key' will need to be created containing the Google API Key which can be found in google-services.json or from the Google API Console.

