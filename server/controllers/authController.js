const { OAuth2Client } = require('google-auth-library');
const { db } = require('../config/firebase');
require('dotenv').config();
const googleClientId = process.env.GOOGLE_CLIENT_ID;
const googleClient = new OAuth2Client(googleClientId);

exports.verifyIdToken = async (req, res) => {
    const idToken = req.body.idToken;

    try {
        const ticket = await googleClient.verifyIdToken({
            idToken,
            audience: googleClientId,
        });
        const payload = ticket.getPayload();
        const userId = payload['sub'];

        // Save user to Firestore
        const userRef = db.collection('users').doc(userId);
        await userRef.set(payload, { merge: true });

        res.status(200).json(payload);
    } catch (error) {
        console.error('Error verifying ID token:', error);
        res.status(401).send('Invalid token');
    }
};
