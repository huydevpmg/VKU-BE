const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const passport = require('passport')

router.post('/verify-id-token', authController.verifyIdToken);




module.exports = router;
