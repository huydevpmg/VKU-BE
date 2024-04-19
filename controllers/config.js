require("dotenv").config()

const firebaseConfig = {
  apiKey: process.env.FIREBASE_API_KEY,
  authDomain: "vku-app-cecd3.firebaseapp.com",
  projectId: "vku-app-cecd3",
  storageBucket: "vku-app-cecd3.appspot.com",
  messagingSenderId: "580572019975",
  appId: "1:580572019975:web:191cac08ba0235790499a2",
  measurementId: "G-J44PZWLS9G",
};

const firebase = require("firebase");
firebase.initializeApp(firebaseConfig);
const database = firebase.firestore();

// const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
// const { getFirestore, Timestamp, FieldValue, Filter } = require('firebase-admin/firestore');
// initializeApp(firebaseConfig)
// const database = getFirestore();

module.exports = database;