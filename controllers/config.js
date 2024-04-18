// const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
// const { getFirestore, Timestamp, FieldValue, Filter } = require('firebase-admin/firestore');

const firebase = require("firebase");
const firebaseConfig = {
  apiKey: "AIzaSyDAc-G3iWdE7IQoaQSEGO2GAN_Chr_syZg",
  authDomain: "vku-app-cecd3.firebaseapp.com",
  projectId: "vku-app-cecd3",
  storageBucket: "vku-app-cecd3.appspot.com",
  messagingSenderId: "580572019975",
  appId: "1:580572019975:web:191cac08ba0235790499a2",
  measurementId: "G-J44PZWLS9G",
};

firebase.initializeApp(firebaseConfig);
const database = firebase.firestore();
// const database = firebase.getFirestore();

module.exports = database;
