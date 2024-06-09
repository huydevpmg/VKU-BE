const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const passport = require('passport');
const authRoutes = require('./routes/authRoutes');
const crawlRoutes = require('./routes/crawlRoutes');
const firebaseService = require('./config/firebase');

const { OAuth2Client } = require('google-auth-library');
require('dotenv').config();
const googleClientId = process.env.GOOGLE_CLIENT_ID;
const googleClient = new OAuth2Client(googleClientId);

const { db } = require('./config/firebase');


const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());  // Middleware để phân tích cú pháp JSON
app.use(bodyParser.json());
app.use(passport.initialize());

// Initialize Firebase
firebaseService.initialize

app.post('/verify-user', async (req, res) => {
    const { username ,email, userId, profilePictureUrl } = req.body;
    if (!email || !userId) {
      return res.status(400).send('Missing user data');
    }
  
    try {
      const userRef = db.collection('users').doc(userId);
      const doc = await userRef.get();
  
      if (doc.exists) {
        return res.status(200).json(doc.data());
      } else {
        const userData = { username,email, userId, profilePictureUrl };
        await userRef.set(userData);
        return res.status(201).json(userData);
      }
    } catch (error) {
      console.error('Error verifying user:', error);
      return res.status(500).send('Internal Server Error');
    }
  });
// Routes
// app.use('/', authRoutes);
app.use('/', crawlRoutes);

// Start server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
